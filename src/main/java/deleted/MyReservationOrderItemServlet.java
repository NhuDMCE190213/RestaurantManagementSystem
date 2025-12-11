/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package deleted;

import dao.CategoryDAO;
import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MenuItem;
import model.Order;
import model.OrderItem;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "MyReservationOrderItemServlet", urlPatterns = {"/myReservationOrderItem"})
public class MyReservationOrderItemServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

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
        String namepage = "";
        String view = request.getParameter("view");

        int orderId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException exception) {
            orderId = 0;
        }

        Order currentOrder = orderDAO.getElementByID(orderId);

        int page;
        int totalPages;

        if (currentOrder != null) {
            totalPages = getTotalPages(orderItemDAO.countItembyOrderId(currentOrder.getOrderId()));
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

        } else if (view.equalsIgnoreCase("edit")) {
            namepage = "list";

            int orderItemId;

            try {
                orderItemId = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException exception) {
                orderItemId = -1;
            }

            OrderItem currentOrderItem = orderItemDAO.getElementByID(orderItemId);

            request.setAttribute("currentOrderItem", currentOrderItem);
        }

        if (currentOrder != null) {
            request.setAttribute("orderItemsList", orderItemDAO.getAllByOrderId(currentOrder.getOrderId(), page, MAX_ELEMENTS_PER_PAGE));
        }

        request.setAttribute("menuItemsList", menuItemDAO.getAll());
        request.setAttribute("categoryList", categoryDAO.getAll());
        request.setAttribute("currentOrder", currentOrder);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalPrice", orderDAO.getTotalPricebyOrderIdFormatVND(orderId));

        request.getRequestDispatcher("/WEB-INF/myReservationOrderItem/" + namepage + ".jsp").forward(request, response);
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

        int orderId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            orderId = -1;
        }

        Order order = orderDAO.getElementByID(orderId);

        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("add")) {
                int menuItemId;
                int quantity;

                try {
                    menuItemId = Integer.parseInt(request.getParameter("menuItemId"));
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    menuItemId = -1;
                    quantity = -1;
                }

                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

//validate
                popupStatus = false;
                if (order == null || menuItem == null) {
                    popupMessage = "The add action is NOT successfull. The menu item or order is incorrect.";
                } else if (quantity < 1) {
                    popupMessage = "The add action is NOT successfull. The quantity is below 1";
                } else if (!order.getStatus().equalsIgnoreCase("Pending")) {
                    popupMessage = "The add action is NOT successfull. The status of order is not Pending";
                } else {
                    popupStatus = true;
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

                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);

//validate
                popupStatus = false;
                if (order == null || menuItem == null || (id < 1)) {
                    popupMessage = "The edit action is NOT successfull. The order or menu item or order item is wrong.";
                } else if (quantity < 1) {
                    popupMessage = "The edit action is NOT successfull. The quantity is below 1";
                } else if (!order.getStatus().equalsIgnoreCase("Pending")) {
                    popupMessage = "The edit action is NOT successfull. The status of order is not Pending";
                } else {
                    popupStatus = true;
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

//validate
                popupStatus = false;
                if (id <= 0 || order == null) {
                    popupMessage = "The delete action is NOT successfull. The order item or order is wrong.";
                } else if (!order.getStatus().equalsIgnoreCase("Pending")) {
                    popupMessage = "The delete action is NOT successfull. The status of order is not Pending";
                } else {
                    popupStatus = true;
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
        response.sendRedirect(request.getContextPath() + "/myReservationOrderItem" + "?orderId=" + orderId);
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
