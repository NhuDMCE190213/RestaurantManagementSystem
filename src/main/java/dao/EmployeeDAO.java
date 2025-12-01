/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author PHAT
 */
public class EmployeeDAO extends DBContext {

    private final RoleDAO roleDAO = new RoleDAO();

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        try {
            String query = "SELECT emp_id, emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status\n"
                    + "FROM     employee\n"
                    + "WHERE  (LOWER(status) <> 'deleted')\n"
                    + "ORDER BY emp_id\n";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int empId = rs.getInt(1);
                String empAccount = rs.getString(2);
                String password = rs.getString(3);
                String empName = rs.getString(4);
                String gender = rs.getString(5);
                Date dob = rs.getDate(6);
                String phoneNumber = rs.getString(7);
                String email = rs.getString(8);
                String address = rs.getString(9);
                int roleId = rs.getInt(10);
                String status = rs.getString(11);

                Employee emp = new Employee(empId, empAccount, password, empName, gender, dob, phoneNumber, email, address, roleDAO.getElementByID(roleId), status);

                list.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }
        return list;
    }

    public List<Employee> getAll(int page, int maxElement) {
        List<Employee> list = new ArrayList<>();
        try {
            String query = "SELECT emp_id, emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status\n"
                    + "FROM     employee\n"
                    + "WHERE  (LOWER(status) <> 'deleted')\n"
                    + "ORDER BY emp_id\n"
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});
            while (rs.next()) {
                int empId = rs.getInt(1);
                String empAccount = rs.getString(2);
                String password = rs.getString(3);
                String empName = rs.getString(4);
                String gender = rs.getString(5);
                Date dob = rs.getDate(6);
                String phoneNumber = rs.getString(7);
                String email = rs.getString(8);
                String address = rs.getString(9);
                int roleId = rs.getInt(10);
                String status = rs.getString(11);

                Employee emp = new Employee(empId, empAccount, password, empName, gender, dob, phoneNumber, email, address, roleDAO.getElementByID(roleId), status);

                list.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }
        return list;
    }

    public List<Employee> getAll(int page, int maxElement, String keyword) {
        List<Employee> list = new ArrayList<>();
        try {
            String query = "SELECT emp_id, emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status\n"
                    + "FROM     employee\n"
                    + "WHERE  (LOWER(status) <> 'deleted') AND (LOWER(emp_account) LIKE LOWER(?) OR\n"
                    + "                  LOWER(emp_name) LIKE LOWER(?) OR\n"
                    + "                  LOWER(gender) LIKE LOWER(?) OR\n"
                    + "                  LOWER(phone_number) LIKE LOWER(?) OR\n"
                    + "                  LOWER(email) LIKE LOWER(?) OR\n"
                    + "                  LOWER(address) LIKE LOWER(?))\n"
                    + "ORDER BY emp_id\n"
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, keyword, keyword, keyword, keyword, keyword, (page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int empId = rs.getInt(1);
                String empAccount = rs.getString(2);
                String password = rs.getString(3);
                String empName = rs.getString(4);
                String gender = rs.getString(5);
                Date dob = rs.getDate(6);
                String phoneNumber = rs.getString(7);
                String email = rs.getString(8);
                String address = rs.getString(9);
                int roleId = rs.getInt(10);
                String status = rs.getString(11);

                Employee emp = new Employee(empId, empAccount, password, empName, gender, dob, phoneNumber, email, address, roleDAO.getElementByID(roleId), status);

                list.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }
        return list;
    }

    public Employee getElementByID(int id) {
        try {
            String query = "SELECT emp_id, emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status\n"
                    + "FROM     employee\n"
                    + "WHERE  (LOWER(status) <> 'deleted') AND (emp_id = ?)";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            while (rs.next()) {
                int empId = rs.getInt(1);
                String empAccount = rs.getString(2);
                String password = rs.getString(3);
                String empName = rs.getString(4);
                String gender = rs.getString(5);
                Date dob = rs.getDate(6);
                String phoneNumber = rs.getString(7);
                String email = rs.getString(8);
                String address = rs.getString(9);
                int roleId = rs.getInt(10);
                String status = rs.getString(11);

                Employee emp = new Employee(empId, empAccount, password, empName, gender, dob, phoneNumber, email, address, roleDAO.getElementByID(roleId), status);

                return emp;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }
        return null;
    }

    public int add(String emp_account, String password, String emp_name, String gender, Date dob,
            String phone_number, String email, String address, int role_id) {
        try {
            String query = "INSERT INTO employee \n"
                    + "                  (emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            return this.executeQuery(query,
                    new Object[]{emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, "Active"});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int add(String emp_account, String password, String emp_name, int role_id) {
        try {
            String query = "INSERT INTO employee \n"
                    + "                  (emp_account, password, emp_name, role_id, status)\n"
                    + "VALUES (?, ?, ?, ?, ?)";

            return this.executeQuery(query,
                    new Object[]{emp_account, password, emp_name, role_id, "Active"});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int empId, String empAccount, String password, String empName, String gender, Date dob, String phoneNumber, String email, String address, int roleId) {
        try {

            String query = "UPDATE Employee SET emp_account = ?, password = ?, emp_name = ?, gender = ?, dob = ?, "
                    + "phone_number = ?, email = ?, address = ?, role_id = ?, status = ? \n"
                    + "WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{empAccount, password, empName, gender, dob, phoneNumber, email, address, roleId, "Active", empId});
        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int empId, String empAccount, String empName, String gender, Date dob, String phoneNumber, String email, String address) {
        try {

            String query = "UPDATE Employee SET emp_account = ?, emp_name = ?, gender = ?, dob = ?, "
                    + "phone_number = ?, email = ?, address = ?, status = ? \n"
                    + "WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{empAccount, empName, gender, dob, phoneNumber, email, address, "Active", empId});
        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int edit(int empId, String password) {
        try {

            String query = "UPDATE Employee SET password = ?\n"
                    + "WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{password, empId});
        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int edit(int empId, int roleId) {
        try {

            String query = "UPDATE Employee SET role_id = ?\n"
                    + "WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{roleId, empId});
        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int delete(int id) {
        
        if (id == 1) return -1;
        
        try {
            String query = "UPDATE Employee SET status = 'Deleted' WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Can't not delete object");
        }
        return -1;
    }

    public int ban(int id) {
        
        if (id == 1) return -1;
        
        try {
            String query = "UPDATE Employee SET status = 'Banned' WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Can't not ban object");
        }
        return -1;
    }

    public int unban(int id) {
        try {
            String query = "UPDATE Employee SET status = 'Active' WHERE emp_id = ?";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Can't not unban object");
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(e.emp_id) AS numrow\n"
                    + "FROM     employee AS e INNER JOIN\n"
                    + "                  role AS r ON e.role_id = r.role_id\n"
                    + "WHERE  (LOWER(e.status) <> 'deleted')";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return 0;
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

    public Employee authenticate(String empAccount, String hashedPassword) {
        try {

            String query = "SELECT emp_id, emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status\n"
                    + "FROM     employee\n"
                    + "WHERE  (emp_account = ?) AND (password = ?) AND (LOWER(status) = 'active')";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{empAccount, hashedPassword});

            if (rs.next()) {

                int empId = rs.getInt(1);
                String account = rs.getString(2);
                String password = rs.getString(3);
                String empName = rs.getString(4);
                String gender = rs.getString(5);
                Date dob = rs.getDate(6);
                String phoneNumber = rs.getString(7);
                String email = rs.getString(8);
                String address = rs.getString(9);
                int roleId = rs.getInt(10);
                String status = rs.getString(11);

                return new Employee(empId, account, password, empName, gender, dob, phoneNumber, email, address, roleDAO.getElementByID(roleId), status);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return null;
    }
}
