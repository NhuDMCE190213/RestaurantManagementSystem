/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.Constants.*;
import dao.CategoryDAO;
import dao.IngredientDAO;
import dao.MenuItemDAO;
import dao.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.MenuItem;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "RecipeServlet", urlPatterns = {"/recipe"})
public class RecipeServlet extends HttpServlet {

    private final RecipeDAO recipeDAO = new RecipeDAO();       // recipe items
    private final MenuItemDAO menuItemDAO = new MenuItemDAO(); // menu items
    private final IngredientDAO ingDAO = new IngredientDAO();
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RecipeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RecipeServlet at " + request.getContextPath() + "</h1>");
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
            request.setAttribute("currentMenuItem", menuItemDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        } else if (view.equalsIgnoreCase("view")) {
            namepage = "view";
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }
            request.setAttribute("currentMenuItem", menuItemDAO.getElementByID(id));
        }

        int page;
        int totalPages = getTotalPages(menuItemDAO.countItem());
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("menuItems", menuItemDAO.getAll(page, MAX_ELEMENTS_PER_PAGE));
        request.setAttribute("ingredients", ingDAO.getAll());
        request.setAttribute("categories", categoryDAO.getAll()); // if JSP needs categories
        request.getRequestDispatcher("/WEB-INF/recipe/" + namepage + ".jsp").forward(request, response);
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
            // --- menu item level actions ---
            if (action.equalsIgnoreCase("add")) {
                // expected params: category_id, item_name, image_url, price, description
                String itemName = request.getParameter("item_name");
                String imageUrl = request.getParameter("image_url");
                String description = request.getParameter("description");
                int price = 0;
                int categoryId;
                try {
                    categoryId = Integer.parseInt(request.getParameter("category_id"));
                } catch (NumberFormatException ex) {
                    categoryId = -1;
                }
                try {
                    price = Integer.parseInt(request.getParameter("price"));
                } catch (NumberFormatException ex) {
                    price = 0;
                }

                if (!isValidString(itemName, -1) || categoryId <= 0) {
                    popupStatus = false;
                    popupMessage = "Add menu item failed. Invalid input.";
                } else {
                    Category cat = categoryDAO.getElementByID(categoryId);
                    MenuItem item = new MenuItem(0, cat, itemName, imageUrl, price, description, "Active");
                    int check = menuItemDAO.add(item);
                    if (check >= 1) {
                        popupMessage = "Menu item added successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Add menu item failed. Error code: " + check;
                    }
                }
            } else if (action.equalsIgnoreCase("edit")) {
                // expected params: id, category_id, item_name, image_url, price, description, status
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException ex) {
                    id = -1;
                }
                String itemName = request.getParameter("item_name");
                String imageUrl = request.getParameter("image_url");
                String description = request.getParameter("description");
                String status = request.getParameter("status");
                int price = 0;
                int categoryId;
                try {
                    categoryId = Integer.parseInt(request.getParameter("category_id"));
                } catch (NumberFormatException ex) {
                    categoryId = -1;
                }
                try {
                    price = Integer.parseInt(request.getParameter("price"));
                } catch (NumberFormatException ex) {
                    price = 0;
                }

                if (!isValidInteger(id, false, false, true) || !isValidString(itemName, -1) || categoryId <= 0 || !isValidString(status, -1)) {
                    popupStatus = false;
                    popupMessage = "Edit menu item failed. Invalid input.";
                } else {
                    Category cat = categoryDAO.getElementByID(categoryId);
                    MenuItem item = new MenuItem(id, cat, itemName, imageUrl, price, description, status);
                    int check = menuItemDAO.edit(item);
                    if (check >= 1) {
                        popupMessage = "Menu item edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Edit menu item failed. Error code: " + check;
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException ex) {
                    id = -1;
                }
                if (!isValidInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Delete menu item failed. Invalid id.";
                } else {
                    int check = menuItemDAO.delete(id);
                    if (check >= 1) {
                        popupMessage = "Menu item deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Delete menu item failed. Error code: " + check;
                    }
                }
            } // --- item-level actions handled by RecipeDAO (recipe items) ---
            else if (action.equalsIgnoreCase("add_item")) {
                int menuItemId, ingredientId;
                double quantity = 0;
                String unit = request.getParameter("unit");
                String note = request.getParameter("note");
                try {
                    menuItemId = Integer.parseInt(request.getParameter("menu_item_id"));
                } catch (NumberFormatException e) {
                    menuItemId = -1;
                }
                try {
                    ingredientId = Integer.parseInt(request.getParameter("ingredient_id"));
                } catch (NumberFormatException e) {
                    ingredientId = -1;
                }
                try {
                    quantity = Double.parseDouble(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    quantity = -1;
                }
                if (!isValidInteger(menuItemId, false, false, true)
                        || !isValidInteger(ingredientId, false, false, true)
                        || quantity <= 0) {
                    popupStatus = false;
                    popupMessage = "Add item failed. Input invalid.";
                } else {
                    int checkError = recipeDAO.addItem(menuItemId, ingredientId, quantity, unit, note);
                    if (checkError >= 1) {
                        popupMessage = "Item added to menu item id: " + menuItemId + " successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Add item action failed. Error: " + getSqlErrorCode(checkError);
                    }
                }
            } else if (action.equalsIgnoreCase("edit_item")) {
                int recipeItemId, ingredientId;
                double quantity = 0;
                String unit = request.getParameter("unit");
                String note = request.getParameter("note");
                String status = "Active";

                try {
                    recipeItemId = Integer.parseInt(request.getParameter("recipe_item_id"));
                } catch (NumberFormatException e) {
                    recipeItemId = -1;
                }
                try {
                    ingredientId = Integer.parseInt(request.getParameter("ingredient_id"));
                } catch (NumberFormatException e) {
                    ingredientId = -1;
                }
                try {
                    quantity = Double.parseDouble(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    quantity = -1;
                }
                if (!isValidInteger(recipeItemId, false, false, true)
                        || !isValidInteger(ingredientId, false, false, true)
                        || quantity <= 0) {
                    popupStatus = false;
                    popupMessage = "Edit item failed. Input invalid.";
                } else {
                    int checkError = recipeDAO.editItem(recipeItemId, ingredientId, quantity, unit, note, status);
                    if (checkError >= 1) {
                        popupMessage = "Item edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Edit item action failed. Error: " + getSqlErrorCode(checkError);
                    }
                }
            } else if (action.equalsIgnoreCase("delete_item")) {
                int recipeItemId;
                try {
                    recipeItemId = Integer.parseInt(request.getParameter("recipe_item_id"));
                } catch (NumberFormatException e) {
                    recipeItemId = -1;
                }
                if (!isValidInteger(recipeItemId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Delete item failed.";
                } else {
                    int checkError = recipeDAO.deleteItem(recipeItemId);
                    if (checkError >= 1) {
                        popupMessage = "Item deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "Delete item action failed. Error: " + getSqlErrorCode(checkError);
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);

        // Redirect logic
        if ("add_item".equalsIgnoreCase(action)
                || "edit_item".equalsIgnoreCase(action)
                || "delete_item".equalsIgnoreCase(action)) {

            int menuItemId;
            try {
                menuItemId = Integer.parseInt(request.getParameter("menu_item_id"));
            } catch (NumberFormatException e) {
                menuItemId = -1;
            }

            if (menuItemId > 0) {
                response.sendRedirect(request.getContextPath() + "/recipe?view=view&id=" + menuItemId);
            } else {
                response.sendRedirect(request.getContextPath() + "/recipe");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/recipe");
        }
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
        if (temp_code + DUPLICATE_KEY == 0) {
            return "DUPLICATE_KEY";
        } else if (temp_code + FOREIGN_KEY_VIOLATION == 0) {
            return "FOREIGN_KEY_VIOLATION";
        } else if (temp_code + NULL_INSERT_VIOLATION == 0) {
            return "NULL_INSERT_VIOLATION";
        } else if (temp_code + UNIQUE_INDEX == 0) {
            return "DUPLICATE_UNIQUE";
        }
        return "Unknown Error Code:" + temp_code;
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
