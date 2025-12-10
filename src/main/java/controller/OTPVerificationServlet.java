package controller;

import dao.CustomerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "OTPVerificationServlet", urlPatterns = {"/verification"})
public class OTPVerificationServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("popupStatus", status);
            session.setAttribute("popupMessage", message);
        }
    }

    private void clearRegistrationSession(HttpSession session) {
        if (session != null) {
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                String attribute = attributes.nextElement();
                if (attribute.startsWith("temp_") || attribute.equals("registration_otp")) {
                    session.removeAttribute(attribute);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("registration_otp") == null) {
            setPopup(request, false, "Invalid request. Please start the registration process again.");
            response.sendRedirect("register");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/authentication/verification.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inputOTP = request.getParameter("otp");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("registration_otp") == null) {
            setPopup(request, false, "Session expired or invalid request. Please register again.");
            response.sendRedirect("register");
            return;
        }

        String sessionOTP = (String) session.getAttribute("registration_otp");

        if (inputOTP != null && inputOTP.equals(sessionOTP)) {

            String customerAccount = (String) session.getAttribute("temp_account");
            String hashedPassword = (String) session.getAttribute("temp_password");
            String customerName = (String) session.getAttribute("temp_name");
            String email = (String) session.getAttribute("temp_email");
            String phoneNumber = (String) session.getAttribute("temp_phone");
            String gender = (String) session.getAttribute("temp_gender");
            String address = (String) session.getAttribute("temp_address");
            java.sql.Date dob = (java.sql.Date) session.getAttribute("temp_dob");

            try {
               //success
                int checkError = customerDAO.add(customerAccount, hashedPassword, customerName,
                        gender, phoneNumber, email, address, dob);

                if (checkError >= 1) {
                    clearRegistrationSession(session);
                    setPopup(request, true, "Register successfully! You can now log in.");
                    response.sendRedirect("login");
                    return;
                } else {
                    request.setAttribute("error", "Database Error:" + checkError);
                }
            } catch (Exception daoEx) {
                request.setAttribute("error", "Unexpected error.");
                Logger.getLogger(OTPVerificationServlet.class.getName()).log(Level.SEVERE, "DAO Exception", daoEx);
            }
        } else {
            request.setAttribute("error", "Invalid OTP. Please check your email and try again.");
        }
        request.getRequestDispatcher("/WEB-INF/authentication/verification.jsp").forward(request, response);
    }
}
