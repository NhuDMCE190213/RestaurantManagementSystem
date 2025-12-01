/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import dao.EmployeeDAO;
import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.ReservationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.MenuItem;

/**
 *
 * @author TruongBinhTrong
 */
@WebServlet(name = "HomepageServlet", urlPatterns = {"/homepage"})
public class HomepageServlet extends HttpServlet {

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
            out.println("<title>Servlet HomepageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomepageServlet at " + request.getContextPath() + "</h1>");
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
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        
        CustomerDAO customerDAO = new CustomerDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        OrderDAO orderDAO = new OrderDAO();
        
        try {

            List<String> categoryNames = menuItemDAO.getTopCategoryNames();

            request.setAttribute("categoryNames", categoryNames);

            for (String categoryName : categoryNames) {

                List<MenuItem> menuItems = menuItemDAO.getTopMenuItemsByCategoryName(categoryName);

                String attributeName = categoryName.toLowerCase().replace(" ", "").concat("List");

                request.setAttribute(attributeName, menuItems);
            }
            
            request.setAttribute("numberOfCustomer", customerDAO.countItem());
            request.setAttribute("numberOfEmployee", employeeDAO.countItem());
            request.setAttribute("numberOfReservation", reservationDAO.countItem(""));
            request.setAttribute("numberOfOrder", orderDAO.countItem());

            request.getRequestDispatcher("/WEB-INF/homepage/homepage.jsp").forward(request, response);

        } catch (Exception e) {

            log("Error in MenuItemServlet: " + e.getMessage(), e);

            throw new ServletException("Could not load menu data.", e);
        }
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
        processRequest(request, response);
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
