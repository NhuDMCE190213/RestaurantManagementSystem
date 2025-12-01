/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
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
public void sendEmail(String toEmail, String subject, String content) {

    String code = String.valueOf((int) (Math.random() * 900000) + 100000); 

    final String username = "phuonghtd.ce190603@gmail.com"; 
    final String password = "dxve inyz droz nuls"; 

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com"); 
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
        System.out.println("Sent reset code successfully to: " + toEmail);

    } catch (MessagingException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }}
}
