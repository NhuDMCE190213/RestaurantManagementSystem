/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();
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
            out.println("<title>Servlet OrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderServlet at " + request.getContextPath() + "</h1>");
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

            request.setAttribute("currentOrder", orderDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        } else if (view.equalsIgnoreCase("list-detail")) {
            namepage = "listDetail";
        }

        int page;
        int totalPages = getTotalPages(orderDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ordersList", orderDAO.getAll(page, MAX_ELEMENTS_PER_PAGE));
        request.setAttribute("vouchersList", voucherDAO.getAllAvailable());
        request.setAttribute("tablesList", tableDAO.getAllOccupied());

        request.getRequestDispatcher("/WEB-INF/order/" + namepage + ".jsp").forward(request, response);
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
                int empId;
                int tableId;
                int voucherId;
                String paymentMethod = request.getParameter("paymentMethod");

                try {
                    empId = Integer.parseInt(request.getParameter("empId"));
                    tableId = Integer.parseInt(request.getParameter("tableId"));
                    voucherId = Integer.parseInt(request.getParameter("voucherId"));
                } catch (NumberFormatException e) {
                    empId = -1;
                    tableId = -1;
                    voucherId = -1;
                }

                Reservation reservation = reservationDAO.getElementByTableId(tableId);
                Employee emp = employeeDAO.getElementByID(empId); // check available
                Voucher voucher = voucherDAO.getById(voucherId); // check available

//validate
                if (reservation == null || emp == null || paymentMethod == null
                        || paymentMethod.isBlank() || (!paymentMethod.equals("Cash") && !paymentMethod.equals("Pay later"))) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. Check the information again.";
                } else {
                    popupMessage = "The object added successfull.";
                }
//end
                if (popupStatus == true) {
                    try {
                        int checkError = orderDAO.add(reservation.getReservationId(), emp.getEmpId(), 
                                (voucher != null) ? voucher.getVoucherId() : null, paymentMethod);
                        if (checkError >= 1) {
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("edit")) {
                int orderId;
                int empId;
                int tableId;
                int voucherId;
                String paymentMethod = request.getParameter("paymentMethod");

                try {
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                    empId = Integer.parseInt(request.getParameter("empId"));
                    tableId = Integer.parseInt(request.getParameter("tableId"));
                    voucherId = Integer.parseInt(request.getParameter("voucherId"));
                } catch (NumberFormatException e) {
                    orderId = -1;
                    empId = -1;
                    tableId = -1;
                    voucherId = -1;
                }

                Reservation reservation = reservationDAO.getElementByTableId(tableId);
                Employee emp = employeeDAO.getElementByID(empId); // check available
                Voucher voucher = voucherDAO.getById(voucherId); // check available

                System.out.println((!paymentMethod.equals("Cash") && !paymentMethod.equals("Pay later")));
                
//validate
                if (orderId <= 0
                        || reservation == null || emp == null || paymentMethod == null
                        || paymentMethod.isBlank() 
                        || (!paymentMethod.equals("Cash") && !paymentMethod.equals("Pay later"))) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object edited successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.edit(orderId, reservation.getReservationId(), emp.getEmpId(), 
                            (voucher != null) ? voucher.getVoucherId() : null, paymentMethod);

                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
            } else if (action.equalsIgnoreCase("approve")) {

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (id <= 0 || !orderDAO.validateApprove(id) || orderItemDAO.getAllByOrderId(id).size() <= 0) {
                    popupStatus = false;
                    popupMessage = "The approve action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " approved successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.approve(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The approve action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("reject")) {

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (id <= 0) {
                    popupStatus = false;
                    popupMessage = "The reject action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " rejected successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.reject(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The reject action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("complete")) {

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (id <= 0) {
                    popupStatus = false;
                    popupMessage = "The complete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " completed successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.complete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The complete action is NOT successfull. Check the information again.";
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/order");
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
}
