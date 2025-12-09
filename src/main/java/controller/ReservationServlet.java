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
import dao.CustomerDAO;

import dao.ReservationDAO;
import dao.TableDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import model.Customer;
import model.Employee;
import model.Reservation;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservation"})
public class ReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();
    TableDAO tableDAO = new TableDAO();
    CustomerDAO customerDAO = new CustomerDAO();

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

        String view = request.getParameter("view");
        if (view == null) {
            view = "list";
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        // LIST
        if (view.equalsIgnoreCase("list")) {
            int totalPages = getTotalPages(reservationDAO.countItem(keyword));
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("reservationList", reservationDAO.getAll(page, keyword));
            request.getRequestDispatcher("/WEB-INF/reservation/list.jsp").forward(request, response);
            return;
        }

        // CREATE FORM
        if (view.equalsIgnoreCase("add")) {

            // LẤY DANH SÁCH BÀN
            request.setAttribute("listTable", tableDAO.getAll());

            // LẤY DANH SÁCH CUSTOMER
            request.setAttribute("listCustomer", customerDAO.getAll());

            request.getRequestDispatcher("/WEB-INF/reservation/create.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/reservation");
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
            response.sendRedirect(request.getContextPath() + "/reservation");
            return;
        } //        // ==== EDIT (dùng chung, nhưng redirect khác nhau tùy 'from') ====
        //        if (action.equalsIgnoreCase("edit")) {
        //            int id, tableId;
        //            Date date;
        //            Time time;
        //
        //            String dateStr = request.getParameter("reservationDate");
        //            String timeStr = request.getParameter("reservationTime");
        //
        //            try {
        //                id = Integer.parseInt(request.getParameter("reservationId"));
        //                tableId = Integer.parseInt(request.getParameter("tableId"));
        //                // Parse date an toàn
        //                date = (dateStr != null && !dateStr.isEmpty()) ? Date.valueOf(dateStr) : null;
        //
        //                // Parse time an toàn cho cả HH:mm và HH:mm:ss
        //                if (timeStr != null && !timeStr.isEmpty()) {
        //                    timeStr = timeStr.trim();
        //                    if (timeStr.length() == 5) {           // "HH:mm"
        //                        timeStr = timeStr + ":00";
        //                    } else if (timeStr.length() == 8) {    // "HH:mm:ss"
        //                        // giữ nguyên
        //                    } else if (timeStr.contains("T")) {    // "YYYY-MM-DDTHH:mm"
        //                        String[] parts = timeStr.split("T");
        //                        String hhmm = parts[1];
        //                        timeStr = (hhmm.length() == 5) ? hhmm + ":00" : hhmm;
        //                    }
        //                    time = Time.valueOf(timeStr);          // ném lỗi nếu format sai
        //                } else {
        //                    time = null;
        //                }
        //            } catch (Exception e) {
        //                id = -1;
        //                tableId = -1;
        //                date = null;
        //                time = null;
        //            }
        //
        //            if (!validateInteger(id, false, false, true) || tableId <= 0 || date == null || time == null) {
        //                popupStatus = false;
        //                popupMessage = "Edit failed. Invalid input.";
        //            } else {
        //                int check = reservationDAO.edit(id, tableId, date, time);
        //                if (check < 1) {
        //                    popupStatus = false;
        //                    popupMessage = "Edit failed. SQL error: " + getSqlErrorCode(check);
        //                } else {
        //                    popupMessage = "Reservation (ID: " + id + ") updated successfully.";
        //                }
        //            }
        //
        //            setPopup(request, popupStatus, popupMessage);
        //
        //            // Điều hướng theo nguồn gọi
        //            String from = request.getParameter("from");
        //            String customerIdStr = request.getParameter("customerId");
        //
        //            if ("mylist".equalsIgnoreCase(from) && customerIdStr != null && !customerIdStr.isEmpty()) {
        //                // popup cho customer
        //                request.getSession().setAttribute("popupMessage", popupMessage);
        //                request.getSession().setAttribute("popupStatus", popupStatus);
        //                request.getSession().setAttribute("popupPage", "my-reservation");
        //
        //                response.sendRedirect(request.getContextPath()
        //                        + "/my-reservation?customerId=" + customerIdStr);
        //            } else {
        //                // popup cho admin (giữ nguyên cách cũ nếu muốn)
        //                setPopup(request, popupStatus, popupMessage);
        //                response.sendRedirect(request.getContextPath() + "/reservation");
        //            }
        //
        //            return;
        //       } 

        // ----- ADMIN ADD RESERVATION -----
        if (action.equalsIgnoreCase("add")) {

            int customerId;
            int tableId;
            Date date;
            Time timeStart, timeEnd;

            try {
                customerId = Integer.parseInt(request.getParameter("customerId"));
                tableId = Integer.parseInt(request.getParameter("tableId"));
                date = Date.valueOf(request.getParameter("reservationDate"));

                String sStart = request.getParameter("timeStart");
                String sEnd = request.getParameter("timeEnd");

                if (sStart == null || sEnd == null
                        || sStart.isBlank() || sEnd.isBlank()) {
                    throw new IllegalArgumentException();
                }

                if (sStart.length() == 5) {
                    sStart += ":00";
                }
                if (sEnd.length() == 5) {
                    sEnd += ":00";
                }

                timeStart = Time.valueOf(sStart);
                timeEnd = Time.valueOf(sEnd);

                if (!timeEnd.after(timeStart)) {
                    setPopup(request, false, "End time must be later than start time.");
                    response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                    return;
                }

            } catch (Exception e) {
                setPopup(request, false, "Invalid input for Add Reservation.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                return;
            }

            // ==============================
            // KIỂM TRA CUSTOMER & TABLE
            // ==============================
            Customer customer = customerDAO.getElementByID(customerId);
            if (customer == null) {
                setPopup(request, false, "Customer not found.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                return;
            }

            model.Table selectedTable = tableDAO.getElementByID(tableId);
            if (selectedTable == null) {
                setPopup(request, false, "Table not found.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                return;
            }

            // ==============================
            // LẤY EMPLOYEE ĐANG ĐĂNG NHẬP
            // ==============================
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session == null) {
                setPopup(request, false, "Employee is not logged in.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                return;
            }

            Employee emp = (Employee) session.getAttribute("employeeSession");
            if (emp == null) {
                setPopup(request, false, "Employee is not logged in.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add");
                return;
            }

            int empId = emp.getEmpId();   // DÙNG getEmpId() từ model Employee

            // ==============================
            // TẠO RESERVATION (EMPLOYEE TẠO GIÙM)
            // ==============================
            int check = reservationDAO.addByEmployee(
                    empId,
                    customerId,
                    tableId,
                    date,
                    timeStart,
                    timeEnd
            );

            if (check < 1) {
                setPopup(request, false, "Add failed. SQL error: " + getSqlErrorCode(check));
            } else {
                setPopup(request, true, "Reservation created successfully.");
            }

            response.sendRedirect(request.getContextPath() + "/reservation");
            return;
        } else if (action.equalsIgnoreCase("approve") || action.equalsIgnoreCase("reject")) {
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            if (!validateInteger(id, false, false, true)) {
                popupStatus = false;
                popupMessage = "Invalid reservation ID.";
            } else {
                Reservation current = reservationDAO.getElementByID(id);
                if (current == null) {
                    popupStatus = false;
                    popupMessage = "Reservation not found.";
                } else {
                    String currentStatus = current.getStatus();
                    String actionType = action.equalsIgnoreCase("approve") ? "Approved" : "Rejected";

                    if (currentStatus.equalsIgnoreCase("Approved") && action.equalsIgnoreCase("reject")) {
                        popupStatus = false;
                        popupMessage = "Cannot reject a reservation that has already been approved.";
                    } else if (currentStatus.equalsIgnoreCase("Rejected") && action.equalsIgnoreCase("approve")) {
                        popupStatus = false;
                        popupMessage = "Cannot approve a reservation that has already been rejected.";
                    } else if (currentStatus.equalsIgnoreCase("Cancelled")) {
                        popupStatus = false;
                        popupMessage = "Cannot change status of a cancelled reservation.";
                    } else {
                        int check = reservationDAO.updateStatus(id, actionType);
                        if (check < 1) {
                            popupStatus = false;
                            popupMessage = "Update failed. SQL error: " + getSqlErrorCode(check);
                        } else {
                            popupMessage = "Reservation (ID: " + id + ") updated -> " + actionType;
                        }
                    }
                }
            }

            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath() + "/reservation");
            return;
        }

        // Các action khác (add/cancel của customer) đã chuyển sang MyReservationServlet
        response.sendRedirect(request.getContextPath() + "/reservation");
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
