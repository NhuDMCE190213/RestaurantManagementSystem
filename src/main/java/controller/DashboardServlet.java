/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to editQuantityList this template
 */
package controller;

import com.google.gson.Gson;
import dao.OrderItemDAO;
import dao.ReservationDAO;
import dao.TableDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();

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
            out.println("<title>Servlet DashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to editQuantityList the code.">
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

        request.setAttribute("totalReservations", reservationDAO.totalReservation());
        request.setAttribute("totalIncome", orderItemDAO.getFormatVND(orderItemDAO.getTotalIncome()));
        request.setAttribute("activeTables", tableDAO.getTotalTableAvailable());

        //income table
        Map<String, Integer> monthIncomeMap = reservationDAO.getMonthIncomeList();
        String[] monthLabels = new String[monthIncomeMap.size()];
        int[] monthlyIncome = new int[monthIncomeMap.size()];

        int index = 0;
        for (Map.Entry<String, Integer> entry : monthIncomeMap.entrySet()) {
            monthLabels[index] = entry.getKey();
            monthlyIncome[index] = entry.getValue();
            index++;
        }
        Gson gson = new Gson();
        request.setAttribute("monthLabels", gson.toJson(monthLabels));
        request.setAttribute("monthlyIncome", gson.toJson(monthlyIncome));
        //end
        
        //table chart
        Map<String, Integer> tableMap = reservationDAO.getTableListOfUsed();
        String[] numberTableList = new String[tableMap.size()];
        int[] usedTableList = new int[tableMap.size()];

        int i = 0;
        for (Map.Entry<String, Integer> entry : tableMap.entrySet()) {
            numberTableList[i] = entry.getKey();
            usedTableList[i] = entry.getValue();
            i++;
        }
        
        request.setAttribute("tableNames", gson.toJson(numberTableList));
        request.setAttribute("tableUsage", gson.toJson(usedTableList));
        //end

        //reservation status
        Map<String, Integer> statusMap = reservationDAO.getStatusList();

        request.setAttribute("waitingDepositCount", AutoNumber(statusMap.get("Waiting_deposit")));
        request.setAttribute("reservingCount", AutoNumber(statusMap.get("Reserving")));
        request.setAttribute("approvedCount", AutoNumber(statusMap.get("Approved")));
        request.setAttribute("unpaidCount", AutoNumber(statusMap.get("Unpaid")));
        request.setAttribute("completedCount", AutoNumber(statusMap.get("Completed")));
        request.setAttribute("cancelledBeforeDepositCount", AutoNumber(statusMap.get("Cancel_before_deposit")));
        request.setAttribute("cancelledAfterDepositCount", AutoNumber(statusMap.get("Cancel_after_deposit")));
        request.setAttribute("noShowCount", AutoNumber(statusMap.get("No_show")));
        request.setAttribute("rejectedCount", AutoNumber(statusMap.get("Rejected")));
        //end

        request.getRequestDispatcher("/WEB-INF/dashboard/dashboard.jsp").forward(request, response);
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

    public int AutoNumber(Object obj) {
        if (obj == null) {
            return 0;
        }
        return (int) obj;
    }
}
