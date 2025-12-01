/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    public List<Ingredient> getAll() {
        List<Ingredient> list = new ArrayList<>();

        try {
    String query = "SELECT\n"
        + "    i.ingredient_id,\n"
        + "    i.ingredient_name,\n"
        + "    t.type_name,\n"
        + "\ti.unit,\n"
        + "    COALESCE(SUM(CASE WHEN imp.status IS NULL OR LOWER(imp.status) IN (LOWER(N'Completed'), LOWER(N'Active')) THEN im.quantity ELSE 0 END), 0)\n"
        + "    - COALESCE(usage_data.total_used, 0) AS TotalQuantity,\n"
        + "    i.status,\n"
        + "    i.expiration_date\n"
        + "FROM ingredient i\n"
        + "JOIN type t \n"
        + "    ON i.type_id = t.type_id\n"
        + "LEFT JOIN (\n"
        + "    SELECT ingredient_id, SUM(quantity_used) AS total_used\n"
        + "    FROM ingredient_usage\n"
        + "    GROUP BY ingredient_id\n"
        + ") AS usage_data ON usage_data.ingredient_id = i.ingredient_id\n"
        + "LEFT JOIN import_detail im \n"
        + "    ON im.ingredient_id = i.ingredient_id\n"
        + "LEFT JOIN import imp ON imp.import_id = im.import_id\n"
        + "WHERE LOWER(i.status) != LOWER(N'Deleted')\n"
        + "GROUP BY\n"
        + "    i.ingredient_id,\n"
        + "    i.ingredient_name,\n"
        + "    t.type_name,\n"
        + "\ti.unit,\n"
        + "    usage_data.total_used,\n"
        + "    i.status,\n"
        + "    i.expiration_date\n"
        + "ORDER BY \n"
        + "    i.ingredient_id\n";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("type_name"),
                        rs.getString("unit"),
                        rs.getDouble("TotalQuantity"),
                        rs.getString("status")
                );

                Date expirationDate = rs.getDate("expiration_date");
                if (expirationDate != null) {
                    ing.setExpirationDate(expirationDate.toLocalDate());
                }
                applyExpirationStatus(ing);

                list.add(ing);
            }
        } catch (SQLException ex) {
        }

        return list;
    }

    public List<Ingredient> getAll(int page, String keyword) {
        List<Ingredient> list = new ArrayList<>();

        try {
    String query
        = "SELECT i.ingredient_id, i.ingredient_name, t.type_name, "
            + "i.unit, COALESCE(SUM(CASE WHEN imp.status IS NULL OR LOWER(imp.status) IN (LOWER(N'Completed'), LOWER(N'Active')) THEN im.quantity ELSE 0 END), 0)"
            + " - COALESCE(usage_data.total_used, 0) AS TotalQuantity, i.status, i.expiration_date "
        + "FROM ingredient i "
        + "JOIN type t ON i.type_id = t.type_id "
        + "LEFT JOIN ("
        + "    SELECT ingredient_id, SUM(quantity_used) AS total_used"
        + "    FROM ingredient_usage"
        + "    GROUP BY ingredient_id"
        + ") AS usage_data ON usage_data.ingredient_id = i.ingredient_id "
        + "LEFT JOIN import_detail im ON im.ingredient_id = i.ingredient_id "
        + "LEFT JOIN import imp ON imp.import_id = im.import_id "
        + "WHERE LOWER(i.status) != LOWER('Deleted') "
            + "  AND LOWER(i.ingredient_name) LIKE LOWER(?) "
            + "GROUP BY i.ingredient_id, i.ingredient_name, t.type_name, i.unit, usage_data.total_used, i.status, i.expiration_date "
            + "ORDER BY i.ingredient_id "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("type_name"),
                        rs.getString("unit"),
                        rs.getDouble("TotalQuantity"),
                        rs.getString("status")
                );

                Date expirationDate = rs.getDate("expiration_date");
                if (expirationDate != null) {
                    ing.setExpirationDate(expirationDate.toLocalDate());
                }
                applyExpirationStatus(ing);

                list.add(ing);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

  // In IngredientDAO.java, replace the entire getElementByID method with this:

public Ingredient getElementByID(int id) {

    try {
        String query = "SELECT i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.status, i.expiration_date,\n"
            + "       COALESCE(SUM(CASE WHEN imp.status IS NULL OR LOWER(imp.status) IN (LOWER(N'Completed'), LOWER(N'Active')) THEN im.quantity ELSE 0 END), 0)\n"
            + "       - COALESCE(usage_data.total_used, 0) AS total_quantity\n"
                + "FROM ingredient AS i\n"
                + "LEFT JOIN type AS t ON i.type_id = t.type_id\n"
                + "LEFT JOIN import_detail im ON im.ingredient_id = i.ingredient_id\n"
                + "LEFT JOIN import imp ON imp.import_id = im.import_id\n"
                + "LEFT JOIN (\n"
                + "    SELECT ingredient_id, SUM(quantity_used) AS total_used\n"
                + "    FROM ingredient_usage\n"
                + "    GROUP BY ingredient_id\n"
                + ") AS usage_data ON usage_data.ingredient_id = i.ingredient_id\n"
                + "WHERE (i.ingredient_id = ?) AND (LOWER(i.status) <> LOWER('Deleted'))\n"
                + "GROUP BY i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.status, usage_data.total_used, i.expiration_date";

        ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

        if (rs.next()) {

            int ingredientId = rs.getInt("ingredient_id");
            String ingredientName = rs.getString("ingredient_name");
            String unit = rs.getString("unit");
            int typeId = rs.getInt("type_id");
            String typeName = rs.getString("type_name");
            String status = rs.getString("status");
            double totalQuantity = rs.getDouble("total_quantity");

            Ingredient ing = new Ingredient(
                    ingredientId,
                    ingredientName,
                    unit,
                    typeId,
                    typeName,
                    status
            );
            ing.setTotalQuantity(totalQuantity);

            Date expirationDate = rs.getDate("expiration_date");
            if (expirationDate != null) {
                ing.setExpirationDate(expirationDate.toLocalDate());
            }
            applyExpirationStatus(ing);

            return ing;

        }

    } catch (SQLException ex) {
        Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, "Can't load Ingredient object by ID", ex);
    }

    return null;
}

    public int getLastId() {

        try {
            String query = "SELECT TOP (1) i.ingredient_id FROM ingredient i ORDER BY i.ingredient_id DESC";

            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt("ingredient_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

        public int add(String ingredient_name, int type_id, String unit, LocalDate expirationDate) {

        try {
            String query = "INSERT INTO ingredient (ingredient_name, type_id, unit, expiration_date, status)\n"
                + "VALUES (?, ?, ?, ?, ?)";

            Date expirationSqlDate = expirationDate != null ? Date.valueOf(expirationDate) : null;

            return this.executeQuery(query, new Object[]{ingredient_name, type_id, unit, expirationSqlDate, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

        public int edit(int ingredient_id, String ingredient_name, int type_id, String unit, LocalDate expirationDate){
        try {

            String query = "UPDATE ingredient\n"
                + "SET ingredient_name = ?, type_id = ?, unit = ?, expiration_date = ?\n"
                + "WHERE  (ingredient_id = ?)";

            Date expirationSqlDate = expirationDate != null ? Date.valueOf(expirationDate) : null;

            return this.executeQuery(query, new Object[]{ingredient_name, type_id, unit, expirationSqlDate, ingredient_id});

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

    public List<Ingredient> search(String keyword) {
        List<Ingredient> list = new ArrayList<>();
        try {
            String query = "SELECT i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.status, i.expiration_date "
                    + "FROM ingredient AS i "
                    + "LEFT JOIN type AS t ON i.type_id = t.type_id "
                    + "WHERE LOWER(i.status) != LOWER(N'Deleted') "
                    + "AND (CAST(i.ingredient_id AS VARCHAR) LIKE ? OR LOWER(i.ingredient_name) LIKE ?) "
                    + "ORDER BY i.ingredient_id";

            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{searchKeyword, searchKeyword});

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("unit"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("status")
                );
                Date expirationDate = rs.getDate("expiration_date");
                if (expirationDate != null) {
                    ing.setExpirationDate(expirationDate.toLocalDate());
                }
                applyExpirationStatus(ing);
                list.add(ing);
            }

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void applyExpirationStatus(Ingredient ingredient) {
        if (ingredient == null) {
            return;
        }

        LocalDate expiration = ingredient.getExpirationDate();
        if (expiration == null) {
            ingredient.setExpired(false);
            ingredient.setExpiringSoon(false);
            return;
        }

        LocalDate today = LocalDate.now();
        boolean isExpired = expiration.isBefore(today);
        boolean isExpiringSoon = !isExpired && !expiration.isAfter(today.plusDays(3));

        ingredient.setExpired(isExpired);
        ingredient.setExpiringSoon(isExpiringSoon);
    }

}
