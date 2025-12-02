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

/**
 *
 * @author PHAT
 */
public class RecipeDAO extends DBContext {

    public List<Recipe> getAll() {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.recipe_name, r.note, r.status, mi.item_name "
                    + "FROM recipe r LEFT JOIN menu_item mi ON r.menu_item_id = mi.menu_item_id "
                    + "WHERE (LOWER(r.status) <> LOWER(N'Deleted')) "
                    + "ORDER BY recipe_item_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                Recipe r = new Recipe(
                        rs.getInt("recipe_item_id"),
                        rs.getInt("menu_item_id"),
                        rs.getString("item_name"),
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getString("note"),
                        rs.getString("status")
                );
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
            String query = "SELECT r.recipe_item_id, r.menu_item_id, r.recipe_name, r.note, r.status, mi.item_name "
                    + "FROM recipe r LEFT JOIN menu_item mi ON r.menu_item_id = mi.menu_item_id "
                    + "WHERE (LOWER(r.status) <> LOWER(N'Deleted')) "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                Recipe r = new Recipe(
                        rs.getInt("recipe_item_id"),
                        rs.getInt("menu_item_id"),
                        rs.getString("item_name"),
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getString("note"),
                        rs.getString("status")
                );
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    /**
     * Paginated search across recipe_item_id, menu item name, ingredient name
     */
    public List<Recipe> getAll(int page, String keyword) {
        List<Recipe> list = new ArrayList<>();
        try {
            String base = "SELECT r.recipe_item_id, r.menu_item_id, mi.item_name, r.ingredient_id, ing.ingredient_name, r.quantity, r.unit, r.note, r.status "
                    + "FROM recipe r "
                    + "LEFT JOIN menu_item mi ON r.menu_item_id = mi.menu_item_id "
                    + "LEFT JOIN ingredient ing ON r.ingredient_id = ing.ingredient_id "
                    + "WHERE LOWER(r.status) <> LOWER(N'Deleted') ";
            String orderPg = " ORDER BY r.recipe_item_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.toLowerCase() + "%";
                String q = base + "AND (CAST(r.recipe_item_id AS VARCHAR) LIKE ? OR LOWER(mi.item_name) LIKE ? OR LOWER(ing.ingredient_name) LIKE ?)" + orderPg;
                rs = this.executeSelectionQuery(q, new Object[]{kw, kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            } else {
                String q = base + orderPg;
                rs = this.executeSelectionQuery(q, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            }
            while (rs.next()) {
                Recipe r = new Recipe(
                        rs.getInt("recipe_item_id"),
                        rs.getInt("menu_item_id"),
                        rs.getString("item_name"),
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getString("note"),
                        rs.getString("status")
                );
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Get a single recipe row by recipe_item_id
     */
    public Recipe getElementByID(int id) {
        try {
            String query = "SELECT r.recipe_item_id, r.menu_item_id, mi.item_name, r.ingredient_id, ing.ingredient_name, r.quantity, r.unit, r.note, r.status "
                    + "FROM recipe r "
                    + "LEFT JOIN menu_item mi ON r.menu_item_id = mi.menu_item_id "
                    + "LEFT JOIN ingredient ing ON r.ingredient_id = ing.ingredient_id "
                    + "WHERE r.recipe_item_id = ? AND LOWER(r.status) <> LOWER(N'Deleted')";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            if (rs.next()) {
                return new Recipe(
                        rs.getInt("recipe_item_id"),
                        rs.getInt("menu_item_id"),
                        rs.getString("item_name"),
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getString("note"),
                        rs.getString("status")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Insert a recipe item row. Returns affected rows or SQL error code handled
     * by checkErrorSQL.
     */
    public int addItem(int menuItemId, int ingredientId, double quantity, String unit, String note) {
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

    /**
     * Edit a recipe item row.
     */
    public int editItem(int recipeItemId, int ingredientId, double quantity, String unit, String note, String status) {
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

    /**
     * Soft-delete a recipe item (status = 'Deleted')
     */
    public int deleteItem(int recipeItemId) {
        try {
            String query = "UPDATE recipe SET status = 'Deleted' WHERE recipe_item_id = ?";
            return this.executeQuery(query, new Object[]{recipeItemId});
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Count active recipe rows (for pagination)
     */
    public int countItem() {
        try {
            String query = "SELECT COUNT(recipe_item_id) AS numrow FROM recipe WHERE (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // helper: check if menu_item exists / is deleted
    public boolean isMenuItemDeleted(int menuItemId) {
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
}