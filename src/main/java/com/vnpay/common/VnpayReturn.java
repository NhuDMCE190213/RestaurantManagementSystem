/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.vnpay.common;

import dao.ReservationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import model.Employee;
import model.Reservation;

/**
 *
 * @author HP
 */
@WebServlet(name = "VnpayReturn", urlPatterns = {"/vnpayReturn"})
public class VnpayReturn extends HttpServlet {
    
    private final ReservationDAO reservationDAO = new ReservationDAO();

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
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
            
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");
                
                int reservationId;
                int deposit;
                try {
                    reservationId = Integer.parseInt(request.getParameter("vnp_TxnRef").split("_")[0]);
                    deposit = Integer.parseInt(request.getParameter("vnp_Amount")) / 100;
                } catch (NumberFormatException e) {
                    reservationId = -1;
                    deposit = -1;
                }
                
                Reservation reservation = reservationDAO.getElementByID(reservationId);
                
                boolean transSuccess = false;
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    //update banking system
                    transSuccess = true;
                    
                    if (reservation.getStatus().equalsIgnoreCase("Serving")) {
                        reservationDAO.complete(reservationId, "Completed");
                    } else if (reservation.getStatus().equalsIgnoreCase("waiting_deposit")) {
                        reservationDAO.deposit(reservationId, "Approved", deposit);
                    }
                } else {
//                    reservationDAO.deposit(reservationId, "Failed");
                }
                request.setAttribute("transResult", transSuccess);
                
                HttpSession session = request.getSession(false);
                if (session != null) {
                    if (session.getAttribute("employeeSession") != null) {
                        request.getRequestDispatcher("/WEB-INF/vnpay/paymentResultEmployee.jsp").forward(request, response);
                    } else {
                        request.getRequestDispatcher("/WEB-INF/vnpay/paymentResult.jsp").forward(request, response);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            } else {
                //RETURN PAGE ERROR
                System.out.println("GD KO HOP LE (invalid signature)");
            }
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
        processRequest(request, response);
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
