/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    private Connection conn;
    private final String DB_URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=RestaurantManagement;encrypt=false";
    private final String DB_USER = "sa";
    private final String DB_PWD = "123456";

    public DBContext() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public ResultSet executeSelectionQuery(String query, Object[] params) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object p = params[i];

                if (p instanceof String) {
                    statement.setNString(i + 1, (String) p);
                } else {
                    statement.setObject(i + 1, p);
                }
            }
        }
        return statement.executeQuery();
    }

    public int executeQuery(String query, Object[] params) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);

//                System.out.println(params[i]);
            }
        }

        return statement.executeUpdate();
    }

    public String hashToMD5(String input) {
        try {
            // Sử dụng thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // Chuyển đổi byte array sang chuỗi Hex
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Xử lý ngoại lệ nếu thuật toán MD5 không khả dụng
            throw new RuntimeException("MD5 algorithm not found!", e);
        }
    }

}
