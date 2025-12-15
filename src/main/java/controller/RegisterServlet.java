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
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.EmailSender;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();
    private DBContext db = new DBContext();
    private EmailSender emailSender = new EmailSender();

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
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        Date dob = null;

        boolean registerSuccess = true;
        String errorMessage = "";
        HttpSession session = request.getSession();

        try {

            // validate
            if (isNullOrEmpty(customerAccount) || isNullOrEmpty(password) || isNullOrEmpty(customerName)
                    || isNullOrEmpty(email) || isNullOrEmpty(phoneNumber)) {
                registerSuccess = false;
                errorMessage = "All fields marked with (*) are required. Please fill them out.";
            } else if (!password.equals(confirmPassword)) {
                registerSuccess = false;
                errorMessage = "Password and Confirm Password don't match.";
            } else if (password.length() < 6) {
                registerSuccess = false;
                errorMessage = "Password must be at least 6 characters long.";
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

            //send otp
            if (registerSuccess) {

                // otp
                String otpCode = String.valueOf((int) (Math.random() * 900000) + 100000);
                String hashedPassword = db.hashToMD5(password);

                // temp store
                session.setAttribute("temp_account", customerAccount);
                session.setAttribute("temp_password", hashedPassword);
                session.setAttribute("temp_name", customerName);
                session.setAttribute("temp_email", email);
                session.setAttribute("temp_phone", phoneNumber);
                session.setAttribute("temp_gender", gender);
                session.setAttribute("temp_address", address);
                session.setAttribute("temp_dob", dob);
                session.setAttribute("registration_otp", otpCode);

                String subject = "Yummy Restaurant Registration - OTP Verification";
                String content = "Hello " + customerName + ",\n\nYour registration verification code is: " + otpCode
                        + "\n\nPlease enter this code on the website to complete your registration."
                        + "\n\nThis code is valid for a short time.";

                emailSender.authenticatebyEmail(email, subject, content); //send otp

                response.sendRedirect("verification");
                return;
            }

        } catch (RuntimeException e) {
            registerSuccess = false;
            errorMessage = "System Error (Email/Hash): Failed to send email or hash password.";
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            registerSuccess = false;
            errorMessage = "Unexpected error.";
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, e);
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
