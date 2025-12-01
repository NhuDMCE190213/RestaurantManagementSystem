/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.*;
import dao.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author TruongBinhTrong
 */
@WebServlet(name = "SupplierServlet", urlPatterns = {"/supplier"})
public class SupplierServlet extends HttpServlet {

    SupplierDAO supplierDAO = new SupplierDAO();

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
            out.println("<title>Servlet SupplierServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SupplierServlet at " + request.getContextPath() + "</h1>");
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

            request.setAttribute("currentSupplier", supplierDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(supplierDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("suppliersList", supplierDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/supplier/" + namepage + ".jsp").forward(request, response);
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
                String name = request.getParameter("supplierName");
                String phoneNumber = request.getParameter("phoneNumber");
                String email = request.getParameter("email");
                String address = request.getParameter("address");
                String contactPerson = request.getParameter("contactPerson");

                if (!validateString(name, -1)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with name=" + name + " added successfull.";
                }
                if (popupStatus == true) {
                    int checkError = supplierDAO.add(name, phoneNumber, email, address, contactPerson);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has some error.";
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                String name = request.getParameter("name");
                String phoneNumber = request.getParameter("phoneNumber");
                String email = request.getParameter("email");
                String address = request.getParameter("address");
                String contactPerson = request.getParameter("contactPerson");
                String status = request.getParameter("status");

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateString(name, -1)
                        || !validateString(status, -1)
                        || !validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with id=" + id + " edited successfull.";
                }
                if (popupStatus == true) {
                    int checkError = supplierDAO.edit(id, name, phoneNumber, email, address, contactPerson, status);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. The object has some error.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " deleted successfull.";
                }
                if (popupStatus == true) {
                    int checkError = supplierDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has some error.";
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/supplier");

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
