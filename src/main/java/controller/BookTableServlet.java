/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import static constant.CommonFunction.getSqlErrorCode;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateString;
import dao.ReservationDAO;
import dao.TableDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import model.Customer;
import model.Reservation;

/**
 *
 * @author TruongBinhTrong
 */
@WebServlet(name = "BookTableServlet", urlPatterns = {"/booktable"})
public class BookTableServlet extends HttpServlet {

    TableDAO tableDAO = new TableDAO();
    ReservationDAO reservationDAO = new ReservationDAO();
    public String json;

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
            out.println("<title>Servlet ReservationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReservationServlet at " + request.getContextPath() + "</h1>");
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

        if (!validateString(view, -1) || view.equalsIgnoreCase("booktable")) {
            namepage = "booktable";
            request.setAttribute("tableList", tableDAO.getAll());

        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
            try {
                Date date = Date.valueOf(LocalDate.now());
                int tableId = Integer.parseInt(request.getParameter("tableId"));
                request.setAttribute("selectedTable", tableDAO.getElementByID(tableId));
                List<Reservation> list = reservationDAO.getReservationsByTable(tableId);
                List<Time[]> ranges = reservationDAO.getStartEndTimesByTableAndDate(tableId, date);
                request.setAttribute("reservedRanges", ranges);

                if (list == null) {
                    list = java.util.Collections.emptyList();
                }
                request.setAttribute("existingReservations", list);
            } catch (Exception e) {
                request.setAttribute("selectedTable", null);
                request.setAttribute("existingReservations", java.util.Collections.emptyList());
            }
        } else if (view.equalsIgnoreCase("edit")) {
            namepage = "edit";
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        request.getRequestDispatcher("/WEB-INF/reservation/" + namepage + ".jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Customer customer = null;
        if (session != null) {
            customer = (Customer) session.getAttribute("customerSession");
        }

        if (customer == null) {
            setPopup(request, false, "Please login to make a reservation.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean popupStatus = true;
        String popupMessage = "";

        try {
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            Date date = Date.valueOf(request.getParameter("reservationDate"));
            String timeStr = request.getParameter("reservationTime");

            Time time;
            if (timeStr != null && timeStr.contains("T")) {
                String[] parts = timeStr.split("T");
                time = Time.valueOf(parts[1] + ":00");
            } else {
                time = Time.valueOf(timeStr + ":00");
            }

            // Lấy trạng thái bàn hiện tại
            model.Table selectedTable = tableDAO.getElementByID(tableId);

            int check = reservationDAO.add(customer.getCustomerId(), tableId, date, time);
            if (check < 1) {
                popupStatus = false;
                popupMessage = "Booking failed. SQL error: " + getSqlErrorCode(check);
            } else {
                if (selectedTable != null
                        && "occupied".equalsIgnoreCase(selectedTable.getStatus())) {
                    popupMessage = "Table is currently in Occupied state, "
                            + "Your reservation request has been submitted and will likely not be accepted. "
                            + "(Reservation status = Pending)";
                } else {
                    popupMessage = "Reservation created successfully! Status = Pending.";
                }
            }
        } catch (Exception e) {
            popupStatus = false;
            popupMessage = "Invalid input. Please check your booking information.";
        }

        session.setAttribute("popupMessage", popupMessage);
        session.setAttribute("popupStatus", popupStatus);
        session.setAttribute("popupPage", "my-reservation");

        response.sendRedirect(
                request.getContextPath()
                + "/my-reservation?customerId=" + customer.getCustomerId()
        );
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
