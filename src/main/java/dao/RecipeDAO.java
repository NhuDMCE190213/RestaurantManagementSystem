/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.CommonFunction.*;
import static constant.Constants.*;
import db.DBContext;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Recipe;
import model.RecipeItem;

/**
 *
 * @author PHAT
 */
public class RecipeDAO extends DBContext {

    // --- MENU ITEM as "Recipe" listing (we treat menu_item as recipe owner) ---
    public List<Recipe> getAll() {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT menu_item_id, item_name, status "
                    + "FROM menu_item "
                    + "WHERE (item_name IS NOT NULL) AND (LOWER(status) <> LOWER(N'Deleted')) "
                    + "ORDER BY menu_item_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("item_name");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, name, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Recipe> getAll(int page) {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT menu_item_id, item_name, status "
                    + "FROM menu_item "
                    + "WHERE (item_name IS NOT NULL) AND (LOWER(status) <> LOWER(N'Deleted')) "
                    + "ORDER BY menu_item_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("item_name");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, name, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Search by keyword (applies to menu_item_id and item_name)
     *
     * @param page
     * @param keyword
     * @return
     */
    public List<Recipe> getAll(int page, String keyword) {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT menu_item_id, item_name, status FROM menu_item "
                    + "WHERE (LOWER(status) <> LOWER(N'Deleted')) "
                    + "AND (CAST(menu_item_id AS VARCHAR) LIKE ? OR LOWER(item_name) LIKE ?) "
                    + "ORDER BY menu_item_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            String kw = "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("item_name");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, name, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Get a "Recipe" by menu_item_id; loads menu_item info and recipe items
    public Recipe getElementByID(int menuItemId) {
        try {
            String query = "SELECT menu_item_id, item_name, status "
                    + "FROM menu_item "
                    + "WHERE menu_item_id = ? AND LOWER(status) <> LOWER(N'Deleted')";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{menuItemId});
            if (rs.next()) {
                String name = rs.getString("item_name");
                String status = rs.getString("status");
                Recipe r = new Recipe(menuItemId, name, status);
                // load recipe items that belong to this menu_item
                r.setItems(getItemsByMenuItemId(menuItemId));
                return r;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(String recipeName) {
        try {
            String query = "INSERT INTO recipe (recipe_name, status) VALUES (?, ?)";
            return this.executeQuery(query, new Object[]{recipeName, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int id, String recipeName, String status) {
        try {
            // Note: editing menu_item's name/status is not implemented here; keep original behaviour minimal
            String query = "UPDATE menu_item SET item_name = ?, status = ? WHERE menu_item_id = ?";
            return this.executeQuery(query, new Object[]{recipeName, status, id});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE menu_item SET status = 'Deleted' WHERE menu_item_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(menu_item_id) AS numrow FROM menu_item WHERE (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // --- RECIPE (table 'recipe') items CRUD ---
    public List<RecipeItem> getItemsByMenuItemId(int menuItemId) {
        List<RecipeItem> list = new ArrayList<>();
        try {
            // recipe table contains recipe_item_id, menu_item_id, ingredient_id, quantity, unit, note, status
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.ingredient_id, r.quantity, r.unit, r.note, r.status, ig.ingredient_name "
                    + "FROM recipe r LEFT JOIN ingredient ig ON r.ingredient_id = ig.ingredient_id "
                    + "WHERE r.menu_item_id = ? AND (LOWER(r.status) <> LOWER(N'Deleted')) ORDER BY r.recipe_item_id";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{menuItemId});
            while (rs.next()) {
                int id = rs.getInt("recipe_item_id");
                int mId = rs.getInt("menu_item_id");
                int ingId = rs.getInt("ingredient_id");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                String note = rs.getString("note");
                String status = rs.getString("status");
                String ingName = rs.getString("ingredient_name");

                RecipeItem item = new RecipeItem(id, mId, ingId, qty, unit, note, status, ingName);
                list.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public RecipeItem getRecipeItemById(int recipeItemId) {
        try {
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.ingredient_id, r.quantity, r.unit, r.note, r.status, ig.ingredient_name "
                    + "FROM recipe r LEFT JOIN ingredient ig ON r.ingredient_id = ig.ingredient_id "
                    + "WHERE r.recipe_item_id = ? AND (LOWER(r.status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{recipeItemId});
            if (rs.next()) {
                int id = rs.getInt("recipe_item_id");
                int mId = rs.getInt("menu_item_id");
                int ingId = rs.getInt("ingredient_id");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                String note = rs.getString("note");
                String status = rs.getString("status");
                String ingName = rs.getString("ingredient_name");

                return new RecipeItem(id, mId, ingId, qty, unit, note, status, ingName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public RecipeItem getItemByMenuItemAndIngredient(int menuItemId, int ingredientId) {
        try {
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.ingredient_id, r.quantity, r.unit, r.note, r.status, ig.ingredient_name "
                    + "FROM recipe r LEFT JOIN ingredient ig ON r.ingredient_id = ig.ingredient_id "
                    + "WHERE r.menu_item_id = ? AND r.ingredient_id = ? AND (LOWER(r.status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{menuItemId, ingredientId});
            if (rs.next()) {
                int id = rs.getInt("recipe_item_id");
                int mId = rs.getInt("menu_item_id");
                int ingId = rs.getInt("ingredient_id");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                String note = rs.getString("note");
                String status = rs.getString("status");
                String ingName = rs.getString("ingredient_name");

                return new RecipeItem(id, mId, ingId, qty, unit, note, status, ingName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int addItem(int menuItemId, int ingredientId, double quantity, String unit, String note) {
        RecipeItem recipeItemExist = getItemByMenuItemAndIngredient(menuItemId, ingredientId);
        if (recipeItemExist != null) {
            String oldunit = recipeItemExist.getUnit();
            String newunit = unit;
            Double oldquantity = recipeItemExist.getQuantity();
            Double newquantity = quantity;
            if (!isSameUnitType(oldunit, newunit)) {
                return -2;
            }
            if (oldunit.equals(newunit)) {
                double total = oldquantity + newquantity;
                return editItem(recipeItemExist.getRecipeItemId(), ingredientId, total, unit, note, "Active");
            } else {
                Double newquantity2 = convert(newquantity, newunit, oldunit);
                boolean useNewUnit = newquantity2 > oldquantity;
                if (useNewUnit) {
                    double oldConverted = convert(oldquantity, oldunit, newunit);
                    return editItem(recipeItemExist.getRecipeItemId(), ingredientId, newquantity + oldConverted, newunit, note, "Active");
                } else {
                    double newConverted = convert(newquantity, newunit, oldunit);
                    return editItem(recipeItemExist.getRecipeItemId(), ingredientId, oldquantity + newConverted, oldunit, note, "Active");
                }
            }

        }
        try {
            String query = "INSERT INTO recipe (menu_item_id, ingredient_id, quantity, unit, note, status) VALUES (?, ?, ?, ?, ?, ?)";
            return this.executeQuery(query, new Object[]{menuItemId, ingredientId, quantity, unit, note, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int editItem(int recipeItemId, int ingredientId, double quantity, String unit, String note, String status) {
        RecipeItem current = getRecipeItemById(recipeItemId);
        if (current == null) {
            return -1;
        }

        int menuItemId = current.getRecipeId(); // assumes RecipeItem has getMenuItemId()

        RecipeItem other = getOtherItemByMenuItemAndIngredient(menuItemId, ingredientId, recipeItemId);

        if (other != null) {
            // We will merge edited values into 'other' and delete current.
            String oldunit = other.getUnit();
            String newunit = unit;
            Double oldquantity = other.getQuantity();
            Double newquantity = quantity;

            if (!isSameUnitType(oldunit, newunit)) {
                return -2;
            }

            try {
                if (oldunit.equals(newunit)) {
                    double total = oldquantity + newquantity;
                    // update other
                    int res = this.executeQuery("UPDATE recipe SET quantity = ?, unit = ?, note = ?, status = ? WHERE recipe_item_id = ?",
                            new Object[]{total, oldunit, note, "Active", other.getRecipeItemId()});
                    // mark current as deleted
                    this.executeQuery("UPDATE recipe SET status = 'Deleted' WHERE recipe_item_id = ?", new Object[]{recipeItemId});
                    return res;
                } else {
                    Double newquantity2 = convert(newquantity, newunit, oldunit);
                    if (newquantity2.isNaN()) {
                        return -2;
                    }
                    boolean useNewUnit = newquantity2 > oldquantity;
                    if (useNewUnit) {
                        Double oldConverted = convert(oldquantity, oldunit, newunit);
                        if (oldConverted.isNaN()) {
                            return -2;
                        }
                        double merged = newquantity + oldConverted;
                        int res = this.executeQuery("UPDATE recipe SET quantity = ?, unit = ?, note = ?, status = ? WHERE recipe_item_id = ?",
                                new Object[]{merged, newunit, note, "Active", other.getRecipeItemId()});
                        this.executeQuery("UPDATE recipe SET status = 'Deleted' WHERE recipe_item_id = ?", new Object[]{recipeItemId});
                        return res;
                    } else {
                        Double newConverted = convert(newquantity, newunit, oldunit);
                        if (newConverted.isNaN()) {
                            return -2;
                        }
                        double merged = oldquantity + newConverted;
                        int res = this.executeQuery("UPDATE recipe SET quantity = ?, unit = ?, note = ?, status = ? WHERE recipe_item_id = ?",
                                new Object[]{merged, oldunit, note, "Active", other.getRecipeItemId()});
                        this.executeQuery("UPDATE recipe SET status = 'Deleted' WHERE recipe_item_id = ?", new Object[]{recipeItemId});
                        return res;
                    }
                }
            } catch (SQLException ex) {
                int sqlError = checkErrorSQL(ex);
                if (sqlError != 0) {
                    return sqlError;
                }
                Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
        try {
            String query = "UPDATE recipe SET ingredient_id = ?, quantity = ?, unit = ?, note = ?, status = ? WHERE recipe_item_id = ?";
            return this.executeQuery(query, new Object[]{ingredientId, quantity, unit, note, status, recipeItemId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int deleteItem(int recipeItemId) {
        try {
            String query = "UPDATE recipe SET status = 'Deleted' WHERE (recipe_item_id = ?)";
            return this.executeQuery(query, new Object[]{recipeItemId});
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    // helper: check if menu_item exists / is deleted
    public boolean isRecipeDeleted(int menuItemId) {
        try {
            String query = "SELECT status FROM menu_item WHERE menu_item_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{menuItemId});
            if (rs.next()) {
                String status = rs.getString("status");
                return "Deleted".equalsIgnoreCase(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private double convert(double qty, String from, String to) {
        if (from.equals(to)) {
            return qty;
        }

        // mass
        if (from.equals("kg") && to.equals("g")) {
            return qty * 1000;
        }
        if (from.equals("g") && to.equals("kg")) {
            return qty / 1000.0;
        }

        // volume
        if (from.equals("l") && to.equals("ml")) {
            return qty * 1000;
        }
        if (from.equals("ml") && to.equals("l")) {
            return qty / 1000.0;
        }

        return qty; // fallback
    }

    private boolean isSameUnitType(String u1, String u2) {
        String type1 = getUnitType(u1);
        String type2 = getUnitType(u2);
        return type1.equals(type2);
    }

    private String getUnitType(String unit) {
        unit = unit.toLowerCase();

        switch (unit) {
            case "kg":
            case "g":
                return "mass"; // khối lượng

            case "l":
            case "ml":
                return "volume"; // thể tích

            case "pcs":
                return "count"; // đếm số lượng

            default:
                return "unknown";
        }
    }

    public RecipeItem getOtherItemByMenuItemAndIngredient(int menuItemId, int ingredientId, int excludeRecipeItemId) {
        try {
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.ingredient_id, r.quantity, r.unit, r.note, r.status, ig.ingredient_name "
                    + "FROM recipe r LEFT JOIN ingredient ig ON r.ingredient_id = ig.ingredient_id "
                    + "WHERE r.menu_item_id = ? AND r.ingredient_id = ? AND r.recipe_item_id <> ? AND (LOWER(r.status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{menuItemId, ingredientId, excludeRecipeItemId});
            if (rs.next()) {
                int id = rs.getInt("recipe_item_id");
                int mId = rs.getInt("menu_item_id");
                int ingId = rs.getInt("ingredient_id");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                String note = rs.getString("note");
                String status = rs.getString("status");
                String ingName = rs.getString("ingredient_name");

                return new RecipeItem(id, mId, ingId, qty, unit, note, status, ingName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
