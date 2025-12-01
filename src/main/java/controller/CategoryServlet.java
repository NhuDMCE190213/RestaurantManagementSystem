/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.addEDtoEverything;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.validateInteger;
import static constant.CommonFunction.validateString;
import constant.Constants;
import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Category;

/**
 *
 * @author Nguyen Thanh Nhan - CE190122
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {"/category"})
public class CategoryServlet extends HttpServlet {

    CategoryDAO categoryDAO = new CategoryDAO();

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
            out.println("<title>Servlet CategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryServlet at " + request.getContextPath() + "</h1>");
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

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
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

            request.setAttribute("currentCategory", categoryDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(categoryDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categoryList", categoryDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/category/" + namepage + ".jsp").forward(request, response);
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
        

        boolean passValidation = true;
        if (action != null && !action.isEmpty()) {

            if (action.equalsIgnoreCase("add")) {
                String name = request.getParameter("categoryName");
                String description = request.getParameter("description");

//validate
                if (!validateString(name, -1)) {
                    passValidation = false;
                }
//end
                if (passValidation == true) {
                    if (categoryDAO.add(name, description) >= 1) {
                    } else {
                        passValidation = false;
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                String name = request.getParameter("name");
                String description = request.getParameter("description");

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;

                    passValidation = false;
                }
 
//validate
                if (!validateString(name, -1)
                        || !validateInteger(id, false, false, true)) {
                    passValidation = false;
                }
//end
                if (passValidation == true) {
                    int checkError = categoryDAO.edit(id, name, description);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - Constants.DUPLICATE_KEY == 0) {                //check trung code/key
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - Constants.FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - Constants.NULL_INSERT_VIOLATION == 0) {
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
                    int checkError = categoryDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - Constants.DUPLICATE_KEY == 0) {                //check trung code/key
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - Constants.FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - Constants.NULL_INSERT_VIOLATION == 0) {
                            System.err.println("NULL_INSERT_VIOLATION");
                        }

                        passValidation = false;
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/category?" + "status=" + (passValidation ? "success" : "fail") + "&lastAction=" + addEDtoEverything(action));

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
