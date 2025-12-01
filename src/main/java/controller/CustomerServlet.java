/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.addEDtoEverything;
import static constant.CommonFunction.getSqlErrorCode;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateInteger;
import static constant.CommonFunction.validateString;
import constant.Constants;
import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customer"})
public class CustomerServlet extends HttpServlet {

    CustomerDAO customerDAO = new CustomerDAO();

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
            out.println("<title>Servlet CustomerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerServlet at " + request.getContextPath() + "</h1>");
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

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
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

            request.setAttribute("currentCustomer", customerDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(customerDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("customerList", customerDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/customer/" + namepage + ".jsp").forward(request, response);
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
                String cusAccount = request.getParameter("customerAccount");
                String password = request.getParameter("password");
                String cusName = request.getParameter("customerName");

                //validate
                if (!validateString(cusAccount, -1)
                        || !validateString(password, -1)
                        || !validateString(cusName, -1)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with name=" + cusName + " added successfull"
                            + "(Account: " + cusAccount + "; "
                            + "Password: " + password + ")";
                }
                //end

                password = constant.HashUtil.toMD5(password);

                if (popupStatus == true) {
                    int checkError = customerDAO.add(cusAccount, password, cusName);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("updateStatus")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Invalid ID.";
                } else {
                    int checkError = customerDAO.updateStatus(id);
                    if (checkError >= 1) {
                        popupStatus = true;
                        popupMessage = "Customer status (ID: " + id + ") updated successfully!";
                    } else {
                        popupStatus = false;
                        popupMessage = "Failed to update status. SQL error: " + getSqlErrorCode(checkError);
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                //validate
                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " deleted successfull.";
                }
                //end
                if (popupStatus == true) {
                    int checkError = customerDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }
            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath() + "/customer");
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
