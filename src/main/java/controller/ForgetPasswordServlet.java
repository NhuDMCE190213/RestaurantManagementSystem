/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
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
import model.Customer;
import model.Employee;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "ForgetPasswordServlet", urlPatterns = {"/forgetPassword"})
public class ForgetPasswordServlet extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final DBContext dbContext = new DBContext();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ForgetPasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ForgetPasswordServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("resetCode") != null) {
            request.setAttribute("error", "Error occured during previous process. Please start over.");
            session.removeAttribute("resetCode");
            session.removeAttribute("resetEmail");
            session.removeAttribute("userId");
            session.removeAttribute("userType");
        }
        request.getRequestDispatcher("/WEB-INF/authentication/forgot.jsp").forward(request, response);
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
        String action = request.getParameter("action");

        HttpSession session = request.getSession();

        if ("send_code".equals(action)) {
            handleSendCode(request, response, session);
        } else if ("reset_password".equals(action)) {
            handleResetPassword(request, response, session);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void handleSendCode(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String errorMessage = "";
        Object user = null;
        String userType = null;
        if (email == null || email.trim().isEmpty()) {
            errorMessage = "Please enter your email address.";
        } else {

            Customer customer = customerDAO.getElementByEmail(email);
            if (customer != null) {
                user = customer;
                userType = "customer";
            } else {

                Employee employee = employeeDAO.getElementByEmail(email);
                if (employee != null) {
                    user = employee;
                    userType = "employee";
                }
            }

            if (user == null) {
                errorMessage = "This email is not registered with any user account or the account is banned.";
            } else {
                // reset code
                String resetCode = String.valueOf((int) (Math.random() * 900000) + 100000);
                session.setAttribute("resetCode", resetCode);
                session.setAttribute("resetEmail", email);
                if ("customer".equals(userType)) {
                    session.setAttribute("userId", ((Customer) user).getCustomerId());
                } else {
                    session.setAttribute("userId", ((Employee) user).getEmpId());
                }
                session.setAttribute("userType", userType);
                session.setMaxInactiveInterval(5 * 60); // Code expires in 5 minutes

                // send email
                String subject = "Password Reset Code";
                String content = "Your password reset code is: " + resetCode + ". This code will expire in 5 minutes.";
                dbContext.sendEmail(email, subject, content);
                request.setAttribute("message", "A password reset code has been sent to your email.");
                request.getRequestDispatcher("/WEB-INF/authentication/forgot.jsp").forward(request, response);
                return;
            }
        }
        request.setAttribute("email", email);
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/authentication/forgot.jsp").forward(request, response);
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        String submittedCode = request.getParameter("code");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String storedCode = (String) session.getAttribute("resetCode");
        Integer userId = (Integer) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");
        String errorMessage = "";

        if (storedCode == null || userId == null || userType == null) {
            errorMessage = "The reset session has expired or is invalid. Please start the process again.";
        } else if (submittedCode == null || !submittedCode.equals(storedCode)) {
            errorMessage = "Invalid reset code. Please check your email and try again.";
        } else if (newPassword == null || newPassword.trim().isEmpty() || !newPassword.equals(confirmPassword)) {
            errorMessage = "Passwords do not match or are empty.";
        } else if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            errorMessage = "Password must be at least 6 characters long.";
        } else {
            String hashedPassword = dbContext.hashToMD5(newPassword);
            int result = -1;

            if ("customer".equals(userType)) {
                result = customerDAO.updatePassword(userId, hashedPassword);
            } else if ("employee".equals(userType)) {
                result = employeeDAO.updatePassword(userId, hashedPassword);
            }

            if (result > 0) {

                session.removeAttribute("resetCode");
                session.removeAttribute("resetEmail");
                session.removeAttribute("userId");
                session.removeAttribute("userType");

                session.setAttribute("successMessage", "Password updated successfully. Please login with your new password.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            } else {
                errorMessage = "An error occurred while updating your password. Please try again.";
            }
        }

        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/authentication/forgot.jsp").forward(request, response);
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
