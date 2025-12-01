/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ReservationDAO;
import dao.TableDAO;
import dao.VoucherDAO;
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
import model.MenuItem;
import model.Order;
import model.Reservation;
import model.Voucher;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "MyOrderServlet", urlPatterns = {"/myOrder"})
public class MyOrderServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final TableDAO tableDAO = new TableDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

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
            out.println("<title>Servlet MyOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyOrderServlet at " + request.getContextPath() + "</h1>");
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
        Customer customer = null;
        String namepage = "";
        String view = request.getParameter("view");
        // check tam
        try {
            customer = (Customer) request.getSession(false).getAttribute("customerSession");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // check tam

        int page;
        int totalPages = getTotalPages(orderDAO.countItemByCustomer(customer.getCustomerId()));

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "list";
            totalPages = getTotalPages(orderDAO.countItemByCustomer(customer.getCustomerId()));
        } else if (view.equalsIgnoreCase("addOrder")) {
            namepage = "add";

            request.setAttribute("reservationsList", reservationDAO.getAllByCustomerId(customer.getCustomerId()));
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("vouchersList", voucherDAO.getAllAvailable());
        } else {

            int myOrder;

            try {
                myOrder = Integer.parseInt(request.getParameter("orderId"));
            } catch (NumberFormatException e) {
                myOrder = -1;
            }

            if (view.equalsIgnoreCase("detail")) {
                namepage = "detail";

                //validate
                Order order = orderDAO.getElementByID(myOrder);
                if ((order == null) || order.getReservation().getCustomer().getCustomerId() != customer.getCustomerId()) {
                    namepage = "detail";
                }
                //end
            } else if (view.equalsIgnoreCase("edit")) {
//            namepage = "edit";
                namepage = "detail";

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }
                request.setAttribute("currentOrderItem", orderItemDAO.getElementByID(id));
            }

            request.setAttribute("currentOrder", orderDAO.getElementByID(myOrder));
            request.setAttribute("orderItemsList", orderItemDAO.getAllByOrderId(myOrder, page, MAX_ELEMENTS_PER_PAGE));
            request.setAttribute("totalPrice", orderDAO.getTotalPricebyOrderIdFormatVND(myOrder));
            request.setAttribute("menuItemsList", menuItemDAO.getAll());

            totalPages = getTotalPages(orderItemDAO.countItembyOrderId(myOrder));
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ordersList", orderDAO.getAllByCustomerId(customer.getCustomerId(), page, MAX_ELEMENTS_PER_PAGE));

        request.getRequestDispatcher("/WEB-INF/myOrder/" + namepage + ".jsp").forward(request, response);
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

        Customer customer = null;

        int orderId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            orderId = -1;
        }

        // check tam
        try {
            customer = (Customer) request.getSession(false).getAttribute("customerSession");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // check tam

        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("addOrder")) {
                int reservationId;
                int voucherId;
                String paymentMethod = request.getParameter("paymentMethod");

                try {
                    voucherId = Integer.parseInt(request.getParameter("voucherId"));
                    reservationId = Integer.parseInt(request.getParameter("reservationId"));
                } catch (NumberFormatException e) {
                    voucherId = -1;
                    reservationId = -1;
                }
                
                Reservation reservation = reservationDAO.getElementByID(reservationId);
                Voucher voucher = voucherDAO.getById(voucherId); // check available

//validate
                if (paymentMethod == null || reservation == null
                        || paymentMethod.isBlank() || (!paymentMethod.equals("Cash") && !paymentMethod.equals("Pay later"))) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. Check the information again.";
                } else {
                    popupMessage = "The object added successfull.";
                }
//end
                if (popupStatus == true) {
                    try {
                        int checkError = orderDAO.add(reservation.getReservationId(), null, 
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
            } else if (action.equalsIgnoreCase("add")) {
                int menuItemId;
                int quantity;

                try {
                    menuItemId = Integer.parseInt(request.getParameter("menuItemId"));
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    menuItemId = -1;
                    quantity = -1;
                }

                Order order = orderDAO.getElementByID(orderId);
                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

//validate
                if (order == null || menuItem == null
                        || (quantity < 1) || !order.getStatus().equalsIgnoreCase("Pending")) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. Check the information again.";
                } else {
                    popupMessage = "The object added successfull.";
                }
//end
                if (popupStatus == true) {
                    try {
                        int checkError = orderItemDAO.add(order.getOrderId(), menuItem.getMenuItemId(), menuItem.getPrice(), quantity);
                        if (checkError >= 1) {
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("cancel")) {

                Order order = orderDAO.getElementByID(orderId);

//validate
                if (orderId <= 0 || order == null
                        || order.getReservation().getCustomer().getCustomerId() != customer.getCustomerId()
                        || !order.getStatus().equalsIgnoreCase("Pending")) {
                    popupStatus = false;
                    popupMessage = "The cancel action is NOT successfull.";
                } else {
                    popupMessage = "The order cancelled successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.cancel(orderId);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The cancle action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                int menuItemId;
                int quantity;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    menuItemId = Integer.parseInt(request.getParameter("menuItemId"));
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    id = -1;
                    menuItemId = -1;
                    quantity = -1;
                }

                Order order = orderDAO.getElementByID(orderId); //chưa check trang thai cua order moi them vao
                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

//validate
                if (order == null || menuItem == null
                        || (quantity < 1) || (id < 1) || !order.getStatus().equalsIgnoreCase("Pending")) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object edited successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderItemDAO.edit(id, order.getOrderId(), menuItem.getMenuItemId(), menuItem.getPrice(), quantity);

                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                Order order = orderDAO.getElementByID(orderId); //chưa check trang thai cua order moi them vao

//validate
                if (id <= 0 || order == null || !order.getStatus().equalsIgnoreCase("Pending")) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " deleted successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderItemDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. Check the information again.";
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/myOrder");
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
