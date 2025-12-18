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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.Employee;
import model.Reservation;
import model.Table;
import model.Voucher;

/**
 * @author You
 */
public class ReservationDAO extends DBContext {

    public static final int ERR_PAST_TIME = -9999;

    public Reservation getElementByTableId(int id) {
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, emp_id, r.voucher_id, r.table_id, r.reservation_date, r.time_start, r.time_end, r.description, r.status "
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
            String sql = "SELECT reservation_id, customer_id, emp_id, r.voucher_id, table_id, reservation_date, r.time_start, r.time_end, r.description, status "
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
            String sql = "SELECT r.reservation_id, r.customer_id, emp_id, r.voucher_id, r.table_id, "
                    + "r.reservation_date, r.time_start, r.time_end, r.description, r.status "
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
            String sql = "SELECT reservation_id, customer_id, emp_id, r.voucher_id, table_id, reservation_date, time_start, time_end, r.description, status\n"
                    + "FROM     reservation\n"
                    + "WHERE  (customer_id = ?) AND (LOWER(status) = 'pending' OR\n"
                    + "LOWER(status) = 'approved')\n"
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
            String sql = "SELECT r.reservation_id, r.customer_id, emp_id, r.voucher_id, r.table_id, "
                    + "r.reservation_date, r.time_start, r.time_end, r.description, r.status "
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
            String sql = "SELECT r.reservation_id, r.customer_id, emp_id, r.voucher_id, r.table_id, "
                    + "r.reservation_date, r.time_start, r.time_end, r.description, r.status "
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

    public int add(int customerId, Integer voucherId, int tableId, Date date, Time time_start, Time time_end, String description) {
        if (checkDateRealTime(date, time_start) <= 0) {
            return ERR_PAST_TIME;
        }
        try {
            String sql = "INSERT INTO reservation (customer_id, voucher_id, table_id, reservation_date, time_start, time_end, description, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            return this.executeQuery(sql, new Object[]{
                customerId, voucherId, tableId, date, time_start, time_end, description, "Waiting_deposit"});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int reservationId, int tableId, Date date, Time time_start, Time time_end,
            Integer voucherId, String description) {
        try {
            String sql = "UPDATE reservation "
                    + "SET table_id = ?, reservation_date = ?, time_start = ?, time_end = ?, "
                    + "    voucher_id = ?, description = ? "
                    + "WHERE reservation_id = ?";

            return this.executeQuery(sql, new Object[]{
                tableId, date, time_start, time_end, voucherId, description, reservationId
            });
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

    public int cancelByCustomer(int reservationId, int customerId) {
        try {
            // lấy status hiện tại
            String sqlGet = "SELECT status FROM reservation WHERE reservation_id = ? AND customer_id = ?";
            ResultSet rs = this.executeSelectionQuery(sqlGet, new Object[]{reservationId, customerId});
            if (!rs.next()) {
                return 0; // không thấy reservation hoặc không đúng chủ
            }
            String cur = rs.getString("status");
            String cancelStatus;

            // Chưa cọc (waiting_deposit) hoặc pending => cancel_before_deposit
            if (cur != null && (cur.equalsIgnoreCase("Waiting_deposit") || cur.equalsIgnoreCase("Pending"))) {
                cancelStatus = "Cancelled_before_deposit";
            } else {
                cancelStatus = "Cancelled_after_deposit";
            }

            // update status
            String sqlUpd = "UPDATE reservation SET status = ? WHERE reservation_id = ? AND customer_id = ?";
            return this.executeQuery(sqlUpd, new Object[]{cancelStatus, reservationId, customerId});

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

        int employeeId = rs.getInt("emp_id");
        Integer voucherId = rs.getObject("voucher_id") != null ? rs.getInt("voucher_id") : null;

        int tableId = rs.getInt("table_id");

        Date date = rs.getDate("reservation_date");
        Time timeStart = rs.getTime("time_start");
        Time timeEnd = rs.getTime("time_end");
        String description = rs.getString("description");
        String status = rs.getString("status");

        Customer customer = new CustomerDAO().getElementByID(customerId);
        Table table = new TableDAO().getElementByID(tableId);

        Employee emp = null;
        if (employeeId != 0) {
            emp = new EmployeeDAO().getElementByID(employeeId);
        }

        Voucher voucher = null;
        if (voucherId != null) {
            voucher = new VoucherDAO().getById(voucherId);
        }

        Reservation r = new Reservation(
                id, customer, emp, voucher, table,
                date, timeStart, timeEnd,
                description, status
        );
        return r;
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
            String sql = "SELECT reservation_id, reservation_date, time_start, time_end, status "
                    + "FROM reservation "
                    + "WHERE table_id = ? AND LOWER(status) IN ('approved','serving') "
                    + "ORDER BY reservation_date DESC, time_start";

            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId});
            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("reservation_id"),
                        null, null, null, null,
                        rs.getDate("reservation_date"),
                        rs.getTime("time_start"),
                        rs.getTime("time_end"),
                        null,
                        rs.getString("status")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getStartEndTimesByTableAndDate(int tableId, Date date) {
        List<Object[]> list = new ArrayList<>();
        try {
            String sql = "SELECT reservation_date, "
                    + "CAST(DATEADD(MINUTE, -15, CAST(time_start AS datetime)) AS time) AS start_time, "
                    + "CAST(DATEADD(HOUR, 2,  CAST(time_end   AS datetime)) AS time) AS end_time "
                    + "FROM reservation "
                    + "WHERE table_id = ? AND reservation_date = ? "
                    + "AND LOWER(status) IN ('approved', 'serving') "
                    + "ORDER BY time_start";

            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId, date});
            while (rs.next()) {
                Date d = rs.getDate("reservation_date");
                Time start = rs.getTime("start_time");
                Time end = rs.getTime("end_time");
                list.add(new Object[]{d, start, end});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int addByEmployee(int empId, int customerId, int tableId,
            Date date, Time time_start, Time time_end, String description) {
        try {
            String sql = "INSERT INTO reservation "
                    + "(customer_id, emp_id, table_id, reservation_date, time_start, time_end, description, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            return this.executeQuery(sql, new Object[]{
                customerId,
                empId,
                tableId,
                date,
                time_start,
                time_end,
                description,
                "Waiting_deposit"
            });
        } catch (SQLException ex) {
            return checkErrorSQL(ex);
        }
    }

    public int addByCustomer(int customerId, int tableId,
            Date date, Time time_start, Time time_end, String description) {

        try {
            String sql = "INSERT INTO reservation "
                    + "(customer_id, table_id, reservation_date, time_start, time_end, description, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            return this.executeQuery(sql, new Object[]{
                customerId,
                tableId,
                date,
                time_start,
                time_end,
                description,
                "Pending"
            });
        } catch (SQLException ex) {
            return checkErrorSQL(ex);
        }
    }

    public int deposit(int reservationId, String status, int deposit) {
        try {
            String sql = "UPDATE reservation "
                    + "SET status = ?, deposit = ?\n"
                    + "WHERE reservation_id = ?";

            return this.executeQuery(sql, new Object[]{status, deposit, reservationId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int complete(int reservationId, String status) {
        try {
            String sql = "UPDATE reservation "
                    + "SET status = ?\n"
                    + "WHERE reservation_id = ?";
            return this.executeQuery(sql, new Object[]{status, reservationId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int getDeposit(int reservationId) {
        try {
            String sql = "SELECT deposit\n"
                    + "FROM reservation\n"
                    + "WHERE reservation_id = ? ";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{reservationId});
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int totalReservation() {
        try {
            String sql = "SELECT COUNT(*) AS Expr1\n"
                    + "FROM     reservation";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Map<String, Integer> getTableListOfUsed() {
        Map<String, Integer> tableMap = new HashMap<>();

        try {
            String sql = "SELECT Top(5) t.table_number, COUNT(r.table_id) AS used\n"
                    + "FROM     reservation AS r INNER JOIN\n"
                    + "                  [table] AS t ON r.table_id = t.table_id\n"
                    + "GROUP BY t.table_number\n"
                    + "ORDER BY used DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                tableMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableMap;
    }
    
    public Map<String, Integer> getTableListOfUsed(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return getTableListOfUsed();
        }
        
        Map<String, Integer> tableMap = new HashMap<>();

        try {
            String sql = "SELECT Top(5) t.table_number, COUNT(r.table_id) AS used\n"
                    + "FROM     reservation AS r INNER JOIN\n"
                    + "                  [table] AS t ON r.table_id = t.table_id\n"
                    + "WHERE r.reservation_date BETWEEN ? AND ?\n"
                    + "GROUP BY t.table_number\n"
                    + "ORDER BY used DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{startDate, endDate});
            while (rs.next()) {
                tableMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableMap;
    }

    public Map<String, Integer> getStatusList() {
        Map<String, Integer> statusMap = new HashMap<>();

        try {
            String sql = "SELECT status, COUNT(*) AS appeared\n"
                    + "FROM     reservation\n"
                    + "GROUP BY status\n"
                    + "ORDER BY appeared DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                statusMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return statusMap;
    }
    
    public Map<String, Integer> getStatusList(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return getStatusList();
        }
        
        Map<String, Integer> statusMap = new HashMap<>();

        try {
            String sql = "SELECT status, COUNT(*) AS appeared\n"
                    + "FROM     reservation\n"
                    + "WHERE reservation_date BETWEEN ? AND ?\n"
                    + "GROUP BY status\n"
                    + "ORDER BY appeared DESC";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{startDate, endDate});
            while (rs.next()) {
                statusMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return statusMap;
    }

    public Map<String, Integer> getMonthIncomeList() {
        Map<String, Integer> monthIncomeMap = new LinkedHashMap<>();

        try {
            String sql = "SELECT DATENAME(MONTH, r.reservation_date) AS month_name, sum(oi.unit_price * oi.quantity) as total, MONTH(r.reservation_date) AS month_number\n"
                    + "FROM     reservation AS r INNER JOIN\n"
                    + "                  order_item AS oi ON r.reservation_id = oi.reservation_id\n"
                    + "WHERE  (YEAR(r.reservation_date) = YEAR(GETDATE())) AND (LOWER(r.status) = LOWER('Completed')) AND (LOWER(oi.status) = LOWER('Completed'))\n"
                    + "group by DATENAME(MONTH, r.reservation_date), MONTH(r.reservation_date)\n"
                    + "order by month_number";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                monthIncomeMap.put(rs.getInt(3) + "-" + rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return monthIncomeMap;
    }

    public Map<String, Integer> getMonthIncomeList(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return getMonthIncomeList();
        }

        Map<String, Integer> monthIncomeMap = new LinkedHashMap<>();

        try {
            String sql = "SELECT DATENAME(MONTH, r.reservation_date) AS month_name, sum(oi.unit_price * oi.quantity) as total, MONTH(r.reservation_date) AS month_number\n"
                    + "FROM     reservation AS r INNER JOIN\n"
                    + "                  order_item AS oi ON r.reservation_id = oi.reservation_id\n"
                    + "WHERE  (r.reservation_date BETWEEN ? AND ?)\n"
                    + " AND (LOWER(r.status) = LOWER('Completed'))\n"
                    + " AND (LOWER(oi.status) = LOWER('Completed'))\n"
                    + "group by DATENAME(MONTH, r.reservation_date), MONTH(r.reservation_date)\n"
                    + "order by month_number";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{startDate, endDate});
            while (rs.next()) {
                monthIncomeMap.put(rs.getInt(3) + "-" + rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return monthIncomeMap;
    }

    public boolean isTimeSlotAvailable(int tableId, Date date, Time newStart, Time newEnd, Integer excludeReservationId) {
        try {
            String sql
                    = "SELECT COUNT(*) "
                    + "FROM reservation "
                    + "WHERE table_id = ? AND reservation_date = ? "
                    + "  AND LOWER(status) IN ('approved','serving') "
                    + "  AND (? IS NULL OR reservation_id <> ?) "
                    + "  AND ( "
                    + "       CAST(DATEADD(MINUTE, -15, CAST(time_start AS datetime)) AS time) < CAST(? AS time) "
                    + // blockStart < newEnd
                    "   AND CAST(DATEADD(HOUR,   2, CAST(time_end   AS datetime)) AS time) > CAST(? AS time) "
                    + // blockEnd   > newStart
                    "  )";

            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{
                tableId, date,
                excludeReservationId, excludeReservationId,
                newEnd, newStart
            });

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Time[]> getBlockedRangesByTableAndDate(int tableId, Date date) {
        List<Time[]> list = new ArrayList<>();
        try {
            String sql
                    = "SELECT "
                    + " CAST(DATEADD(MINUTE, -15, CAST(time_start AS datetime)) AS time) AS block_start, "
                    + " CAST(DATEADD(HOUR, 2,  CAST(time_end   AS datetime)) AS time) AS block_end "
                    + "FROM reservation "
                    + "WHERE table_id = ? AND reservation_date = ? "
                    + "  AND LOWER(status) IN ('approved','serving') "
                    + "ORDER BY time_start";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{tableId, date});
            while (rs.next()) {
                list.add(new Time[]{rs.getTime("block_start"), rs.getTime("block_end")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int checkDateRealTime(Date date, Time startTime) {
        try {
            String sql
                    = "SELECT CASE "
                    + " WHEN CAST(? AS datetime) + CAST(? AS datetime) < GETDATE() THEN -1 "
                    + " WHEN CAST(? AS datetime) + CAST(? AS datetime) = GETDATE() THEN 0 "
                    + " ELSE 1 "
                    + " END AS result";

            ResultSet rs = this.executeSelectionQuery(
                    sql,
                    new Object[]{date, startTime, date, startTime}
            );

            if (rs.next()) {
                return rs.getInt("result");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int beforeRealTime(Date date, Time time) {
        try {
            String sql = "SELECT CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME)";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                Date currentDate = rs.getDate(1);
                Time now = rs.getTime(2);

                if (currentDate.equals(date) && time.after(now)) {
                    return 1;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public Map<Integer, Time> getNearestTodayByTableId() {
        Map<Integer, Time> result = new HashMap<>();
        try {
            String sql = "SELECT table_id, MIN(time_start) AS Expr1\n"
                    + "FROM     reservation\n"
                    + "WHERE  (reservation_date = CAST(GETDATE() AS DATE)) AND (time_start > CAST(GETDATE() AS Time))\n"
                    + "GROUP BY table_id";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});
            while (rs.next()) {
                result.put(rs.getInt(1), rs.getTime(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
