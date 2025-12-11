/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ReservationDAO;
import dao.VoucherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Reservation;
import model.Voucher;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "MyReservationOrderServlet", urlPatterns = {"/myReservationOrder"})
public class MyReservationOrderServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    private boolean popupStatus = true;
    private String popupMessage = "";

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

        int reservationId;// khi khong tim thay thi khong cho add
        try {
            reservationId = Integer.parseInt(request.getParameter("reservationId"));
        } catch (NumberFormatException ex) {
            reservationId = 0;
        }
        Reservation currentReservation = reservationDAO.getElementByID(reservationId);

        int page;
        int totalPages;

        if (currentReservation != null) {
            totalPages = getTotalPages(orderDAO.countItemByCustomerAndReservation(customer.getCustomerId(),
                    currentReservation.getReservationId()));
        } else {
            totalPages = 0;
        }

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "list";

            if (currentReservation != null) {
                request.setAttribute("ordersList", orderDAO.getAllByCustomerIdAndReservationId(customer.getCustomerId(),
                        currentReservation.getReservationId(), page, MAX_ELEMENTS_PER_PAGE));
            }
        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
        } else if (view.equalsIgnoreCase("edit")) {
            int orderId;

            try {
                orderId = Integer.parseInt(request.getParameter("orderId"));
            } catch (NumberFormatException ex) {
                orderId = -1;
            }

            request.setAttribute("currentOrder", orderDAO.getElementByID(orderId));
            request.setAttribute("currentOrderItem", orderItemDAO.getAllByOrderId(orderId));
            namepage = "edit";
        }

        request.setAttribute("categoryList", categoryDAO.getAll());
        request.setAttribute("itemsList", menuItemDAO.getAll());
        request.setAttribute("vouchersList", voucherDAO.getAllAvailable());
        request.setAttribute("currentReservation", currentReservation);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/myReservationOrder/" + namepage + ".jsp").forward(request, response);
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

        int orderId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            orderId = -1;
        }

        int reservationId;// khi khong tim thay thi khong cho add
        try {
            reservationId = Integer.parseInt(request.getParameter("reservationId"));
        } catch (NumberFormatException ex) {
            reservationId = 0;
        }
        Reservation currentReservation = reservationDAO.getElementByID(reservationId);

        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("add")) {
                add(request, currentReservation);
            } else if (action.equalsIgnoreCase("edit")) {
                edit(request, currentReservation, orderId);
            } else if (action.equalsIgnoreCase("cancel")) {
                cancel(orderId);
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/myReservationOrder?reservationId=" + reservationId);
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

    private void add(HttpServletRequest request, Reservation currentReservation) {
        int voucherId;
        String paymentMethod = request.getParameter("paymentMethod");

        try {
            voucherId = Integer.parseInt(request.getParameter("voucherId"));
        } catch (NumberFormatException e) {
            voucherId = -1;
        }

        Voucher voucher = voucherDAO.getById(voucherId);

//validate
        popupStatus = false;
        if (paymentMethod == null || paymentMethod.isBlank()) {
            popupMessage = "The add action is NOT successfull. The payment method is blank.";
        } else if (currentReservation == null) {
            popupMessage = "The add action is NOT successfull. The reservation id is wrong.";
        } else {
            popupStatus = true;
            popupMessage = "The object added successfull.";
        }
//end
        if (popupStatus == true) {
            try {
                int checkError = orderDAO.add(currentReservation.getReservationId(), null,
                        (voucher != null) ? voucher.getVoucherId() : null, paymentMethod);

                if (checkError >= 1) {
// add order item
                    Order order = orderDAO.getNewestElementByResevationId(currentReservation.getReservationId());

                    String[] itemIds = request.getParameterValues("itemIdList");
                    String[] quantities = request.getParameterValues("quantityList");

                    List<OrderItem> orderItems = new ArrayList<>();

                    if (itemIds != null && quantities != null) {
                        for (int i = 0; i < itemIds.length; i++) {

                            int menuItemId;
                            int quantity;

                            try {
                                menuItemId = Integer.parseInt(itemIds[i]);
                                quantity = Integer.parseInt(quantities[i]);
                            } catch (NumberFormatException e) {
                                menuItemId = -1;
                                quantity = -1;
                            }

                            MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

                            //validate
                            if (order == null || menuItem == null
                                    || !order.getStatus().equalsIgnoreCase("Pending")) {
                                popupStatus = false;
                                popupMessage = "The add action is NOT successfull. The item is wrong. Check information again";
                            }
                            //end

                            if (popupStatus == true) {
                                if (quantity > 0) {
                                    orderItems.add(new OrderItem(menuItemId, null, menuItem, menuItem.getPrice(), quantity));
                                }
                            } else {
                                popupStatus = false;
                                popupMessage = "The add action is NOT successfull. The item is wrong.";
                                break;
                            }
                        }

                        if (popupStatus == true) {
                            int checkError2 = orderItemDAO.add(order.getOrderId(), orderItems);

                            if (checkError2 >= 1) {
                            } else {
                                popupStatus = false;
                                popupMessage = "The add action is NOT successfull. Check the information menu item again.";
                            }
                        }
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. Check the information menu item again.";
                    }
                }
//end

            } catch (Exception e) {
                popupStatus = false;
                popupMessage = "The add action is NOT successfull. Check the information order again.";
            }
        }
    }

    private void edit(HttpServletRequest request, Reservation currentReservation, int orderId) {
        int voucherId;
        String paymentMethod = request.getParameter("paymentMethod");

        try {
            voucherId = Integer.parseInt(request.getParameter("voucherId"));
        } catch (NumberFormatException e) {
            voucherId = -1;
        }

        Voucher voucher = voucherDAO.getById(voucherId);

//validate
        popupStatus = false;
        if (paymentMethod == null || paymentMethod.isBlank()) {
            popupMessage = "The edit action is NOT successfull. The payment method is blank.";
        } else if (currentReservation == null) {
            popupMessage = "The edit action is NOT successfull. The reservation id is wrong.";
        } else if (!orderDAO.getElementByID(orderId).getStatus().equalsIgnoreCase("pending")) {
            popupMessage = "The edit action is NOT successfull. The order is not pending.";
        } else {
            popupStatus = true;
            popupMessage = "The object edited successfull.";
        }
//end
        if (popupStatus == true) {
            try {
                int checkError = orderDAO.edit(orderId, currentReservation.getReservationId(), null, (voucher != null) ? voucher.getVoucherId() : null, paymentMethod);

                if (checkError >= 1) {
// add order item
                    Order order = orderDAO.getNewestElementByResevationId(currentReservation.getReservationId());

                    String[] itemIds = request.getParameterValues("itemIdList");
                    String[] quantities = request.getParameterValues("quantityList");

                    List<OrderItem> orderItems = new ArrayList<>();

                    if (itemIds != null && quantities != null) {
                        for (int i = 0; i < itemIds.length; i++) {

                            int menuItemId;
                            int quantity;

                            try {
                                menuItemId = Integer.parseInt(itemIds[i]);
                                quantity = Integer.parseInt(quantities[i]);
                            } catch (NumberFormatException e) {
                                menuItemId = -1;
                                quantity = -1;
                            }

                            MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

                            //validate
                            if (order == null || menuItem == null
                                    || !order.getStatus().equalsIgnoreCase("Pending")) {
                                popupStatus = false;
                                popupMessage = "The edit action is NOT successfull. The item is wrong. Check information again";
                            }
                            //end

                            if (popupStatus == true) {
                                orderItems.add(new OrderItem(menuItemId, null, menuItem, menuItem.getPrice(), quantity));
                            } else {
                                popupStatus = false;
                                popupMessage = "The edit action is NOT successfull. The item is wrong.";
                                break;
                            }
                        }

                        if (popupStatus == true) {
                            int checkError2 = orderItemDAO.edit(order.getOrderId(), orderItems);

                            if (checkError2 >= 1) {
                            } else {
                                popupStatus = false;
                                popupMessage = "The edit action is NOT successfull. Check the information menu item again.";
                            }
                        }
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. Check the information menu item again.";
                    }
                }
//end

            } catch (Exception e) {
                popupStatus = false;
                popupMessage = "The edit action is NOT successfull. Check the information order again.";
            }
        }
    }

    private void cancel(int orderId) {
        Order order = orderDAO.getElementByID(orderId);

//validate
        popupStatus = false;
        if (orderId <= 0 || order == null) {
            popupMessage = "The cancel action is NOT successfull. The order is not exist";
        } else if (!order.getStatus().equalsIgnoreCase("Pending")) {
            popupMessage = "The cancel action is NOT successfull. The status of order is not Pending";
        } else {
            popupStatus = true;
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
    }

}
