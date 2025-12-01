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
import model.Type;

/**
 *
 * @author TruongBinhTrong
 */
public class TypeDAO extends DBContext {

    public List<Type> getAll() {
        List<Type> list = new ArrayList<>();

        try {
            String query = "SELECT *"
                    + "FROM type AS t\n"
                    + "WHERE LOWER(t.status) != LOWER(N'Deleted')\n"
                    + "ORDER BY t.type_id";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                Type type = new Type(
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("description"),
                        rs.getString("status")
                );

                list.add(type);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Type> getAll(int page, String keyword) {
        List<Type> list = new ArrayList<>();

        try {
            String query = "SELECT *\n"
                    + "FROM     type\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
                    + "AND (LOWER(type_name) LIKE LOWER(?))\n"
                    + "ORDER BY type_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Type type = new Type(
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("description"),
                        rs.getString("status")
                );
                list.add(type);
            }
        } catch (SQLException ex) {
        }

        return list;
    }

    public List<Type> getAll(int page) {
        List<Type> list = new ArrayList<>();

        try {
            String query = "SELECT *\n"
                    + "FROM     type\n"
                    + "WHERE  (LOWER(status) != LOWER(N'Deleted'))\n"
                    + "ORDER BY type_id\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Type type = new Type(
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("description"),
                        rs.getString("status")
                );

                list.add(type);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public int getTypeByIngredient(String ingredientName) {
        try {
            String query = "SELECT t.type_id FROM type t JOIN ingredient i ON t.type_id = i.type_id "
                    + "WHERE i.ingredient_name =  ? AND (LOWER(t.status) != LOWER(N'Deleted'))";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{ingredientName});

            while (rs.next()) {
                int typeId = rs.getInt(1);
                return typeId;
            }
        } catch (SQLException ex) {
        }
        return -1;
    }

    public Type getElementByID(int id) {

        try {
            String query = "SELECT *\n"
                    + "FROM     type\n"
                    + "WHERE  (type_id = ? and LOWER(status) != LOWER(N'Deleted'))\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {

                Type type = new Type(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                );

                return type;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int add(String type_name, String description) {
        try {
            String query = "INSERT INTO type (type_name, description, status)\n"
                    + "VALUES (?, ?, ?)";

            return this.executeQuery(query, new Object[]{type_name, description, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(TypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int type_id, String type_name, String description) {
        try {

            String query = "UPDATE type\n"
                    + "SET type_name = ?, description = ?\n"
                    + "WHERE  (type_id = ?)";

            return this.executeQuery(query, new Object[]{type_name, description, type_id});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(TypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE type\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (type_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(type_id) AS numrow\n"
                    + "FROM type\n"
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
