/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EmployeeDAO;
import dao.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Role;
import utils.EmailSender;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"})
public class EmployeeServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    EmployeeDAO employeeDAO = new EmployeeDAO();
    RoleDAO roleDAO = new RoleDAO();

    private final EmailSender emailSender = new EmailSender();

    boolean popupStatus = true;
    String popupMessage = "";

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
            out.println("<title>Servlet EmployeeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmployeeServlet at " + request.getContextPath() + "</h1>");
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
        String namepage = "";
        String view = request.getParameter("view");

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "list";
        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
        } else if (view.equalsIgnoreCase("edit")) {
            namepage = "edit";

            int id;

            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            request.setAttribute("currentEmployee", employeeDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(employeeDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("rolesList", roleDAO.getAll());
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("employeeList", employeeDAO.getAll(page, MAX_ELEMENTS_PER_PAGE, keyword));

        request.getRequestDispatcher("/WEB-INF/employee/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
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

        popupStatus = true;
        popupMessage = "";

        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("add")) {
                add(request);
            } else if (action.equalsIgnoreCase("edit")) {
                edit(request);
            } else if (action.equalsIgnoreCase("delete")) {
                delete(request);
            } else if (action.equalsIgnoreCase("ban")) {
                ban(request);
            } else if (action.equalsIgnoreCase("unban")) {
                unban(request);
            }
            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath() + "/employee");
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

    private int getTotalPages(int countItems) {
        return (int) Math.ceil((double) countItems / MAX_ELEMENTS_PER_PAGE);
    }

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        session.setAttribute("popupStatus", status);
        session.setAttribute("popupMessage", message);
    }

    private void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute("popupStatus");
        session.removeAttribute("popupMessage");
    }

    private void add(HttpServletRequest request) {
        String empAccount = request.getParameter("empAccount");
        String password = request.getParameter("password");
        String empName = request.getParameter("empName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int roleId;

        try {
            roleId = Integer.parseInt(request.getParameter("roleId"));
        } catch (NumberFormatException ex) {
            roleId = -1;
        }

        Role role = roleDAO.getElementByID(roleId);
//validate
        popupStatus = false;
        popupMessage = "The add action is NOT successfull.";
        if (empAccount == null || empAccount.isBlank()) {
            popupMessage += " The username has been blank.";
        } else if (password == null || password.isBlank()) {
            popupMessage += " The password has been blank.";
        } else if (empName == null || empName.isBlank()) {
            popupMessage += " The name has been blank.";
        } else if (phone == null || phone.isBlank()) {
            popupMessage += " The phone has been blank.";
        } else if (email == null || email.isBlank()) {
            popupMessage += " The email has been blank.";
        } else if (role == null) {
            popupMessage += " The role is not exist.";
        } else {
            popupStatus = true;
            popupMessage = "The object with name=" + empName + " added successfull;"
                    + "(Account: " + empAccount + "; "
                    + "Password: " + password + ")";
        }
//end

        String passwordHarshMd5 = employeeDAO.hashToMD5(password);

        if (popupStatus == true) {
//            int checkError = employeeDAO.add(empAccount, passwordHarshMd5, empName, roleId);
            int checkError = employeeDAO.add(empAccount, passwordHarshMd5, empName, null, null, phone, email, null, role.getId());
            if (checkError >= 1) {
                emailSender.sendPasswordToEmployeeEmail(email, "New employee account has been registered",
                        "Dear " + empName + ",\n\n"
                        + "Welcome to our company!\n\n"
                        + "Your employee account has been successfully created. Below are your login details:\n\n"
                        + "----------------------------------------\n"
                        + "Account: " + empAccount + "\n"
                        + "Password: " + password + "\n"
                        + "Role: " + role.getName() + "\n"
                        + "Link: http://localhost:8080/SWP391_RMS/login_employee \n"
                        + "----------------------------------------\n\n"
                        + "For security reasons, please log in and change your password as soon as possible.\n\n"
                        + "If you have any questions or need technical assistance, feel free to contact the IT Support team.\n\n"
                        + "Best regards,\n"
                        + "Human Resources Department\n"
                        + "Yummy");

            } else {
                popupStatus = false;
                popupMessage = "The add action is NOT successfull. Check the information again.";
            }
        }
    }

    private void edit(HttpServletRequest request) {
        int empId;
        int roleId;

        try {
            empId = Integer.parseInt(request.getParameter("id"));
            roleId = Integer.parseInt(request.getParameter("roleId"));
        } catch (NumberFormatException e) {
            empId = -1;
            roleId = -1;
        }

        Role role = roleDAO.getElementByID(roleId);
//validate
        popupStatus = false;
        popupMessage = "The edit action is NOT successfull.";
        if (empId <= 0) {
            popupMessage += " The account is not exist.";
        } else if (role == null) {
            popupMessage += " The role is not exist.";
        } else {
            popupStatus = true;
            popupMessage = "The object with id=" + empId + " edited role successfull.";
        }
//end

        if (popupStatus == true) {
            int checkError = employeeDAO.edit(empId, role.getId());

            if (checkError >= 1) {

            } else {
                popupStatus = false;
                popupMessage = "The edit action is NOT successfull. Check the information again.";
            }
        }
    }

    private void delete(HttpServletRequest request) {
        int empId;

        try {
            empId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            empId = -1;
        }

//validate
        popupStatus = false;
        popupMessage = "The delete action is NOT successfull.";
        if (empId <= 0) {
            popupMessage += " The account is not exist.";
        } else {
            popupStatus = true;
            popupMessage = "The object with id=" + empId + " deleted successfull.";
        }
//end

        if (popupStatus == true) {
            int checkError = employeeDAO.delete(empId);

            if (checkError >= 1) {
            } else {
                popupStatus = false;
                popupMessage = "The delete action is NOT successfull. Check the information again.";
            }
        }
    }

    private void ban(HttpServletRequest request) {
        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            id = -1;
        }

//validate
        popupStatus = false;
        popupMessage = "The ban action is NOT successfull.";
        if (id <= 0) {
            popupMessage += " The account is not exist.";
        } else {
            popupStatus = true;
            popupMessage = "The object with id=" + id + " banned successfull.";
        }
//end
        if (popupStatus == true) {
            int checkError = employeeDAO.ban(id);

            if (checkError >= 1) {

            } else {
                popupStatus = false;
                popupMessage = "The ban action is NOT successfull. Check the information again.";
            }
        }
    }

    private void unban(HttpServletRequest request) {
        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            id = -1;
        }

//validate
        popupStatus = false;
        popupMessage = "The unban action is NOT successfull.";
        if (id <= 0) {
            popupMessage += " The account is not exist.";
        } else {
            popupStatus = true;
            popupMessage = "The object with id=" + id + " unbanned successfull.";
        }
//end
        if (popupStatus == true) {
            int checkError = employeeDAO.unban(id);

            if (checkError >= 1) {

            } else {
                popupStatus = false;
                popupMessage = "The unban action is NOT successfull. Check the information again.";
            }
        }
    }

}
