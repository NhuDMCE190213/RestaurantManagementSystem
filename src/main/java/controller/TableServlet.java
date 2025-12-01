/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.Constants.*;
import dao.TableDAO;
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
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "TableServlet", urlPatterns = {"/table"})
public class TableServlet extends HttpServlet {

    TableDAO tableDAO = new TableDAO();

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
            out.println("<title>Servlet RoleServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoleServlet at " + request.getContextPath() + "</h1>");
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

        if (!isValidString(view, -1) || view.equalsIgnoreCase("list")) {
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
            request.setAttribute("currentTable", tableDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(tableDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("tablesList", tableDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/table/" + namepage + ".jsp").forward(request, response);

        // Xóa popup sau khi hiển thị
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
        boolean popupStatus = true;
        String popupMessage = "";

        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("add")) {
                String number = request.getParameter("number");
                int capacity;
                try {
                    capacity = Integer.parseInt(request.getParameter("table_capacity"));
                } catch (NumberFormatException e) {
                    capacity = -1;
                }

                // validate
                if (!isValidString(number, 10) || !isValidInteger(capacity, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successful. Invalid input.";
                } else {
                    int result = tableDAO.add(number, capacity);
                    if (result >= 1) {
                        popupMessage = "Table " + number + " added successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action failed. SQL Error: " + getSqlErrorCode(result);
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                String number = request.getParameter("number");
                int capacity;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    capacity = Integer.parseInt(request.getParameter("table_capacity"));
                } catch (NumberFormatException e) {
                    id = -1;
                    capacity = -1;
                }

                // validate
                if (!isValidInteger(id, false, false, true)
                        || !isValidString(number, 10)
                        || !isValidInteger(capacity, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successful. Invalid input.";
                } else {
                    int result = tableDAO.edit(id, number, capacity);
                    if (result >= 1) {
                        popupMessage = "Table with ID=" + id + " edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action failed. SQL Error: " + getSqlErrorCode(result);
                    }
                }

            } else if (action.equalsIgnoreCase("delete")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                // validate
                if (!isValidInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successful. Invalid ID.";
                } else {
                    int result = tableDAO.delete(id);
                    if (result >= 1) {
                        popupMessage = "Table with ID=" + id + " deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action failed. SQL Error: " + getSqlErrorCode(result);
                    }
                }
            } else if (action.equalsIgnoreCase("changeStatus")) {
                // New action: change table status (Available, Reserved, Occupied)
                int id;
                String newStatus = request.getParameter("newStatus");
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                // validate id and status
                if (!isValidInteger(id, false, false, true)
                        || newStatus == null
                        || !(newStatus.equalsIgnoreCase("Available")
                        || newStatus.equalsIgnoreCase("Reserved")
                        || newStatus.equalsIgnoreCase("Occupied"))) {
                    popupStatus = false;
                    popupMessage = "Change status action is NOT successful. Invalid input.";
                } else {
                    // Normalize status to have capital first letter (matching DB conventions in this project)
                    String normalizedStatus = newStatus.substring(0, 1).toUpperCase() + newStatus.substring(1).toLowerCase();
                    int result = tableDAO.updateStatus(id, normalizedStatus);
                    if (result >= 1) {
                        popupMessage = "Table with ID=" + id + " status changed to " + normalizedStatus + " successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Change status action failed. SQL Error: " + getSqlErrorCode(result);
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/table");
    }

    private boolean isValidString(String str, int limitLength) {
        if (limitLength < 0) {
            limitLength = Integer.MAX_VALUE;
        }

        return !(str == null || str.isEmpty()) && str.length() <= limitLength;
    }

    private boolean isValidInteger(int value, boolean allowZero, boolean allowNegative, boolean allowPositive) {
        if (!allowNegative && value < 0) {
            return false;
        }
        if (!allowZero && value == 0) {
            return false;
        }

        if (!allowPositive && value > 0) {
            return false;
        }
        return true;
    }

    private int getTotalPages(int countItems) {
        return (int) Math.ceil((double) countItems / MAX_ELEMENTS_PER_PAGE);
    }

    private String getSqlErrorCode(int temp_code) {
        if (temp_code + DUPLICATE_KEY == 0) {                //check trung code/key
            return "DUPLICATE_KEY";
        } else if (temp_code + FOREIGN_KEY_VIOLATION == 0) {
            return "FOREIGN_KEY_VIOLATION";
        } else if (temp_code + NULL_INSERT_VIOLATION == 0) {
            return "NULL_INSERT_VIOLATION";
        } else if (temp_code + UNIQUE_INDEX == 0) {
            return "DUPLICATE_UNIQUE";
        }

        return "Unknow Error Code:" + temp_code;
    }

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("popupStatus", status);
            session.setAttribute("popupMessage", message);
        }
    }

    private void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("popupStatus");
            session.removeAttribute("popupMessage");
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
