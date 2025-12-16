/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import dao.MenuItemDAO;
import dao.OrderItemDAO;
import dao.ReservationDAO;
import dao.VoucherDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Customer;
import model.MenuItem;
import model.OrderItem;
import model.Reservation;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
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

        int reservationId;// khi khong tim thay thi khong cho add
        try {
            reservationId = Integer.parseInt(request.getParameter("reservationId"));
        } catch (NumberFormatException ex) {
            reservationId = -1;
        }

        Reservation currentReservation = reservationDAO.getElementByID(reservationId);
        
        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "list";
        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
        } else if (view.equalsIgnoreCase("edit")) {
            request.setAttribute("currentOrderItem", orderItemDAO.getAllByReservationId(reservationId));
            namepage = "edit";
        }

        if (currentReservation != null) {
            //Process list to map
            List<OrderItem> orderItems = orderItemDAO.getAllByReservationId(currentReservation.getReservationId());
            Map<String, Map<String, Integer>> orderItemsMap = new HashMap<>();
            Map<String, Map<String, Integer>> orderItemsIdMap = new HashMap<>();

            for (OrderItem orderItem : orderItems) {
                String key = orderItem.getMenuItem().getMenuItemId() + "_" + orderItem.getUnitPrice();
                String status = orderItem.getStatus();
                int quantity = orderItem.getQuantity();
                int id = orderItem.getOrderItemId();

                if (quantity <= 0) {
                    continue;
                }

                if (!orderItemsMap.containsKey(key)) {
                    orderItemsMap.put(key, new HashMap<>());
                    orderItemsIdMap.put(key, new HashMap<>());
                }

                orderItemsMap.get(key).put(status, quantity);
                orderItemsIdMap.get(key).put(status, id);
            }
            // end

            request.setAttribute("orderItemsMap", orderItemsMap);
            request.setAttribute("orderItemsIdMap", orderItemsIdMap);
        }
        
        
                
        request.setAttribute("orderItemForMapList", orderItemDAO.getAllByReservationIdForMap(reservationId));
        request.setAttribute("orderItemsList", orderItemDAO.getAllByReservationId(reservationId));
        request.setAttribute("categoryList", categoryDAO.getAll());
        request.setAttribute("itemsList", menuItemDAO.getAll());
        request.setAttribute("vouchersList", voucherDAO.getAllAvailable());
        request.setAttribute("currentReservation", currentReservation);
        
        long subTotal = orderItemDAO.getTotalPrice(reservationId);
        long vat = subTotal * 10 / 100;
        
        request.setAttribute("subTotal", orderItemDAO.getFormatVND(subTotal));
        request.setAttribute("vat", orderItemDAO.getFormatVND(vat));

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
                edit(request, currentReservation);
            } else if (action.equalsIgnoreCase("complete")) {
                complete(request, currentReservation);
            } else if (action.equalsIgnoreCase("cook")) {
                cook(request, currentReservation);
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/order?reservationId=" + reservationId);
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

    private void add(HttpServletRequest request, Reservation reservation) {

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
                popupStatus = false;
                popupMessage = "The add action is NOT successfull.";
                if (reservation == null) {
                    popupMessage += " The reservation is not exist. Check information again.";
                } else if (menuItem == null) {
                    popupMessage += " The item is not exist. Check information again.";
                } else if (!(reservation.getStatus().equalsIgnoreCase("Pending")
                        || reservation.getStatus().equalsIgnoreCase("Approved")
                        || reservation.getStatus().equalsIgnoreCase("Serving"))) {
                    popupMessage += " The reservation is not pending or serving. Check information again.";
                } else {
                    popupStatus = true;
                    popupMessage = "The add action is successfull.";
                }
                //end

                if (popupStatus == true) {
                    if (quantity > 0) {
                        orderItems.add(new OrderItem(menuItemId, reservation, menuItem, menuItem.getPrice(), quantity, "Pending"));
                    }
                } else {
                    break;
                }
            }

            if (popupStatus == true) {
                int checkError2 = orderItemDAO.add(reservation.getReservationId(), orderItems);

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

    private void edit(HttpServletRequest request, Reservation reservation) {

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
                popupStatus = false;
                popupMessage = "The edit action is NOT successfull.";
                if (reservation == null) {
                    popupMessage += " The reservation is not exist. Check information again.";
                } else if (menuItem == null) {
                    popupMessage += " The item is not exist. Check information again.";
                } else if (!(reservation.getStatus().equalsIgnoreCase("Pending")
                        || reservation.getStatus().equalsIgnoreCase("Approved")
                        || reservation.getStatus().equalsIgnoreCase("Serving"))) {
                    popupMessage += " The reservation is not pending or serving. Check information again.";
                } else {
                    popupStatus = true;
                    popupMessage = "The edit action is successfull.";
                }
                //end

                if (popupStatus == true) {
                    orderItems.add(new OrderItem(menuItemId, reservation, menuItem, menuItem.getPrice(), quantity, "Pending"));
                } else {
                    break;
                }
            }

            if (popupStatus == true) {
                int checkError2 = orderItemDAO.editQuantityList(reservation.getReservationId(), orderItems);

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

    private void complete(HttpServletRequest request, Reservation reservation) {
        int orderItemId;

        try {
            orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
        } catch (NumberFormatException e) {
            orderItemId = -1;
        }

        OrderItem orderItem = orderItemDAO.getElementByID(orderItemId);

        //validate
        popupStatus = false;
        popupMessage = "The complete action is NOT successfull.";
        if (reservation == null) {
            popupMessage += " The reservation is not exist. Check information again.";
        } else if (orderItem == null) {
            popupMessage += " The order item is not exist. Check information again.";
        } else if (!orderItem.getStatus().equalsIgnoreCase("Cooking")) {
            popupMessage += " The order item is not cooking. Check information again.";
        } else if (!(reservation.getStatus().equalsIgnoreCase("Pending")
                || reservation.getStatus().equalsIgnoreCase("Approved")
                || reservation.getStatus().equalsIgnoreCase("Serving"))) {
            popupMessage += " The reservation is not pending or serving. Check information again.";
        } else {
            popupStatus = true;
            popupMessage = "The complete action is successfull.";
        }
        //end

        if (popupStatus == true) {
            int checkError = orderItemDAO.complete(reservation.getReservationId(), orderItemId, orderItem.getMenuItem().getMenuItemId(), orderItem.getMenuItem().getPrice());

            if (checkError >= 1) {
            } else {
                popupStatus = false;
                popupMessage = "The complete action is NOT successfull. Check the information menu item again.";
            }
        }
    }

    private void cook(HttpServletRequest request, Reservation reservation) {
        int orderItemId;

        try {
            orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
        } catch (NumberFormatException e) {
            orderItemId = -1;
        }

        OrderItem orderItem = orderItemDAO.getElementByID(orderItemId);

        //validate
        popupStatus = false;
        popupMessage = "The cook action is NOT successfull.";
        if (reservation == null) {
            popupMessage += " The reservation is not exist. Check information again.";
        } else if (orderItem == null) {
            popupMessage += " The order item is not exist. Check information again.";
        } else if (!orderItem.getStatus().equalsIgnoreCase("Pending")) {
            popupMessage += " The order item is not pending. Check information again.";
        } else if (!(reservation.getStatus().equalsIgnoreCase("Pending")
                || reservation.getStatus().equalsIgnoreCase("Approved")
                || reservation.getStatus().equalsIgnoreCase("Serving"))) {
            popupMessage += " The reservation is not pending or serving. Check information again.";
        } else {
            popupStatus = true;
            popupMessage = "The cook action is successfull.";
        }
        //end

        if (popupStatus == true) {
            int checkError = orderItemDAO.cook(reservation.getReservationId(), orderItemId, orderItem.getMenuItem().getMenuItemId(), orderItem.getMenuItem().getPrice());

            if (checkError >= 1) {
            } else {
                popupStatus = false;
                popupMessage = "The cook action is NOT successfull. Check the information menu item again.";
            }
        }
    }
}
