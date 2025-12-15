package controller;

import dao.CustomerDAO;
import model.Customer;
import db.DBContext;
import utils.EmailSender; // Import the EmailSender utility
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "CustomerProfileServlet", urlPatterns = {"/customer-profile"})
public class MyCustomerProfileServlet extends HttpServlet {

    private static final int OTP_LENGTH = 6;
    private CustomerDAO customerDAO = new CustomerDAO();
    private DBContext db = new DBContext();
    private EmailSender emailSender = new EmailSender();

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9]{10}$";
        return phone != null && phone.matches(phoneRegex);
    }

    private boolean isValidAgeRange(Date dob, int minAge, int maxAge) {
        if (dob == null) {
            return true;
        }
        try {
            LocalDate birthDate = dob.toLocalDate();
            LocalDate today = LocalDate.now();

            Period age = Period.between(birthDate, today);
            int years = age.getYears();

            return years >= minAge && years <= maxAge;
        } catch (Exception e) {
            return false;
        }
    }

    private void setSuccessPopup(HttpServletRequest request, String message) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("popupStatus", true);
            session.setAttribute("popupMessage", message);
        }
    }

    private void setErrorPopup(HttpServletRequest request, String message) {
        request.setAttribute("popupStatus", false);
        request.setAttribute("popupMessage", message);
    }

    private void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("popupStatus");
            session.removeAttribute("popupMessage");
            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempCustomerData");
        }
        request.removeAttribute("popupStatus");
        request.removeAttribute("popupMessage");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customerSession");
        String action = request.getParameter("action");

        if (action == null || action.equalsIgnoreCase("view")) {
            request.setAttribute("customer", customer);
            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempCustomerData");
            request.getRequestDispatcher("/WEB-INF/profile/view.jsp").forward(request, response);
            removePopup(request);
        } else if (action.equalsIgnoreCase("edit")) {
            Customer tempCustomer = (Customer) session.getAttribute("tempCustomerData");
            if (tempCustomer != null) {
                request.setAttribute("customer", tempCustomer);
            } else {
                request.setAttribute("customer", customer);
            }

            removePopup(request);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("change-password")) {
            removePopup(request);
            request.getRequestDispatcher("/WEB-INF/profile/changepassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customerSession");
        String action = request.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            updateProfile(request, response, session, customer);
        } else if ("change-password".equalsIgnoreCase(action)) {
            changePassword(request, response, session, customer);
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int min = (int) Math.pow(10, OTP_LENGTH - 1);
        int max = (int) Math.pow(10, OTP_LENGTH) - 1;
        int code = random.nextInt(max - min + 1) + min;
        return String.valueOf(code);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Customer customer)
            throws ServletException, IOException {

        // State retrieval
        String submittedOtp = request.getParameter("otp_code");
        String storedOtp = (String) session.getAttribute("otpCode");
        String newEmailPending = (String) session.getAttribute("newEmailPending");
        Customer tempCustomerData = (Customer) session.getAttribute("tempCustomerData");

        if (!isNullOrEmpty(submittedOtp) && storedOtp != null && newEmailPending != null && tempCustomerData != null) {
            if (!storedOtp.equals(submittedOtp)) {
                setErrorPopup(request, "Invalid verification code. Please check your email and try again.");
                request.setAttribute("customer", tempCustomerData);
                request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
                return;
            }
            int result = customerDAO.edit(
                    customer.getCustomerId(),
                    customer.getCustomerAccount(),
                    tempCustomerData.getCustomerName(),
                    tempCustomerData.getGender(),
                    tempCustomerData.getPhoneNumber(),
                    newEmailPending,
                    tempCustomerData.getAddress(),
                    tempCustomerData.getDob()
            );

            if (result > 0) {
                Customer updated = customerDAO.getElementByID(customer.getCustomerId());
                session.setAttribute("customerSession", updated);
                setSuccessPopup(request, "Profile and email updated successfully.");
            } else {

                setSuccessPopup(request, "Email verified but profile update failed due to a database error.");
            }

            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempCustomerData");

            response.sendRedirect(request.getContextPath() + "/customer-profile?action=view");
            return;
        }

        //validate
        String name = request.getParameter("customer_name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");
        Date dob = null;
        String errorMessage = null;

        Customer submittedData = new Customer(customer.getCustomerId(), customer.getCustomerAccount(), customer.getPassword(),
                name, gender, phone, email, address, dob);

        if (!isNullOrEmpty(dobStr)) {
            try {
                dob = Date.valueOf(dobStr);
                submittedData.setDob(dob);
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid date format for Date of Birth.";
            }
        }
        if (isNullOrEmpty(name)) {
            errorMessage = "Full Name is required.";
        } else if (errorMessage == null && !isValidEmail(email)) {
            errorMessage = "Invalid email format.";
        } else if (errorMessage == null && !isValidPhone(phone)) {
            errorMessage = "Invalid phone number format. Must be 10 digits. (ex: 0xxxxxxxxx).";
        } else if (errorMessage == null && dob != null && !isValidAgeRange(dob, 18, 70)) {
            errorMessage = "Abnormal age found. Age must be between 18 and 70 years old.";
        }
        if (errorMessage == null && phone != null && !phone.equals(customer.getPhoneNumber())) {
            try {
                if (customerDAO.checkPhoneExist(phone)) {
                    errorMessage = "This phone number is already registered.";
                }
            } catch (Exception ex) {
                Logger.getLogger(MyCustomerProfileServlet.class.getName()).log(Level.SEVERE, "Error checking phone existence", ex);
                errorMessage = "Error checking phone number existence.";
            }
        }

        // Email Change Check
        boolean emailChanged = email != null && !email.equalsIgnoreCase(customer.getEmail());

        if (errorMessage == null && emailChanged) {
            try {
                if (customerDAO.checkEmailExist(email)) {
                    errorMessage = "This email address is already in use by another account.";
                } else {

                    String otpCode = generateOTP();
                    String subject = "Email Change Verification Code";
                    String content = "Your verification code to update your email address is: " + otpCode;

                    emailSender.authenticatebyEmail(email, subject, content);

                    session.setAttribute("otpCode", otpCode);
                    session.setAttribute("newEmailPending", email);
                    session.setAttribute("tempCustomerData", submittedData);

                    setSuccessPopup(request, "A verification code has been sent to your new email (" + email + "). Please enter it below to confirm the change.");

                    request.setAttribute("customer", submittedData);
                    request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(MyCustomerProfileServlet.class.getName()).log(Level.SEVERE, "Error during email check or sending OTP", ex);
                errorMessage = "Error processing email change: " + ex.getMessage();
            }
        }

        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);
            request.setAttribute("customer", submittedData);

            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempCustomerData");
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
            return;
        }

        int result = customerDAO.edit(
                customer.getCustomerId(),
                customer.getCustomerAccount(),
                name,
                gender,
                phone,
                email,
                address,
                dob
        );

        if (result > 0) {
            Customer updated = customerDAO.getElementByID(customer.getCustomerId());
            session.setAttribute("customerSession", updated);
            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempCustomerData");

            setSuccessPopup(request, "Profile updated successfully.");
            response.sendRedirect(request.getContextPath() + "/customer-profile?action=view");
            return;
        } else {
            setErrorPopup(request, "Failed to update profile. Database error.");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Customer customer)
            throws ServletException, IOException {

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String errorMessage = null;

        if (isNullOrEmpty(oldPassword) || isNullOrEmpty(newPassword) || isNullOrEmpty(confirmPassword)) {
            errorMessage = "Please fill all fields.";
        }

        if (errorMessage == null) {
            String hashedOld = db.hashToMD5(oldPassword);
            if (!hashedOld.equals(customer.getPassword())) {
                errorMessage = "Old password is incorrect.";
            }
        }

        if (errorMessage == null) {
            if (!newPassword.equals(confirmPassword)) {
                errorMessage = "New password and confirmation do not match.";
            }
        }
        if (errorMessage == null) {
            if (newPassword.length() < 6) {
                errorMessage = "Password must be at least 6 characters long.";
            }
        }
        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);
            request.getRequestDispatcher("/WEB-INF/profile/changepassword.jsp").forward(request, response);
            return;
        }

        String hashedNew = db.hashToMD5(newPassword);
        int result = customerDAO.edit(customer.getCustomerId(), hashedNew);

        if (result > 0) {
            customer.setPassword(hashedNew);
            session.setAttribute("customerSession", customer);
            setSuccessPopup(request, "Password changed successfully.");
            response.sendRedirect(request.getContextPath() + "/customer-profile?action=view");
            return;
        } else {
            setErrorPopup(request, "Failed to change password. Database error.");
            request.getRequestDispatcher("/WEB-INF/profile/changepassword.jsp").forward(request, response);
        }
    }
}
