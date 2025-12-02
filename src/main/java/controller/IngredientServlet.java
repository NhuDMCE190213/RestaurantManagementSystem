/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ImportDAO;
import dao.IngredientDAO;
import dao.TypeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Ingredient;

/**
 *
 * @author TruongBinhTrong
 */
@WebServlet(name = "IngredientServlet", urlPatterns = {"/ingredient"})
public class IngredientServlet extends HttpServlet {

    private static final int MAX_ELEMENTS_PER_PAGE = 10;
    private static final int DUPLICATE_KEY = 2627;
    private static final int FOREIGN_KEY_VIOLATION = 547;
    private static final int NULL_INSERT_VIOLATION = 515;

    IngredientDAO ingredientDAO = new IngredientDAO();
    TypeDAO typeDAO = new TypeDAO();
    ImportDAO importDAO = new ImportDAO();

    public boolean validateString(String str, int limitLength) {

        if (limitLength < 0) {
            limitLength = Integer.MAX_VALUE;
        }

        return !(str == null || str.isEmpty()) && str.length() <= limitLength;
    }

    public boolean validateInteger(int num, boolean allowNegative, boolean allowZero, boolean allowPositive) {
        if (!allowNegative && num < 0) {
            return false;
        }
        if (!allowZero && num == 0) {
            return false;
        }
        if (!allowPositive && num > 0) {
            return false;
        }

        return true;
    }

    public int getTotalPages(int countItems) {

        return (int) Math.ceil((double) countItems / MAX_ELEMENTS_PER_PAGE);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* sample */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet IngredientServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet IngredientServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String namepage = "";
        String view = request.getParameter("view");

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

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

            request.setAttribute("currentIngredient", ingredientDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }
        
        int page;
        int totalPages = getTotalPages(ingredientDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ingredientsList", ingredientDAO.getAll(page, keyword)); // Changed to 'ingredients' list
        request.setAttribute("typesList", typeDAO.getAll());

        request.getRequestDispatcher("/WEB-INF/ingredient/" + namepage + ".jsp").forward(request, response);

    }

    // POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        boolean passValidation = true;
        if (action != null && !action.isEmpty()) {

            if (action.equalsIgnoreCase("add")) {
//                String name = request.getParameter("ingredientName");
//                String unit = request.getParameter("unit");
//                int typeId;
//
//                try {
//                    typeId = Integer.parseInt(request.getParameter("typeId"));
//                } catch (NumberFormatException e) {
//                    typeId = -1;
//                }
//
//                //validate
//                if (!validateString(name, -1) || !validateInteger(typeId, false, false, true)) {
//                    passValidation = false;
//                }
//                //end
//                if (passValidation == true) {
//                    if (ingredientDAO.add(name, typeId, unit, expirationDate) >= 1) {
//                    } else {
//                        passValidation = false;
//                    }
//                }

            } else if (action.equalsIgnoreCase("edit")) {
//                int id;
//                int typeId;
//                String name = request.getParameter("ingredientName");
//                String unit = request.getParameter("unit");
//                LocalDate expirationDate = parseExpirationDate(request.getParameter("expirationDate"));
//
//                try {
//                    id = Integer.parseInt(request.getParameter("id"));
//                } catch (NumberFormatException e) {
//                    id = -1;
//                    passValidation = false;
//                }
//
//                try {
//                    typeId = Integer.parseInt(request.getParameter("typeId"));
//                } catch (NumberFormatException e) {
//                    typeId = -1;
//                }
//
//                //validate
//                if (!validateString(name, -1)
//                        || !validateInteger(id, false, false, true)
//                        || !validateInteger(typeId, false, false, true)) {
//                    passValidation = false;
//                }
//                //end
//                if (passValidation == true) {
//                    int checkError = ingredientDAO.edit(id, name, typeId, unit, expirationDate);
//
//                    if (checkError >= 1) {
//
//                    } else {
//                        if (checkError - DUPLICATE_KEY == 0) {
//                            System.err.println("DUPLICATE_KEY");
//                        } else if (checkError - FOREIGN_KEY_VIOLATION == 0) {
//                            System.err.println("FOREIGN_KEY_VIOLATION");
//                        } else if (checkError - NULL_INSERT_VIOLATION == 0) {
//                            System.err.println("NULL_INSERT_VIOLATION");
//                        }
//
//                        passValidation = false;
//                    }
//                }
            } else if (action.equalsIgnoreCase("delete")) {
//                int id;
//
//                try {
//                    id = Integer.parseInt(request.getParameter("id"));
//                } catch (NumberFormatException e) {
//                    id = -1;
//
//                    passValidation = false;
//                }
//
//                //validate
//                if (!validateInteger(id, false, false, true)) {
//                    passValidation = false;
//                }
//                //end
//                if (passValidation == true) {
//                    int checkError = ingredientDAO.delete(id);
//
//                    if (checkError >= 1) {
//
//                    } else {
//                        if (checkError - DUPLICATE_KEY == 0) {
//                            System.err.println("DUPLICATE_KEY");
//                        } else if (checkError - FOREIGN_KEY_VIOLATION == 0) {
//                            System.err.println("FOREIGN_KEY_VIOLATION");
//                        } else if (checkError - NULL_INSERT_VIOLATION == 0) {
//                            System.err.println("NULL_INSERT_VIOLATION");
//                        }
//
//                        passValidation = false;
//                    }
//                }
            }
        }

//        response.sendRedirect(request.getContextPath() + "/ingredient?" + "status=" + (passValidation ? "success" : "fail") + "&lastAction=" + addEDtoEverything(action));

    }

    @Override
    public String getServletInfo() {
        return "Ingredient management servlet";
    }// </editor-fold>

}
