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
@WebServlet(name = "OrderItemServlet", urlPatterns = {"/orderItem"})
public class OrderItemServlet extends HttpServlet {


    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();


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
            out.println("<title>Servlet OrderItemServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderItemServlet at " + request.getContextPath() + "</h1>");
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


        int orderId;


        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            orderId = -1;
        }


        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "list";
        }
//        else if (view.equalsIgnoreCase("add")) {
//            namepage = "add";
//
//            
//        }
        else if (view.equalsIgnoreCase("edit")) {
//            namepage = "edit";
            namepage = "list";


            int id;


            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }


//            request.setAttribute("menuItemsList", menuItemDAO.getAll());
            request.setAttribute("currentOrderItem", orderItemDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }


        int page;
        int totalPages = getTotalPages(orderItemDAO.countItembyOrderId(orderId));


        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }


        request.setAttribute("menuItemsList", menuItemDAO.getAll());
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentOrder", orderDAO.getElementByID(orderId));
        request.setAttribute("totalPrice", orderDAO.getTotalPricebyOrderIdFormatVND(orderId));
        request.setAttribute("orderItemsList", orderItemDAO.getAllByOrderId(orderId, page, MAX_ELEMENTS_PER_PAGE));


        request.getRequestDispatcher("/WEB-INF/orderItem/" + namepage + ".jsp").forward(request, response);
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


                Order order = orderDAO.getElementByID(orderId); //chưa check trang thai cua order moi them vao
                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);


//validate
//                if (order == null || menuItem == null
//                        || (quantity < 1) || !order.getStatus().equalsIgnoreCase("Pending")) {
//                    popupStatus = false;
//                    popupMessage = "The add action is NOT successfull. Check the information again.";
//                } else {
//                    popupMessage = "The object added successfull.";
//                }
//                if (popupStatus) {
//                    String stockError = validateIngredientAvailability(menuItem, quantity);
//                    if (stockError != null) {
//                        popupStatus = false;
//                        popupMessage = stockError;
//                    }
//                }
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


                Order order = orderDAO.getElementByID(orderId); //chưa check trang thai cua order moi them vao
                MenuItem menuItem = menuItemDAO.getElementByID(menuItemId);


//validate
//                if (order == null || menuItem == null
//                        || (quantity < 1) || (id < 1) || !order.getStatus().equalsIgnoreCase("Pending")) {
//                    popupStatus = false;
//                    popupMessage = "The edit action is NOT successfull. The input has some error.";
//                } else {
//                    popupMessage = "The object edited successfull.";
//                }
//                if (popupStatus) {
//                    String stockError = validateIngredientAvailability(menuItem, quantity);
//                    if (stockError != null) {
//                        popupStatus = false;
//                        popupMessage = stockError;
//                    }
//                }
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
            }  else if (action.equalsIgnoreCase("exportBill")) {


                Order order = orderDAO.getElementByID(orderId); //chưa check trang thai cua order moi them vao
               
//validate
                if (order == null) {
                    popupStatus = false;
                    popupMessage = "The export action is NOT successfull.";
                } else {
                    popupMessage = "The object exported successfull.";
                }
//end
                if (popupStatus == true) {
                    String checkError = orderItemDAO.exportBill(order.getOrderId());


                    if (checkError.isBlank()) {
                        popupStatus = false;
                        popupMessage = "The export action is NOT successfull. Check the information again.";
                    }
                }
            }   else if (action.equalsIgnoreCase("exportIngredient")) {


                Order order = orderDAO.getElementByID(orderId);
               
//validate
                if (order == null) {
                    popupStatus = false;
                    popupMessage = "The export action is NOT successfull.";
                } else {
                    popupMessage = "The object exported successfull.";
                }
//end
                if (popupStatus == true) {
                    String checkError = orderDAO.exportIngredientNeed(order.getOrderId());


                    if (checkError.isBlank()) {
                        popupStatus = false;
                        popupMessage = "The export action is NOT successfull. Check the information again.";
                    }
                }
            }
        }


        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/orderItem?orderId=" + orderId);
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


//    private String validateIngredientAvailability(MenuItem menuItem, int quantity) {
//        if (menuItem == null || menuItem.getRecipe() == null || menuItem.getRecipe().getItems() == null
//                || menuItem.getRecipe().getItems().isEmpty()) {
//            return "Selected menu item has no recipe configuration.";
//        }
//
//
//        for (RecipeItem recipeItem : menuItem.getRecipe().getItems()) {
//            Ingredient ingredient = ingredientDAO.getElementByID(recipeItem.getIngredientId());
//            if (ingredient == null) {
//                String name = recipeItem.getIngredientName() != null ? recipeItem.getIngredientName() : ("ID " + recipeItem.getIngredientId());
//                return "Ingredient " + name + " is unavailable.";
//            }
//
//
//            double available = Math.max(ingredient.getTotalQuantity(), 0);
//            double required = recipeItem.getQuantity() * quantity;
//
//
//            if (available + 1e-9 < required) {
//                String unit = ingredient.getUnit() != null ? ingredient.getUnit() : recipeItem.getUnit();
//                return "Not enough " + ingredient.getIngredientName() + " in stock. Required "
//                        + formatQuantity(required) + (unit != null ? " " + unit : "")
//                        + ", available " + formatQuantity(available) + ".";
//            }
//        }
//
//
//        return null;
//    }


    private String formatQuantity(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-6) {
            return String.valueOf((long) Math.rint(value));
        }
        return String.format(java.util.Locale.US, "%.2f", value);
    }
}



