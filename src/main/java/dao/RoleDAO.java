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
            String query = "SELECT role_id, role_name, description\n"
                    + "FROM     role\n"
                    + "ORDER BY role_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {

                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);

                Role role = new Role(id, name, description);

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
            String query = "SELECT role_id, role_name, description\n"
                    + "FROM     role\n"
                    + "ORDER BY role_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);

                Role role = new Role(id, name, description);

                list.add(role);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public Role getElementByID(int id) {

        try {
            String query = "SELECT role_id, role_name, description\n"
                    + "FROM     role\n"
                    + "WHERE role_id = ?\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                String name = rs.getString(2);
                String description = rs.getString(3);

                Role role = new Role(id, name, description);

                return role;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(role_id) AS numrow\n"
                    + "FROM     role";
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
