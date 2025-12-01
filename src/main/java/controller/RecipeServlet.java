/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.Constants.*;
import dao.IngredientDAO;
import dao.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import model.RecipeItem;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "RecipeServlet", urlPatterns = {"/recipe"})
public class RecipeServlet extends HttpServlet {

    RecipeDAO recipeDAO = new RecipeDAO();
    IngredientDAO ingDAO = new IngredientDAO();

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
            request.setAttribute("currentRecipe", recipeDAO.getElementByID(id));
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
            request.setAttribute("currentRecipe", recipeDAO.getElementByID(id));
        }
//        else if (view.equalsIgnoreCase("add-item")) {
//            namepage = "add-item";
//            int id;
//            try {
//                id = Integer.parseInt(request.getParameter("id"));
//            } catch (NumberFormatException e) {
//                id = -1;
//            }
//            request.setAttribute("currentRecipe", recipeDAO.getElementByID(id));
//        } else if (view.equalsIgnoreCase("edit-item")) {
//            namepage = "edit-item";
//            int recipeItemId;
//            try {
//                recipeItemId = Integer.parseInt(request.getParameter("recipe_item_id"));
//            } catch (NumberFormatException e) {
//                recipeItemId = -1;
//            }
//            request.setAttribute("currentItem", recipeDAO.getRecipeItemById(recipeItemId));
//        }
        int page;
        int totalPages = getTotalPages(recipeDAO.countItem());
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recipesList", recipeDAO.getAll(page, keyword));
        request.setAttribute("ingredients", ingDAO.getAll());
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
            if (action.equalsIgnoreCase("add")) {
                String recipeName = request.getParameter("recipe_name");
                if (!isValidString(recipeName, -1)) {
                    popupStatus = false;
                    popupMessage = "Add recipe failed. Invalid input.";
                } else {
                    int check = recipeDAO.add(recipeName);
                    if (check >= 1) {
                        popupMessage = "Recipe added successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has " + getSqlErrorCode(check) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                String recipeName;
                String status;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }
                recipeName = request.getParameter("recipe_name");
                status = request.getParameter("status");
                if (!isValidInteger(id, false, false, true) || !isValidString(recipeName, -1) || !isValidString(status, -1)) {
                    popupStatus = false;
                    popupMessage = "Edit recipe failed. Invalid input.";
                } else {
                    int check = recipeDAO.edit(id, recipeName, status);
                    if (check >= 1) {
                        popupMessage = "Recipe edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. The object has " + getSqlErrorCode(check) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }
                if (!isValidInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Delete recipe failed.";
                } else {
                    int check = recipeDAO.delete(id);
                    if (check >= 1) {
                        popupMessage = "Recipe deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has " + getSqlErrorCode(check) + " error.";
                    }
                }
            } // item-level actions: add_item, edit_item, delete_item
            else if (action.equalsIgnoreCase("add_item")) {
                int recipeId, ingredientId;
                double quantity = 0;
                String unit = request.getParameter("unit");
                String note = request.getParameter("note");
                try {
                    recipeId = Integer.parseInt(request.getParameter("recipe_id"));
                } catch (NumberFormatException e) {
                    recipeId = -1;
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
                if (!isValidInteger(recipeId, false, false, true) 
                        || !isValidInteger(ingredientId, false, false, true)
                        || quantity <= 0) {
                    popupStatus = false;
                    popupMessage = "Add item failed. Input invalid.";
                } else {
                    int checkError = recipeDAO.addItem(recipeId, ingredientId, quantity, unit, note);
                    if (checkError >= 1) {
                        popupMessage = "Item added to recipe " + recipeId + " successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add item action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
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
                
//                RecipeItem existing = recipeDAO.getRecipeItemById(recipeItemId);
//                if (existing != null && existing.getStatus() != null) {
//                    status = existing.getStatus();
//                }

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
                        popupMessage = "The edit item action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
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
                        popupMessage = "The delete item action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        if ("add_item".equalsIgnoreCase(action)
                || "edit_item".equalsIgnoreCase(action)
                || "delete_item".equalsIgnoreCase(action)) {

            int recipeId;
            try {
                recipeId = Integer.parseInt(request.getParameter("recipe_id"));
            } catch (NumberFormatException e) {
                recipeId = -1;
            }

            if (recipeId > 0) {
                response.sendRedirect(request.getContextPath() + "/recipe?view=view&id=" + recipeId);
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
