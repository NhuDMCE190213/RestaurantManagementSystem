package controller;

import dao.CustomerDAO;
import model.Customer;
import db.DBContext;

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

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "CustomerProfileServlet", urlPatterns = {"/customer-profile"})
public class MyCustomerProfileServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();
    private DBContext db = new DBContext();

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
            request.getRequestDispatcher("/WEB-INF/profile/view.jsp").forward(request, response);
            removePopup(request);
        } else if (action.equalsIgnoreCase("edit")) {
            request.setAttribute("customer", customer);
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

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Customer customer)
            throws ServletException, IOException {

        String name = request.getParameter("customer_name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");
        Date dob = null;
        String errorMessage = null;

        if (!isNullOrEmpty(dobStr)) {
            try {
                dob = Date.valueOf(dobStr);
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

        if (errorMessage == null && email != null && !email.equalsIgnoreCase(customer.getEmail())) {
            try {
                if (customerDAO.checkEmailExist(email)) {
                    errorMessage = "This email address is already in use by another account.";
                }
            } catch (Exception ex) {
                Logger.getLogger(MyCustomerProfileServlet.class.getName()).log(Level.SEVERE, "Error checking email existence", ex);
                errorMessage = "Error checking email existence.";
            }
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

        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);
            Customer tempCustomer = new Customer(customer.getCustomerId(), customer.getCustomerAccount(), customer.getPassword(),
                    name, gender, phone, email, address, dob);
            request.setAttribute("customer", tempCustomer);
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