/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.Constants.*;
import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Voucher;

/**
 *
 * @author PHAT
 */
public class VoucherDAO extends DBContext {

    public List<Voucher> getAllAvailable() {
        List<Voucher> list = new ArrayList<>();
        try {
            String query = "SELECT voucher_id, voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status "
                    + "FROM Voucher WHERE LOWER(status) <> 'deleted'\n"
                    + "AND CAST(GETDATE() AS DATE) BETWEEN start_date AND end_date\n"
                    + "AND quantity > 0\n"
                    + "ORDER BY voucher_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("voucher_code"),
                        rs.getString("voucher_name"),
                        rs.getString("discount_type"),
                        rs.getInt("discount_value"),
                        rs.getInt("quantity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                );
                list.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Voucher> getAll() {
        List<Voucher> list = new ArrayList<>();
        try {
            String query = "SELECT voucher_id, voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status "
                    + "FROM Voucher WHERE LOWER(status) <> 'deleted' ORDER BY voucher_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("voucher_code"),
                        rs.getString("voucher_name"),
                        rs.getString("discount_type"),
                        rs.getInt("discount_value"),
                        rs.getInt("quantity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                );
                list.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Voucher> getAll(int page) {
        List<Voucher> list = new ArrayList<>();
        try {
            String query = "SELECT v.voucher_id, v.voucher_code, v.voucher_name, v.discount_type, v.discount_value, "
                    + "v.quantity, v.start_date, v.end_date, v.status "
                    + "FROM voucher AS v "
                    + "WHERE LOWER(v.status) <> 'deleted' "
                    + "ORDER BY v.voucher_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("voucher_code"),
                        rs.getString("voucher_name"),
                        rs.getString("discount_type"),
                        rs.getInt("discount_value"),
                        rs.getInt("quantity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                );
                list.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Voucher> getAll(int page, String keyword) {
        List<Voucher> list = new ArrayList<>();
        try {
            String safeKeyword = keyword
                    .replace("\\", "\\\\") // escape dấu gạch chéo ngược
                    .replace("%", "\\%") // escape dấu %
                    .replace("_", "\\_");   // escape dấu _
            String query = "SELECT v.voucher_id, v.voucher_code, v.voucher_name, v.discount_type, v.discount_value, "
                    + "v.quantity, v.start_date, v.end_date, v.status "
                    + "FROM voucher AS v "
                    + "WHERE LOWER(v.status) <> 'deleted' "
                    + "AND (LOWER(v.voucher_code) LIKE LOWER(?) OR "
                    + "LOWER(v.voucher_name) LIKE LOWER(?) OR "
                    + "LOWER(v.discount_type) LIKE LOWER(?) OR "
                    + "CAST(v.discount_value AS VARCHAR) LIKE LOWER(?) OR "
                    + "CAST(v.quantity AS VARCHAR) LIKE LOWER(?) OR "
                    + "LOWER(v.status) LIKE LOWER(?)) "
                    + "ORDER BY v.voucher_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + safeKeyword + "%";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{
                keyword, keyword, keyword, keyword, keyword, keyword,
                (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE
            });

            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("voucher_code"),
                        rs.getString("voucher_name"),
                        rs.getString("discount_type"),
                        rs.getInt("discount_value"),
                        rs.getInt("quantity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                );
                list.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int add(String code, String name, String type, int value, int qty, Date start, Date end, String status) {
        try {
            String query = "INSERT INTO Voucher (voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            return this.executeQuery(query, new Object[]{code, name, type, value, qty, start, end, status});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public int edit(int id, String code, String name, String type, int value, int qty, Date start, Date end, String status) {
        try {
            String query = "UPDATE Voucher SET voucher_code=?, voucher_name=?, discount_type=?, discount_value=?, quantity=?, start_date=?, end_date=?, status=? "
                    + "WHERE voucher_id=?";
            return this.executeQuery(query, new Object[]{code, name, type, value, qty, start, end, status, id});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE Voucher SET status='Deleted' WHERE voucher_id=?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public Voucher getById(int id) {
        try {
            String query = "SELECT voucher_id, voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status "
                    + "FROM Voucher WHERE voucher_id = ? AND LOWER(status) <> 'deleted'";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("voucher_code"),
                        rs.getString("voucher_name"),
                        rs.getString("discount_type"),
                        rs.getInt("discount_value"),
                        rs.getInt("quantity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(v.voucher_id) AS numrow "
                    + "FROM voucher AS v "
                    + "WHERE LOWER(v.status) <> 'deleted'";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error counting vouchers");
        }
        return 0;
    }

    public int decrease1Quantity(int id) {
        try {
            String query = "UPDATE Voucher\n"
                    + "SET quantity = quantity - 1\n"
                    + "WHERE voucher_id = ?\n"
                    + "  AND quantity > 0;";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
