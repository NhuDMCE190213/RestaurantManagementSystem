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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;

/**
 *
 * @author Administrator
 */
public class CustomerDAO extends DBContext {

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' "
                    + "ORDER BY c.customer_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int customerId = rs.getInt(1);
                String customerAccount = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String email = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                Customer c = new Customer(customerId, customerAccount, password, customerName, gender, phoneNumber, email, address, dob, status);
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Customer> getAll(int page) {
        List<Customer> list = new ArrayList<>();
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' "
                    + "ORDER BY c.customer_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                int customerId = rs.getInt(1);
                String customerAccount = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String email = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                Customer c = new Customer(customerId, customerAccount, password, customerName, gender, phoneNumber, email, address, dob, status);
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Customer> getAll(int page, String keyword) {
        List<Customer> list = new ArrayList<>();
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' "
                    + "AND (LOWER(c.customer_account) LIKE LOWER(?) OR "
                    + "LOWER(c.customer_name) LIKE LOWER(?) OR "
                    + "LOWER(c.phone_number) LIKE LOWER(?) OR "
                    + "LOWER(c.email) LIKE LOWER(?) OR "
                    + "LOWER(c.address) LIKE LOWER(?) OR "
                    + "LOWER(c.status) LIKE LOWER(?)) "
                    + "ORDER BY c.customer_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query,
                    new Object[]{keyword, keyword, keyword, keyword, keyword, keyword,
                        (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                int customerId = rs.getInt(1);
                String customerAccount = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String email = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                Customer c = new Customer(customerId, customerAccount, password, customerName, gender, phoneNumber, email, address, dob, status);
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Customer getElementByID(int id) {
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND c.customer_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            while (rs.next()) {
                int customerId = rs.getInt(1);
                String customerAccount = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String email = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                return new Customer(customerId, customerAccount, password, customerName, gender, phoneNumber, email, address, dob, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(String customerAccount, String password, String customerName, String gender, String phoneNumber,
            String email, String address, Date dob) {
        try {
            String query = "INSERT INTO customer "
                    + "(customer_account, password, customer_name, gender, phone_number, email, address, dob, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return this.executeQuery(query, new Object[]{customerAccount, password, customerName, gender, phoneNumber, email, address, dob, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int add(String customerAccount, String password, String customerName) {
        try {
            String query = "INSERT INTO customer "
                    + "(customer_account, password, customer_name, status) "
                    + "VALUES (?, ?, ?, ?)";
            return this.executeQuery(query, new Object[]{customerAccount, password, customerName, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int customerId, String customerAccount, String password, String customerName,
            String gender, String phoneNumber, String email, String address, Date dob) {
        try {
            String query = "UPDATE customer SET customer_account = ?, password = ?, customer_name = ?, "
                    + "gender = ?, phone_number = ?, email = ?, address = ?, dob = ?, status = ? "
                    + "WHERE customer_id = ?";
            return this.executeQuery(query,
                    new Object[]{customerAccount, password, customerName, gender, phoneNumber, email, address, dob, "Active", customerId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int customerId, String customerAccount, String customerName, String gender, String phoneNumber,
            String email, String address, Date dob) {
        try {
            String query = "UPDATE customer SET customer_account = ?, customer_name = ?, "
                    + "gender = ?, phone_number = ?, email = ?, address = ?, dob = ?, status = ? "
                    + "WHERE customer_id = ?";
            return this.executeQuery(query,
                    new Object[]{customerAccount, customerName, gender, phoneNumber, email, address, dob, "Active", customerId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int customerId, String password) {
        try {
            String query = "UPDATE customer SET password = ? WHERE customer_id = ?";
            return this.executeQuery(query, new Object[]{password, customerId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE customer SET status = 'Deleted' WHERE customer_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(c.customer_id) AS numrow "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted'";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return 0;
    }

    public int updateStatus(int id) {
        try {
            String query = "UPDATE customer "
                    + "SET status = CASE "
                    + "WHEN status = 'Active' THEN 'Banned' "
                    + "WHEN status = 'Banned' THEN 'Active' "
                    + "ELSE status END "
                    + "WHERE customer_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public boolean checkAccountExist(String customerAccount) {
        try {
            String query = "SELECT c.customer_id FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND c.customer_account = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{customerAccount});

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean checkEmailExist(String email) {
        try {

            String query = "SELECT c.customer_id FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND c.email = ?";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{email});

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Error checking email existence", ex);
        }
        return false;
    }

    public boolean checkPhoneExist(String phoneNumber) {
        try {
            String query = "SELECT c.customer_id FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND c.phone_number = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{phoneNumber});
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Error checking phone number existence", ex);
        }
        return false;
    }

    public Customer authenticate(String customerAccount, String hashedPassword) {
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE c.customer_account = ? AND c.password = ? AND LOWER(c.status) <> 'deleted'";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{customerAccount, hashedPassword});

            if (rs.next()) {
                int customerId = rs.getInt(1);
                String account = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String email = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                return new Customer(customerId, account, password, customerName, gender, phoneNumber, email, address, dob, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //FORGET PASSWORD
    public Customer getElementByEmail(String email) {
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND c.email = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{email});
            if (rs.next()) {
                int customerId = rs.getInt(1);
                String customerAccount = rs.getString(2);
                String password = rs.getString(3);
                String customerName = rs.getString(4);
                String gender = rs.getString(5);
                String phoneNumber = rs.getString(6);
                String foundEmail = rs.getString(7);
                String address = rs.getString(8);
                Date dob = rs.getDate(9);
                String status = rs.getString(10);

                return new Customer(customerId, customerAccount, password, customerName, gender, phoneNumber, foundEmail, address, dob, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int updatePassword(int customerId, String newHashedPassword) {
        try {
            String query = "UPDATE customer SET password = ? WHERE customer_id = ?";
            return this.executeQuery(query, new Object[]{newHashedPassword, customerId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public Customer getByName(String name) {
        try {
            String query = "SELECT c.customer_id, c.customer_account, c.password, c.customer_name, "
                    + "c.gender, c.phone_number, c.email, c.address, c.dob, c.status "
                    + "FROM customer AS c "
                    + "WHERE LOWER(c.status) <> 'deleted' AND LOWER(c.customer_name) = LOWER(?)";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{name});

            if (rs.next()) {
                return new Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getDate(9),
                        rs.getString(10)
                );
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int addCustomerNameOnly(String customerName) {
        try {
            String query = "INSERT INTO customer (customer_name, status) VALUES (?, ?)";
            return this.executeQuery(query, new Object[]{customerName, "Active"});

        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int addQuickCustomer(String customerName, String phoneNumber, String hashedPassword) {
        try {
            String customerAccount = phoneNumber;       // dùng sđt làm account
            String password = hashedPassword;             // password tạm
            String status = "Active";
            String email = phoneNumber + "@guest.local"; // luôn khác nhau theo sđt

            String query = "INSERT INTO customer "
                    + "(customer_account, password, customer_name, gender, phone_number, email, address, dob, status) "
                    + "VALUES (?, ?, ?, NULL, ?, ?, NULL, NULL, ?)";

            return this.executeQuery(query, new Object[]{
                customerAccount,
                password,
                customerName,
                phoneNumber,
                email,
                status
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public Customer getByPhone(String phone) {
        try {
            String query = "SELECT * FROM customer WHERE phone_number = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{phone});

            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_account"),
                        rs.getString("password"),
                        rs.getString("customer_name"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getDate("dob"),
                        rs.getString("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
