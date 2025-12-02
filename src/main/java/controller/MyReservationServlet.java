/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.getSqlErrorCode;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateInteger;
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
import java.sql.Date;
import java.sql.Time;
import model.Reservation;

/**
 *
 * @author Tieu Gia Huy - CE191594
 */
@WebServlet(name = "MyReservationServlet", urlPatterns = {"/my-reservation"})
public class MyReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();
    TableDAO tableDAO = new TableDAO();

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
            out.println("<title>Servlet MyReservationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyReservationServlet at " + request.getContextPath() + "</h1>");
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

        String view = request.getParameter("view");

        // ----- CUSTOMER EDIT FORM -----
        if ("edit".equalsIgnoreCase(view)) {
            int id = -1;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (Exception e) {
            }

            // Lấy reservation
            Reservation r = reservationDAO.getElementByID(id);
            if (r == null) {
                response.sendRedirect(request.getContextPath() + "/my-reservation");
                return;
            }

            // Lấy bàn + các khung giờ đã đặt của bàn đó
            int tableId = r.getTable().getId();

            request.setAttribute("currentReservation", r);
            request.setAttribute("selectedTable", tableDAO.getElementByID(tableId));
            request.setAttribute("existingReservations",
                    reservationDAO.getReservationsByTable(tableId));

            request.getRequestDispatcher("/WEB-INF/reservation/edit.jsp")
                    .forward(request, response);
            return;
        }

        // ----- DEFAULT: MY LIST -----
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        int customerId;
        try {
            customerId = Integer.parseInt(request.getParameter("customerId"));
        } catch (Exception e) {
            customerId = -1;
        }

        int totalPages = getTotalPages(reservationDAO.countByCustomer(customerId, keyword));

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("customerId", customerId);
        request.setAttribute("reservationList", reservationDAO.getByCustomer(customerId, page, keyword));
        request.setAttribute("availableTables", tableDAO.getAll());

        request.getRequestDispatcher("/WEB-INF/reservation/mylist.jsp")
                .forward(request, response);

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

        if (!validateString(action, -1)) {
            response.sendRedirect(request.getContextPath() + "/my-reservation");
            return;
        }

        // ----- CUSTOMER EDIT -----
        if ("edit".equalsIgnoreCase(action)) {
            int id, tableId;
            Date date;
            Time timeStart, timeEnd;

            try {
                id = Integer.parseInt(request.getParameter("reservationId"));
                tableId = Integer.parseInt(request.getParameter("tableId"));
                date = Date.valueOf(request.getParameter("reservationDate"));

                String sStart = request.getParameter("timeStart");
                String sEnd = request.getParameter("timeEnd");

                if (sStart.length() == 5) {
                    sStart += ":00";
                }
                if (sEnd.length() == 5) {
                    sEnd += ":00";
                }

                timeStart = Time.valueOf(sStart);
                timeEnd = Time.valueOf(sEnd);

// Validation: end must be after start
                if (!timeEnd.after(timeStart)) {
                    popupStatus = false;
                    popupMessage = "End time must be later than start time.";

                    request.getSession().setAttribute("popupMessage", popupMessage);
                    request.getSession().setAttribute("popupStatus", popupStatus);
                    request.getSession().setAttribute("popupPage", "my-reservation");

                    response.sendRedirect("my-reservation?customerId=" + request.getParameter("customerId"));
                    return;
                }

            } catch (Exception e) {
                popupStatus = false;
                popupMessage = "Invalid edit input.";

                request.getSession().setAttribute("popupMessage", popupMessage);
                request.getSession().setAttribute("popupStatus", popupStatus);
                request.getSession().setAttribute("popupPage", "my-reservation");

                response.sendRedirect("my-reservation?customerId=" + request.getParameter("customerId"));
                return;
            }

            int check = reservationDAO.edit(id, tableId, date, timeStart, timeEnd);
            if (check < 1) {
                popupStatus = false;
                popupMessage = "Edit failed. SQL: " + getSqlErrorCode(check);
            } else {
                popupMessage = "Reservation updated successfully.";
            }

            request.getSession().setAttribute("popupMessage", popupMessage);
            request.getSession().setAttribute("popupStatus", popupStatus);
            request.getSession().setAttribute("popupPage", "my-reservation");

            response.sendRedirect("my-reservation?customerId=" + request.getParameter("customerId"));
            return;
        }

        if (action.equalsIgnoreCase("add")) {
            int customerId, tableId;
            Date date;
            Time timeStart, timeEnd;

            try {
                customerId = Integer.parseInt(request.getParameter("customerId"));
                tableId = Integer.parseInt(request.getParameter("tableId"));
                date = Date.valueOf(request.getParameter("reservationDate"));

                String t = request.getParameter("reservationTime");
                if (t.length() == 5) {
                    t += ":00";
                }

                timeStart = Time.valueOf(t);
                timeEnd = Time.valueOf(
                        timeStart.toLocalTime().plusHours(3).toString()
                );

            } catch (Exception e) {
                popupStatus = false;
                popupMessage = "Invalid input for Add Reservation.";
                request.getSession().setAttribute("popupMessage", popupMessage);
                request.getSession().setAttribute("popupStatus", popupStatus);
                request.getSession().setAttribute("popupPage", "my-reservation");

                response.sendRedirect(request.getContextPath()
                        + "/my-reservation?customerId=" + request.getParameter("customerId"));
                return;
            }

            model.Table selectedTable = tableDAO.getElementByID(tableId);
            if (selectedTable == null) {
                popupStatus = false;
                popupMessage = "Table not found.";
            } else if (selectedTable.getStatus().equalsIgnoreCase("Reserved")) {
                popupStatus = false;
                popupMessage = "This table is currently reserved and not available.";
            } else {
                int check = reservationDAO.add(customerId, tableId, date, timeStart, timeEnd);
                if (check < 1) {
                    popupStatus = false;
                    popupMessage = "Add failed. SQL error: " + getSqlErrorCode(check);
                } else {
                    if (selectedTable.getStatus().equalsIgnoreCase("Occupied")) {
                        popupMessage = "Bàn hiện đang ở trạng thái Occupied, yêu cầu đã gửi (Pending).";
                    } else {
                        popupMessage = "Reservation created successfully (Pending).";
                    }
                }
            }

            request.getSession().setAttribute("popupMessage", popupMessage);
            request.getSession().setAttribute("popupStatus", popupStatus);
            request.getSession().setAttribute("popupPage", "my-reservation");

            response.sendRedirect(request.getContextPath()
                    + "/my-reservation?customerId=" + request.getParameter("customerId"));
            return;
        } else if (action.equalsIgnoreCase("cancel")) {
            // Customer tự hủy reservation
            int id, customerId;
            try {
                id = Integer.parseInt(request.getParameter("id"));
                customerId = Integer.parseInt(request.getParameter("customerId"));
            } catch (NumberFormatException e) {
                id = -1;
                customerId = -1;
            }

            if (!validateInteger(id, false, false, true) || !validateInteger(customerId, false, false, true)) {
                popupStatus = false;
                popupMessage = "Invalid cancel request.";
            } else {
                int check = reservationDAO.cancelByCustomer(id, customerId);
                if (check < 1) {
                    popupStatus = false;
                    popupMessage = "Cancel failed. SQL error: " + getSqlErrorCode(check);
                } else {
                    popupMessage = "Reservation cancelled.";
                }
            }

            request.getSession().setAttribute("popupMessage", popupMessage);
            request.getSession().setAttribute("popupStatus", popupStatus);
            request.getSession().setAttribute("popupPage", "my-reservation");

            response.sendRedirect(request.getContextPath()
                    + "/my-reservation?customerId=" + request.getParameter("customerId"));
            return;
        }

        // Nếu action không khớp
        response.sendRedirect(request.getContextPath()
                + "/my-reservation?customerId=" + request.getParameter("customerId"));
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
