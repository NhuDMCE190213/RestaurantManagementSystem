package controller;

import db.DBContext;
import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();
    private DBContext db = new DBContext();

    // validate methods
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {

        String phoneRegex = "^[0-9]{10}$";
        return phone.matches(phoneRegex);
    }

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("popupStatus", status);
            session.setAttribute("popupMessage", message);
        }
    }

    private String getSqlErrorCode(int temp_code) {

        final int DUPLICATE_KEY_CONST = -10;
        final int FOREIGN_KEY_VIOLATION_CONST = -20;
        final int NULL_INSERT_VIOLATION_CONST = -30;
        final int UNIQUE_INDEX_CONST = -40;

        if (temp_code == DUPLICATE_KEY_CONST || temp_code == UNIQUE_INDEX_CONST) {
            return "DUPLICATE_KEY (Account or other unique field already exists)";
        } else if (temp_code == FOREIGN_KEY_VIOLATION_CONST) {
            return "FOREIGN_KEY_VIOLATION";
        } else if (temp_code == NULL_INSERT_VIOLATION_CONST) {
            return "NULL_INSERT_VIOLATION";
        }

        return "Unknown Database Error Code: " + temp_code;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/authentication/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String customerAccount = request.getParameter("account");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String customerName = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");

        String gender = null;
        String address = null;
        java.sql.Date dob = null;

        boolean registerSuccess = true;
        String errorMessage = "";

        try {

            if (isNullOrEmpty(customerAccount)
                    || isNullOrEmpty(password)
                    || isNullOrEmpty(customerName)
                    || isNullOrEmpty(email)
                    || isNullOrEmpty(phoneNumber)) {

                registerSuccess = false;
                errorMessage = "All fields marked with (*) are required. Please fill them out.";

            } else if (!password.equals(confirmPassword)) {

                registerSuccess = false;
                errorMessage = "Password and Confirm Password don't match.";
            } else if (!isValidEmail(email)) {
                registerSuccess = false;
                errorMessage = "Invalid email format.";
            } else if (!isValidPhone(phoneNumber)) {
                registerSuccess = false;
                errorMessage = "Invalid phone number. Must be 10 digits (ex: 0xxxxxxxxx).";

            } else if (customerDAO.checkAccountExist(customerAccount)) {
                registerSuccess = false;
                errorMessage = "Username already exists. Try another.";
            } else if (customerDAO.checkEmailExist(email)) {

                registerSuccess = false;
                errorMessage = "Email already exists. Try another.";
            } else if (customerDAO.checkPhoneExist(phoneNumber)) {
                registerSuccess = false;
                errorMessage = "Phone already exists. Try another.";
            }

            if (registerSuccess) {

                String hashedPassword = db.hashToMD5(password);
                try {
                    int checkError = customerDAO.add(customerAccount, hashedPassword, customerName,
                            gender, phoneNumber, email, address, dob);

                    if (checkError >= 1) {
                        setPopup(request, true, "Register successfully! Sign in to connect.");
                        response.sendRedirect("login");
                        return;
                    } else {
                        registerSuccess = false;
                        errorMessage = "Database Error: " + getSqlErrorCode(checkError);
                    }
                } catch (Exception daoEx) {

                    registerSuccess = false;
                    errorMessage = "DAO error.";
                    System.err.println("DAO Exception during registration: " + daoEx.getMessage());
                    daoEx.printStackTrace();
                }
            }

        } catch (Exception e) {

            registerSuccess = false;
            errorMessage = "Unexpected error.";
            System.err.println("Critical System Error: " + e.getMessage());
            e.printStackTrace();
        }
        if (!registerSuccess) {
            request.setAttribute("error", errorMessage);

            request.setAttribute("account", customerAccount);
            request.setAttribute("name", customerName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phoneNumber);

            request.getRequestDispatcher("/WEB-INF/authentication/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user registration for customers";
    }
}