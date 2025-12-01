/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import model.Supplier;

/**
 *
 * @author TruongBinhTrong
 */
public class SupplierDAO extends DBContext {

    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();

        try {
            String query = "SELECT *"
                    + "FROM supplier AS s\n"
                    + "WHERE LOWER(s.status) != LOWER(N'Deleted')\n"
                    + "ORDER BY s.supplier_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"), 
                        rs.getString("supplier_name"),
                        rs.getString("phone_number"), 
                        rs.getString("email"), 
                        rs.getString("address"), 
                        rs.getString("contact_person"), 
                        rs.getString("status")
                );

                list.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
    
        public List<Supplier> getAll(int page, String keyword) {
        List<Supplier> list = new ArrayList<>();

        try {
            String query = "SELECT *\n"
                    + "FROM     supplier\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "AND (LOWER(supplier_name) LIKE LOWER(?))\n"
                    + "ORDER BY supplier_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"), 
                        rs.getString("supplier_name"),
                        rs.getString("phone_number"), 
                        rs.getString("email"), 
                        rs.getString("address"), 
                        rs.getString("contact_person"), 
                        rs.getString("status")
                );
                list.add(supplier);
            }
        } catch (SQLException ex) {
        }

        return list;
    }

    public List<Supplier> getAll(int page) {
        List<Supplier> list = new ArrayList<>();

        try {
            String query = "SELECT *\n"
                    + "FROM     supplier\n"
                    + "WHERE  (LOWER(status) != LOWER(N'Deleted'))\n"
                    + "ORDER BY supplier_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"), 
                        rs.getString("supplier_name"),
                        rs.getString("phone_number"), 
                        rs.getString("email"), 
                        rs.getString("address"), 
                        rs.getString("contact_person"), 
                        rs.getString("status")
                );

                list.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Supplier getElementByID(int id) {

        try {
            String query = "SELECT *\n"
                    + "FROM     supplier\n"
                    + "WHERE  (supplier_id = ? and LOWER(status) != LOWER(N'Deleted'))\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {

                Supplier supplier = new Supplier(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getString(4), 
                        rs.getString(5), 
                        rs.getString(6), 
                        rs.getString(7)
                );

                return supplier;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int add(String supplier_name, String phone_number, String email, String address, String contact_person) {
        try {
            String query = "INSERT INTO supplier (supplier_name, phone_number, email, address, contact_person, status)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{supplier_name, phone_number, email, address, contact_person, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int supplier_id, String supplier_name, String phone_number, String email, String address, String contact_person, String status) {
        try {

            String query = "UPDATE supplier\n"
                    + "SET supplier_name = ?, phone_number = ?, email = ?, address = ?, contact_person = ?, status = ?\n"
                    + "WHERE  (supplier_id = ?)";

            return this.executeQuery(query, new Object[]{supplier_name, phone_number, email, address, contact_person, status, supplier_id});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE supplier\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (supplier_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(supplier_id) AS numrow\n"
                    + "FROM supplier\n"
                    + "WHERE LOWER(status) <> LOWER(N'Deleted')";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }
}
