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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.Reservation;
import model.Table;

/**
 * @author You
 */
public class ReservationDAO extends DBContext {

    public Reservation getElementByTableId(int id) {
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, r.reservation_date, r.reservation_time, r.status "
                    + "FROM reservation AS r INNER JOIN [table] AS t ON r.table_id = t.table_id "
                    + "WHERE t.table_id = ? AND LOWER(r.status) = LOWER('Approved') "
                    + "ORDER BY r.reservation_id DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{id});
            if (rs.next()) {
                return extract(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Reservation> getAllSeated() {
        List<Reservation> list = new ArrayList<>();
        try {
            String sql = "SELECT reservation_id, customer_id, table_id, reservation_date, reservation_time, status "
                    + "FROM reservation WHERE LOWER(status) = LOWER('Seated') "
                    + "ORDER BY reservation_id DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /* ===================== LIST (ADMIN) ===================== */
    public List<Reservation> getAll(int page, String keyword) {
        List<Reservation> list = new ArrayList<>();
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";

        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.status "
                    + "FROM reservation AS r "
                    + "WHERE (CAST(r.reservation_id AS VARCHAR) LIKE ? OR "
                    + "CAST(r.customer_id AS VARCHAR) LIKE ? OR "
                    + "CAST(r.table_id AS VARCHAR) LIKE ? OR "
                    + "LOWER(r.status) LIKE LOWER(?)) "
                    + "ORDER BY r.reservation_id DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            ResultSet rs = this.executeSelectionQuery(sql,
                    new Object[]{kw, kw, kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countItem(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT COUNT(*) FROM reservation AS r "
                    + "WHERE (CAST(r.reservation_id AS VARCHAR) LIKE ? OR "
                    + "CAST(r.customer_id AS VARCHAR) LIKE ? OR "
                    + "CAST(r.table_id AS VARCHAR) LIKE ? OR "
                    + "LOWER(r.status) LIKE LOWER(?))";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{kw, kw, kw, kw});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Reservation> getAllByCustomerId(int customerId) {
        List<Reservation> list = new ArrayList<>();

        try {
            String sql = "SELECT reservation_id, customer_id, table_id, reservation_date, reservation_time, status\n"
                    + "FROM     reservation\n"
                    + "WHERE  (customer_id = ?) AND (LOWER(status) = 'pending' OR\n"
                    + "                  LOWER(status) = 'approved')\n"
                    + "ORDER BY reservation_id DESC";
            ResultSet rs = this.executeSelectionQuery(sql,
                    new Object[]{customerId});
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /* ===================== LIST (CUSTOMER) ===================== */
    public List<Reservation> getByCustomer(int customerId, int page, String keyword) {
        List<Reservation> list = new ArrayList<>();
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.status "
                    + "FROM reservation AS r "
                    + "WHERE r.customer_id = ? "
                    + "AND (CAST(r.table_id AS VARCHAR) LIKE ? OR LOWER(r.status) LIKE LOWER(?)) "
                    + "ORDER BY r.reservation_id DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            ResultSet rs = this.executeSelectionQuery(sql,
                    new Object[]{customerId, kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countByCustomer(int customerId, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT COUNT(*) FROM reservation r "
                    + "WHERE r.customer_id = ? "
                    + "AND (CAST(r.table_id AS VARCHAR) LIKE ? OR LOWER(r.status) LIKE LOWER(?))";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{customerId, kw, kw});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /* ===================== CRUD / STATUS ===================== */
    public Reservation getElementByID(int id) {
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.status "
                    + "FROM reservation r WHERE r.reservation_id = ?";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{id});
            if (rs.next()) {
                return extract(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(int customerId, int tableId, Date date, Time time) {
        try {
            String sql = "INSERT INTO reservation (customer_id, table_id, reservation_date, reservation_time, status) "
                    + "VALUES (?, ?, ?, ?, ?)";
            return this.executeQuery(sql, new Object[]{customerId, tableId, date, time, "Pending"});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int reservationId, int tableId, Date date, Time time) {
        try {
            String sql = "UPDATE reservation "
                    + "SET table_id = ?, reservation_date = ?, reservation_time = ? "
                    + "WHERE reservation_id = ?";
            return this.executeQuery(sql, new Object[]{tableId, date, time, reservationId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int updateStatus(int id, String status) {
        try {
            String sql = "UPDATE reservation SET status = ? WHERE reservation_id = ?";
            return this.executeQuery(sql, new Object[]{status, id});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int cancelByCustomer(int id, int customerId) {
        try {
            String sql = "UPDATE reservation SET status = 'Cancelled' "
                    + "WHERE reservation_id = ? AND customer_id = ?";
            return this.executeQuery(sql, new Object[]{id, customerId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /* ===================== helper ===================== */
    private Reservation extract(ResultSet rs) throws SQLException {
        int id = rs.getInt("reservation_id");
        int customerId = rs.getInt("customer_id");
        int tableId = rs.getInt("table_id");
        Date date = rs.getDate("reservation_date");
        Time time = rs.getTime("reservation_time");
        String status = rs.getString("status");

        CustomerDAO customerDAO = new CustomerDAO();
        TableDAO tableDAO = new TableDAO();

        Customer customer = customerDAO.getElementByID(customerId);
        Table table = tableDAO.getElementByID(tableId);

        return new Reservation(id, customer, table, date, time, 0, status);
    }

    public boolean hasActiveReservationForTable(int tableId) {
        try {
            String sql = "SELECT COUNT(*) FROM reservation "
                    + "WHERE table_id = ? AND LOWER(status) IN ('approved', 'seated')";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId});
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Reservation> getReservationsByTable(int tableId) {
        List<Reservation> list = new ArrayList<>();
        try {
            String sql = "SELECT reservation_date, reservation_time FROM reservation "
                    + "WHERE table_id = ? AND status IN ('Approved')";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId});
            while (rs.next()) {
                list.add(new Reservation(0, null, null, rs.getDate("reservation_date"), rs.getTime("reservation_time"), 0, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Time[]> getStartEndTimesByTableAndDate(int tableId, Date date) {
        List<Time[]> list = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + "DATEADD(MINUTE, -15, reservation_time) AS start_time, "
                    + "DATEADD(HOUR, 3, reservation_time) AS end_time "
                    + "FROM reservation "
                    + "WHERE table_id = ? AND reservation_date = ? "
                    + "AND LOWER(status) IN ('approved') "
                    + "ORDER BY reservation_time";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId, date});
            while (rs.next()) {
                Time start = rs.getTime("start_time");
                Time end = rs.getTime("end_time");
                list.add(new Time[]{start, end});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
