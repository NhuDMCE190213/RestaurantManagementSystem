/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Import;

/**
 *
 * @author TruongBinhTrong
 */
public class ImportDAO extends DBContext {

    public List<Import> getAll(int page, String keyword) {
        List<Import> list = new ArrayList<>();

        if (page <= 0) {
            page = 1;
        }

        if (keyword == null) {
            keyword = "";
        }
        keyword = keyword.trim();

        try {
            String query = "SELECT i.import_id, i.emp_id, e.emp_name, i.supplier_id, s.contact_person, s.supplier_name, i.import_date, i.status "
                    + "FROM import i "
                    + "JOIN employee e ON e.emp_id = i.emp_id "
                    + "JOIN supplier s ON s.supplier_id = i.supplier_id "
                    + "WHERE (i.status IS NULL OR LOWER(i.status) <> LOWER(N'Deleted')) "
                    + "AND (LOWER(e.emp_name) LIKE LOWER(?) OR LOWER(s.supplier_name) LIKE LOWER(?)) "
                    + "ORDER BY i.import_id DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            String searchKeyword = "%" + keyword + "%";

            Object[] params = new Object[]{
                searchKeyword,
                searchKeyword,
                (page - 1) * MAX_ELEMENTS_PER_PAGE,
                MAX_ELEMENTS_PER_PAGE
            };

            ResultSet rs = this.executeSelectionQuery(query, params);

            while (rs.next()) {
                Import imp = new Import(
                        rs.getInt("import_id"),
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_person"),
                        rs.getDate("import_date"),
                        rs.getString("status")
                );

                list.add(imp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Import> getImportDetails(int importId, int page, String keyword) {
        List<Import> list = new ArrayList<>();

        try {
            String query = "SELECT \n"
                    + "    igd.ingredient_name, \n"
                    + "    id.quantity, \n"
                    + "    igd.unit, \n"
                    + "    id.unit_price, \n"
                    + "    id.total_price\n"
                    + "FROM import i\n"
                    + "JOIN import_detail id ON i.import_id = id.import_id\n"
                    + "JOIN ingredient igd ON id.ingredient_id = igd.ingredient_id\n"
                    + "WHERE i.import_id = ?\n"
                    + "AND (LOWER(igd.ingredient_name) LIKE LOWER(?))\n"
                    + "ORDER BY import_detail_id\n"
                    + "OFFSET ? ROWS\n"
                    + "FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{importId, keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                String ingredientName = rs.getString(1);
                int quantity = rs.getInt(2);
                String unit = rs.getString(3);
                int unitPrice = rs.getInt(4);
                int totalPrice = rs.getInt(5);

                Import imp = new Import(ingredientName, quantity, unit, unitPrice, totalPrice);

                list.add(imp);
            }
        } catch (SQLException ex) {
        }

        return list;
    }

    public List<Import> getImportDetails(int importId) {
        List<Import> list = new ArrayList<>();

        try {
            String query = "SELECT \n"
                    + "    igd.ingredient_name, \n"
                    + "    id.quantity, \n"
                    + "    igd.unit, \n"
                    + "    id.unit_price, \n"
                    + "    id.total_price\n"
                    + "FROM import_detail id \n"
                    + "JOIN ingredient igd ON id.ingredient_id = igd.ingredient_id\n"
                    + "WHERE id.import_id = ?\n"
                    + "ORDER BY id.import_detail_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{importId});

            while (rs.next()) {
                Import imp = new Import(
                        rs.getString("ingredient_name"),
                        rs.getInt("quantity"),
                        rs.getString("unit"),
                        rs.getInt("unit_price"),
                        rs.getInt("total_price")
                );

                list.add(imp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Import getElementByID(int id) {

        try {
            String query = "SELECT i.import_id, i.emp_id, e.emp_name, i.supplier_id, s.supplier_name, s.contact_person, i.import_date, i.status "
                    + "FROM import i "
                    + "JOIN employee e ON e.emp_id = i.emp_id "
                    + "JOIN supplier s ON s.supplier_id = i.supplier_id  "
                    + "WHERE i.import_id = ? AND (i.status IS NULL OR LOWER(i.status) <> LOWER(N'Deleted'))";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            if (rs.next()) {
                return new Import(
                        rs.getInt("import_id"),
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_person"),
                        rs.getDate("import_date"),
                        rs.getString("status")
                );
            }
        } catch (SQLException ex) {
        }

        return null;
    }

    public int add(int supplierId, int empId) {
        try {
        String query = "INSERT INTO import (supplier_id, emp_id, import_date, status) VALUES (?, ?, ?, ?)";

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            return this.executeQuery(
                    query,
            new Object[]{supplierId, empId, now, "Pending"}
            );

        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
        }
        return -1;
    }

    public int addDetail(int importId, int ingredientId, int quantity, int unitPrice, int totalPrice) {
        try {
            String query = "INSERT INTO import_detail (import_id, ingredient_id, quantity, unit_price, total_price) "
                    + "VALUES (?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{importId, ingredientId, quantity, unitPrice, totalPrice});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
        }
        return -1;
    }

    public int edit(int importId, int supplierId, int employeeId, Date importDate) {
        try {
            String query = "UPDATE import SET supplier_id = ?, emp_id = ?, import_date = ? WHERE import_id = ?";

            return this.executeQuery(query, new Object[]{supplierId, employeeId, importDate, importId});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE import\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE (import_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(import_id) AS numrow FROM import WHERE (status IS NULL OR LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int markAsCompleted(int importId) {
        try {
            String query = "UPDATE import SET status = 'Completed', import_date = GETDATE() WHERE import_id = ?";
            return this.executeQuery(query, new Object[]{importId});
        } catch (SQLException ex) {
            System.out.println("Unable to mark import as completed");
        }
        return -1;
    }
}
