/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.HashUtil;
import dao.EmployeeDAO;
import db.DBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import utils.EmailSender;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "MyEmployeeProfileServlet", urlPatterns = {"/employee-profile"})
public class MyEmployeeProfileServlet extends HttpServlet {

    private static final int OTP_LENGTH = 6;
    private EmployeeDAO employeeDAO = new EmployeeDAO();
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
        }
        request.removeAttribute("popupStatus");
        request.removeAttribute("popupMessage");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MyEmployeeProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyEmployeeProfileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeSession") == null) {
            response.sendRedirect("employee-login");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employeeSession");
        String action = request.getParameter("action");

        if (action == null || action.equalsIgnoreCase("view")) {
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/profile/view-emp.jsp").forward(request, response);
            removePopup(request);
        } else if (action.equalsIgnoreCase("edit")) {
            request.setAttribute("employee", employee);
            removePopup(request);
            request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("change-password")) {
            removePopup(request);
            request.getRequestDispatcher("/WEB-INF/profile/changepassword-emp.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeSession") == null) {
            response.sendRedirect("employee-login");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employeeSession");
        String action = request.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            updateProfile(request, response, session, employee);
        } else if ("change-password".equalsIgnoreCase(action)) {
            changePassword(request, response, session, employee);
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
            HttpSession session, Employee employee)
            throws ServletException, IOException {
        String submittedOtp = request.getParameter("otp_code");
        String storedOtp = (String) session.getAttribute("otpCode");
        String newEmailPending = (String) session.getAttribute("newEmailPending");
        Employee tempEmpData = (Employee) session.getAttribute("tempEmpData");

        if (!isNullOrEmpty(submittedOtp) && storedOtp != null && newEmailPending != null && tempEmpData != null) {
            if (!storedOtp.equals(submittedOtp)) {
                setErrorPopup(request, "Invalid verification code. Please check your email and try again.");
                request.setAttribute("employee", tempEmpData);
                request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
                return;
            }

            int result = employeeDAO.edit(employee.getEmpId(), employee.getEmpAccount(),
                    tempEmpData.getEmpName(), tempEmpData.getGender(),
                    tempEmpData.getDob(), tempEmpData.getPhoneNumber(),
                    newEmailPending, tempEmpData.getAddress());

            if (result > 0) {
                Employee updated = employeeDAO.getElementByID(employee.getEmpId());
                session.setAttribute("employeeSession", updated);
                setSuccessPopup(request, "Profile and email updated successfully.");
            } else {
                setErrorPopup(request, "Email verified but profile update failed due to a database error.");
            }

            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempEmpData");
            response.sendRedirect(request.getContextPath() + "/employee-profile?action=view");
            return;
        }

        String name = request.getParameter("emp_name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");
        Date dob = null;
        String errorMessage = null;

        Employee submittedData = new Employee(employee.getEmpId(), employee.getEmpAccount(), employee.getPassword(),
                name, gender, dob, phone, email, address);

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
            errorMessage = "Invalid phone number format.";
        } else if (errorMessage == null && dob != null && !isValidAgeRange(dob, 18, 70)) {
            errorMessage = "Age must be between 18 and 70 years old.";
        }

        // Check if phone or email already exists
        if (errorMessage == null && phone != null && !phone.equals(employee.getPhoneNumber())) {
            try {
                if (employeeDAO.checkPhoneExist(phone)) {
                    errorMessage = "This phone number is already registered.";
                }
            } catch (Exception ex) {
                Logger.getLogger(MyEmployeeProfileServlet.class.getName()).log(Level.SEVERE, "Error checking phone existence", ex);
                errorMessage = "Error checking phone number existence.";
            }
        }

        boolean emailChanged = email != null && !email.equalsIgnoreCase(employee.getEmail());

        if (errorMessage == null && emailChanged) {
            try {
                if (employeeDAO.checkEmailExist(email)) {
                    errorMessage = "This email address is already in use by another account.";
                } else {
                    String otpCode = generateOTP();
                    String subject = "Email Change Verification Code";
                    String content = "Your verification code to update your email address is: " + otpCode;

                    emailSender.authenticatebyEmail(email, subject, content);

                    session.setAttribute("otpCode", otpCode);
                    session.setAttribute("newEmailPending", email);
                    session.setAttribute("tempEmpData", submittedData);

                    setSuccessPopup(request, "A verification code has been sent to your new email (" + email + "). Please enter it below to confirm the change.");
                    request.setAttribute("employee", submittedData);
                    request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(MyEmployeeProfileServlet.class.getName()).log(Level.SEVERE, "Error during email check or sending OTP", ex);
                errorMessage = "Error processing email change: " + ex.getMessage();
            }
        }

        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);
            request.setAttribute("employee", submittedData);
            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempEmpData");
            request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
            return;
        }

        int result = employeeDAO.edit(
                employee.getEmpId(),
                employee.getEmpAccount(),
                name,
                gender,
                dob,
                phone,
                email,
                address
        );

        if (result > 0) {
            Employee updated = employeeDAO.getElementByID(employee.getEmpId());
            session.setAttribute("employeeSession", updated);
            session.removeAttribute("otpCode");
            session.removeAttribute("newEmailPending");
            session.removeAttribute("tempEmpData");

            setSuccessPopup(request, "Profile updated successfully.");
            response.sendRedirect(request.getContextPath() + "/employee-profile?action=view");
        } else {
            setErrorPopup(request, "Failed to update profile. Database error.");
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Employee employee)
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
            if (!hashedOld.equals(employee.getPassword())) {
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
            request.getRequestDispatcher("/WEB-INF/profile/changepassword-emp.jsp").forward(request, response);
            return;
        }

        String hashedNew = db.hashToMD5(newPassword);
        int result = employeeDAO.edit(employee.getEmpId(), hashedNew);

        if (result > 0) {
            employee.setPassword(hashedNew);
            session.setAttribute("employeeSession", employee);
            setSuccessPopup(request, "Password changed successfully.");
            response.sendRedirect(request.getContextPath() + "/employee-profile?action=view");
            return;
        } else {
            setErrorPopup(request, "Failed to change password. Database error.");
            request.getRequestDispatcher("/WEB-INF/profile/changepassword-emp.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
