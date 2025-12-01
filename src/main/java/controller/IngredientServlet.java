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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        
        if (limitLength < 0) limitLength = Integer.MAX_VALUE;
        
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
public String addEDtoEverything(String str) {
        if (str == null || str.isEmpty()) return "";
        String temp = str.substring(str.length() - 1);
        return str + (temp.equalsIgnoreCase("t") || temp.equalsIgnoreCase("d") ? "ed" : "d");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
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

        String searchKeyword = request.getParameter("search");
        List<Ingredient> ingredients;
        int totalPages = 1;
        int page = 1;

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        LocalDate today = LocalDate.now();
        request.setAttribute("today", today);

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {

            ingredients = ingredientDAO.search(searchKeyword);
            request.setAttribute("searchKeyword", searchKeyword);
        } else {

            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
            totalPages = getTotalPages(ingredientDAO.countItem());
            ingredients = ingredientDAO.getAll(page, keyword);
        }

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
            int id = 0;
            namepage = "list";
            request.setAttribute("importList", importDAO.getImportDetails(id, page, keyword));
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

        // The old pagination logic is now integrated above
        // int page;
        // int totalPages = getTotalPages(ingredientDAO.countItem()); // Moved up
        // try {
        //     page = Integer.parseInt(request.getParameter("page"));
        // } catch (NumberFormatException e) {
        //     page = 1;
        // }
        // Set attributes based on search or list view
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ingredientsList", ingredients); // Changed to 'ingredients' list
        request.setAttribute("currentPage", page); // Add current page to handle pagination CSS
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
                String name = request.getParameter("ingredientName");
                String unit = request.getParameter("unit");
                int typeId;
                LocalDate expirationDate = parseExpirationDate(request.getParameter("expirationDate"));
                

                try {
                    typeId = Integer.parseInt(request.getParameter("typeId"));
                } catch (NumberFormatException e) {
                    typeId = -1;
                }

                //validate
                if (!validateString(name, -1) || !validateInteger(typeId, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    if (ingredientDAO.add(name, typeId, unit, expirationDate) >= 1) {
                    } else {
                        passValidation = false;
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                int typeId;
                String name = request.getParameter("ingredientName");
                String unit = request.getParameter("unit");
                LocalDate expirationDate = parseExpirationDate(request.getParameter("expirationDate"));


                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                    passValidation = false;
                }

                try {
                    typeId = Integer.parseInt(request.getParameter("typeId"));
                } catch (NumberFormatException e) {
                    typeId = -1;
                }

                //validate
                if (!validateString(name, -1)
                        || !validateInteger(id, false, false, true)
                        || !validateInteger(typeId, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    int checkError = ingredientDAO.edit(id, name, typeId, unit, expirationDate);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - DUPLICATE_KEY == 0) {
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - NULL_INSERT_VIOLATION == 0) {
                            System.err.println("NULL_INSERT_VIOLATION");
                        }

                        passValidation = false;
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;

                    passValidation = false;
                }

                //validate
                if (!validateInteger(id, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    int checkError = ingredientDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - DUPLICATE_KEY == 0) {
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - NULL_INSERT_VIOLATION == 0) {
                            System.err.println("NULL_INSERT_VIOLATION");
                        }

                        passValidation = false;
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/ingredient?" + "status=" + (passValidation ? "success" : "fail") + "&lastAction=" + addEDtoEverything(action));

    }

    private LocalDate parseExpirationDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(rawDate.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    @Override
    public String getServletInfo() {
        return "Ingredient management servlet";
    }// </editor-fold>

}
