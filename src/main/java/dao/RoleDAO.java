/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Role;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class RoleDAO extends DBContext {

    public static void main(String[] args) {
    }

    public List<Role> getAll() {
        List<Role> list = new ArrayList<>();

        try {
            String query = "SELECT role_id, role_name, description, status\n"
                    + "FROM     role\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "ORDER BY role_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {

                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Role role = new Role(id, name, description, status);

                list.add(role);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Role> getAll(int page, int maxElement) {
        List<Role> list = new ArrayList<>();

        try {
            String query = "SELECT role_id, role_name, description, status\n"
                    + "FROM     role\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "ORDER BY role_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Role role = new Role(id, name, description, status);

                list.add(role);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Role> getAll(int page, int maxElement, String keyword) {
        List<Role> list = new ArrayList<>();

        try {
            String query = "SELECT role_id, role_name, description, status\n"
                    + "FROM     role\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "AND (LOWER(role_name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))\n"
                    + "ORDER BY role_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, keyword, (page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Role role = new Role(id, name, description, status);

                list.add(role);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public Role getElementByID(int id) {

        try {
            String query = "SELECT role_id, role_name, description, status\n"
                    + "FROM     role\n"
                    + "WHERE  (role_id = ? and LOWER(status) <> LOWER(N'Deleted'))\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                String name = rs.getString(2);
                String description = rs.getString(3);
                String status = rs.getString(4);

                Role role = new Role(id, name, description, status);

                return role;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }

    public int add(String name, String description) {
        try {
            String query = "INSERT INTO role (role_name, description, status)\n"
                    + "VALUES (?, ?, ?)";

            return this.executeQuery(query, new Object[]{name, description, "Active"});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int id, String name, String description) {
        try {

            String query = "UPDATE role\n"
                    + "SET role_name = ?, description = ?\n"
                    + "WHERE  (role_id = ?)";

            return this.executeQuery(query, new Object[]{name, description, id});

        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE role\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (role_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {
            System.out.println("Can't not delete object");
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(role_id) AS numrow\n"
                    + "FROM     role\n"
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

    public boolean isRoleDeleted(int roleId) {
        try {
            String query = "SELECT status FROM Role WHERE role_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{roleId});
            if (rs.next()) {
                String status = rs.getString("status");
                return "Deleted".equalsIgnoreCase(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Không tìm thấy role hoặc query lỗi thì coi như không bị xóa
    }

    public List<Role> getAvailableRoles() {
        List<Role> list = new ArrayList<>();
        try {
            String query = "SELECT role_id, role_name FROM role WHERE LOWER(status) <> 'deleted' ORDER BY role_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                list.add(new Role(rs.getInt("role_id"), rs.getString("role_name"), "", "Active"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
