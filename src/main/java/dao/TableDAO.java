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
import model.Table;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class TableDAO extends DBContext {

    public List<Table> getAllOccupied() {
        List<Table> list = new ArrayList<>();
        try {
            String query = "SELECT table_id, table_number, table_capacity, status "
                    + "FROM [table] "
                    + "WHERE LOWER(status) = LOWER('Occupied') "
                    + "ORDER BY table_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int id = rs.getInt(1);
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);
                
                list.add(new Table(id, number, capacity, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    /**
     * Lấy toàn bộ bàn (không phân trang)
     */
    public List<Table> getAll() {
        List<Table> list = new ArrayList<>();
        try {
            String query = "SELECT table_id, table_number, table_capacity, status "
                    + "FROM [table] "
                    + "WHERE LOWER(status) <> 'deleted' "
                    + "ORDER BY table_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int id = rs.getInt(1);
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);
                list.add(new Table(id, number, capacity, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Lấy bàn theo trang
     */
    public List<Table> getAll(int page) {
        List<Table> list = new ArrayList<>();
        try {
            String query = "SELECT table_id, table_number, table_capacity, status "
                    + "FROM [table] "
                    + "WHERE LOWER(status) <> 'deleted' "
                    + "ORDER BY table_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{
                (page - 1) * MAX_ELEMENTS_PER_PAGE,
                MAX_ELEMENTS_PER_PAGE
            });
            while (rs.next()) {
                int id = rs.getInt(1);
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);
                list.add(new Table(id, number, capacity, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Lấy bàn theo trang + tìm kiếm (theo số bàn, sức chứa, trạng thái)
     */
    public List<Table> getAll(int page, String keyword) {
        List<Table> list = new ArrayList<>();
        try {
            String base = "SELECT table_id, table_number, table_capacity, status FROM [table] t "
                    + "WHERE LOWER(t.status) <> 'deleted'  ";
            String orderOffset = "ORDER BY table_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

            ResultSet rs;
            if (keyword == null || keyword.trim().isEmpty()) {
                rs = this.executeSelectionQuery(base + orderOffset, new Object[]{
                    (page - 1) * MAX_ELEMENTS_PER_PAGE,
                    MAX_ELEMENTS_PER_PAGE
                });
            } else {
                String query = base + "AND (LOWER(table_number) LIKE ? "
                        + "OR CAST(table_capacity AS NVARCHAR(50)) LIKE ? "
                        + "OR LOWER(status) LIKE ?) "
                        + orderOffset;
                String key = "%" + keyword.toLowerCase() + "%";
                rs = this.executeSelectionQuery(query, new Object[]{
                    key, key, key,
                    (page - 1) * MAX_ELEMENTS_PER_PAGE,
                    MAX_ELEMENTS_PER_PAGE
                });
            }

            while (rs.next()) {
                int id = rs.getInt(1);
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);
                list.add(new Table(id, number, capacity, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Table getElementByID(int id) {

        try {
            String query = "SELECT table_id, table_number, table_capacity, status\n"
                    + "FROM     [table]\n"
                    + "WHERE  (table_id = ? and  LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "ORDER BY table_id\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);

                Table item = new Table(id, number, capacity, status);

                return item;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int add(String number, int capacity) {
        try {
            String query = "INSERT INTO [table]\n"
                    + "                  (table_number, table_capacity, status)\n"
                    + "VALUES (?, ?, ?)";

            return this.executeQuery(query, new Object[]{number, capacity, "Available"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int id, String number, int capacity) {
        try {

            String query = "UPDATE [table]\n"
                    + "SET          table_number = ?, table_capacity = ?\n"
                    + "WHERE  (table_id = ?)";

            return this.executeQuery(query, new Object[]{number, capacity, id});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE [table]\n"
                    + "SET          status = 'Deleted'\n"
                    + "WHERE  (table_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(table_id) AS numrow\n"
                    + "FROM     [table]\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    /**
     * Lấy danh sách bàn available có capacity >= partySize Dùng để hiển thị
     * trong form đặt bàn của customer
     */
    public List<Table> getAvailableTables(int partySize) {
        List<Table> list = new ArrayList<>();
        try {
            String query = "SELECT table_id, table_number, table_capacity, status "
                    + "FROM [table] "
                    + "WHERE LOWER(status) = 'available' "
                    + "AND table_capacity >= ? "
                    + "ORDER BY table_capacity ASC, table_id ASC";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{partySize});
            while (rs.next()) {
                int id = rs.getInt(1);
                String number = rs.getString(2);
                int capacity = rs.getInt(3);
                String status = rs.getString(4);
                list.add(new Table(id, number, capacity, status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int updateStatus(int id, String status) {
        try {
            String sql = "UPDATE [table] SET status = ? WHERE table_id = ?";
            return this.executeQuery(sql, new Object[]{status, id});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(TableDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
