/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Ingredient;

/**
 *
 * @author TruongBinhTrong
 */
public class IngredientDAO extends DBContext {

    private final TypeDAO typeDAO = new TypeDAO();

    public List<Ingredient> getAll() {
        List<Ingredient> list = new ArrayList<>();

        try {
            String query = "SELECT ingredient_id, ingredient_name, quantity, unit, type_id, status\n"
                    + "FROM     ingredient\n"
                    + "where LOWER(status) <> 'deleted'\n";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                int ingredientId = rs.getInt(1);
                String ingredientName = rs.getString(2);
                int quantity = rs.getInt(3);
                String unit = rs.getString(4);
                int typeId = rs.getInt(5);
                String status = rs.getString(6);

                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, quantity, unit, typeDAO.getElementByID(typeId), status);

                list.add(ingredient);
            }
        } catch (SQLException ex) {
        }

        return list;
    }

    public List<Ingredient> getAll(int page, String keyword) {
        List<Ingredient> list = new ArrayList<>();

        try {
            String query
                    = "SELECT ingredient_id, ingredient_name, quantity, unit, type_id, status\n"
                    + "FROM     ingredient\n"
                    + "WHERE  (LOWER(status) <> 'deleted') AND (LOWER(ingredient_name) LIKE LOWER(?))\n"
                    + "ORDER BY ingredient_id\n"
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                int ingredientId = rs.getInt(1);
                String ingredientName = rs.getString(2);
                int quantity = rs.getInt(3);
                String unit = rs.getString(4);
                int typeId = rs.getInt(5);
                String status = rs.getString(6);

                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, quantity, unit, typeDAO.getElementByID(typeId), status);

                list.add(ingredient);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    // In IngredientDAO.java, replace the entire getElementByID method with this:
    public Ingredient getElementByID(int id) {

        try {
            String query = "SELECT ingredient_id, ingredient_name, quantity, unit, type_id, status\n"
                    + "FROM     ingredient\n"
                    + "WHERE  (LOWER(status) <> 'deleted') and ingredient_id = ?";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            if (rs.next()) {
                int ingredientId = rs.getInt(1);
                String ingredientName = rs.getString(2);
                int quantity = rs.getInt(3);
                String unit = rs.getString(4);
                int typeId = rs.getInt(5);
                String status = rs.getString(6);

                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, quantity, unit, typeDAO.getElementByID(typeId), status);

                return ingredient;

            }

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, "Can't load Ingredient object by ID", ex);
        }

        return null;
    }

//    public int getLastId() {
//
//        try {
//            String query = "SELECT TOP (1) i.ingredient_id FROM ingredient i ORDER BY i.ingredient_id DESC";
//
//            ResultSet rs = this.executeSelectionQuery(query, null);
//            if (rs.next()) {
//                return rs.getInt("ingredient_id");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return -1;
//    }
    public int add(String ingredientName, int quantity, String unit, int typeId) {

        try {
            String query = "INSERT INTO ingredient\n"
                    + "                  (ingredient_name, quantity, unit, type_id, status)\n"
                    + "VALUES (?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{ingredientName, quantity, unit, typeId, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int ingredientId, String ingredientName, int quantity, String unit, int typeId, String status) {
        try {

            String query = "UPDATE ingredient\n"
                    + "SET          ingredient_name = ?, quantity = ?, unit = ?, type_id = ?, status = ?\n"
                    + "WHERE  (ingredient_id = ?)";

            return this.executeQuery(query, new Object[]{ingredientName, quantity, unit, typeId, status, ingredientId});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE ingredient\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (ingredient_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "select count(ingredient_id) as numrow from [dbo].[ingredient] where LOWER(status) != LOWER(N'Deleted')";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

//    public List<Ingredient> search(String keyword) {
//        List<Ingredient> list = new ArrayList<>();
//        try {
//            String query = "SELECT i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.status, i.expiration_date "
//                    + "FROM ingredient AS i "
//                    + "LEFT JOIN type AS t ON i.type_id = t.type_id "
//                    + "WHERE LOWER(i.status) != LOWER(N'Deleted') "
//                    + "AND (CAST(i.ingredient_id AS VARCHAR) LIKE ? OR LOWER(i.ingredient_name) LIKE ?) "
//                    + "ORDER BY i.ingredient_id";
//
//            String searchKeyword = "%" + keyword.toLowerCase() + "%";
//            ResultSet rs = this.executeSelectionQuery(query, new Object[]{searchKeyword, searchKeyword});
//
//            while (rs.next()) {
//                Ingredient ing = new Ingredient(
//                        rs.getInt("ingredient_id"),
//                        rs.getString("ingredient_name"),
//                        rs.getString("unit"),
//                        rs.getInt("type_id"),
//                        rs.getString("type_name"),
//                        rs.getString("status")
//                );
//                Date expirationDate = rs.getDate("expiration_date");
//                if (expirationDate != null) {
//                    ing.setExpirationDate(expirationDate.toLocalDate());
//                }
//                applyExpirationStatus(ing);
//                list.add(ing);
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return list;
//    }

//    private void applyExpirationStatus(Ingredient ingredient) {
//        if (ingredient == null) {
//            return;
//        }
//
//        LocalDate expiration = ingredient.getExpirationDate();
//        if (expiration == null) {
//            ingredient.setExpired(false);
//            ingredient.setExpiringSoon(false);
//            return;
//        }
//
//        LocalDate today = LocalDate.now();
//        boolean isExpired = expiration.isBefore(today);
//        boolean isExpiringSoon = !isExpired && !expiration.isAfter(today.plusDays(3));
//
//        ingredient.setExpired(isExpired);
//        ingredient.setExpiringSoon(isExpiringSoon);
//    }

}
