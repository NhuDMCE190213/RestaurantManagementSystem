/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.HashUtil;
import dao.EmployeeDAO;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "MyEmployeeProfileServlet", urlPatterns = {"/employee-profile"})
public class MyEmployeeProfileServlet extends HttpServlet {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    
    
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

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Employee employee)
            throws ServletException, IOException {

        String name = request.getParameter("emp_name");
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
            } catch (Exception e) {
                errorMessage = "Invalid date format for Date of Birth.";
            }
        }
        
        if (isNullOrEmpty(name)) {
            errorMessage = "Full Name is required.";

        } else if (errorMessage == null && !isValidEmail(email)) {
            errorMessage = "Invalid email format.";

        } else if (errorMessage == null && !isValidPhone(phone)) {
            errorMessage = "Invalid phone number format. Must be 10 digits.";

        } else if (errorMessage == null && dob != null && !isValidAgeRange(dob, 18, 70)) {
            errorMessage = "Age must be between 18 and 70 years old.";
        }
        
        if (errorMessage == null && !email.equalsIgnoreCase(employee.getEmail())) {
            try {
                if (employeeDAO.checkEmailExist(email)) {
                    errorMessage = "This email is already in use.";
                }
            } catch (Exception e) {
                Logger.getLogger(MyEmployeeProfileServlet.class.getName())
                        .log(Level.SEVERE, "Email check failed", e);
                errorMessage = "Error checking email.";
            }
        }
        
        if (errorMessage == null && !phone.equals(employee.getPhoneNumber())) {
            try {
                if (employeeDAO.checkPhoneExist(phone)) {
                    errorMessage = "This phone number is already registered.";
                }
            } catch (Exception e) {
                Logger.getLogger(MyEmployeeProfileServlet.class.getName())
                        .log(Level.SEVERE, "Phone check failed", e);
                errorMessage = "Error checking phone number.";
            }
        }
        
        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);

            Employee temp = new Employee(
                    employee.getEmpId(),
                    employee.getEmpAccount(),
                    employee.getPassword(),
                    name, gender, dob, phone, email, address
            );

            request.setAttribute("employee", temp);
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
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        String errorMessage = null;

        if (isNullOrEmpty(oldPass) || isNullOrEmpty(newPass) || isNullOrEmpty(confirm)) {
            errorMessage = "Please fill all fields.";
        }

        if (errorMessage == null) {
            String hashedOld = HashUtil.toMD5(oldPass);
            if (!hashedOld.equals(employee.getPassword())) {
                errorMessage = "Old password is incorrect.";
            }
        }

        if (errorMessage == null && !newPass.equals(confirm)) {
            errorMessage = "New password and confirmation do not match.";
        }

        if (errorMessage != null) {
            setErrorPopup(request, errorMessage);
            request.getRequestDispatcher("/WEB-INF/profile/changepassword-emp.jsp").forward(request, response);
            return;
        }

        String hashedNew = HashUtil.toMD5(newPass);
        int result = employeeDAO.edit(employee.getEmpId(), hashedNew);

        if (result > 0) {
            employee.setPassword(hashedNew);
            session.setAttribute("employeeSession", employee);

            setSuccessPopup(request, "Password changed successfully.");
            response.sendRedirect(request.getContextPath() + "/employee-profile?action=view");

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
