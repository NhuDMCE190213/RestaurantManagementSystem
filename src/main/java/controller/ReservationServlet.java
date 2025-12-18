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
import dao.VoucherDAO;
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
import model.Voucher;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservation"})
public class ReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();
    TableDAO tableDAO = new TableDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    VoucherDAO voucherDAO = new VoucherDAO();

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

        // BƯỚC 1: CHỌN BÀN (BOOKATABLE CHO EMPLOYEE)
        if (view.equalsIgnoreCase("bookatable")) {
            request.setAttribute("listTable", tableDAO.getAll()); // hoặc getAvailableTables nếu muốn
            request.getRequestDispatcher("/WEB-INF/reservation/bookatable.jsp").forward(request, response);
            return;
        }

        // BƯỚC 2: FORM TẠO RESERVATION CHO BÀN ĐÃ CHỌN
        if (view.equalsIgnoreCase("add")) {

            int tableId;
            try {
                tableId = Integer.parseInt(request.getParameter("tableId"));
            } catch (Exception e) {
                // Không có tableId thì quay lại chọn bàn
                response.sendRedirect(request.getContextPath() + "/reservation?view=bookatable");
                return;
            }

            model.Table selectedTable = tableDAO.getElementByID(tableId);
            if (selectedTable == null) {
                setPopup(request, false, "Table not found.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=bookatable");
                return;
            }

            // gửi bàn đã chọn + danh sách customer + reservation hiện có của bàn đó
            request.setAttribute("selectedTable", selectedTable);
            request.setAttribute("listCustomer", customerDAO.getAll());
            request.setAttribute("reservedRanges",
                    reservationDAO.getStartEndTimesByTableAndDate(tableId, Date.valueOf(java.time.LocalDate.now().toString())));
            request.setAttribute("existingReservations", reservationDAO.getReservationsByTable(tableId));

            request.getRequestDispatcher("/WEB-INF/reservation/create.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/reservation");
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
            String description = request.getParameter("description");
            if (description == null) {
                description = "";
            }

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
                    response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
                    return;
                }

                boolean ok = reservationDAO.isTimeSlotAvailable(tableId, date, timeStart, timeEnd, null);
                if (!ok) {
                    setPopup(request, false, "This table has already been booked in the selected time range.");
                    response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
                    return;
                }

            } catch (Exception e) {
                setPopup(request, false, "Invalid input for Add Reservation.");
                String t = request.getParameter("tableId");
                if (t != null && !t.isBlank()) {
                    response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + t);
                } else {
                    response.sendRedirect(request.getContextPath() + "/reservation?view=bookatable");
                }
                return;
            }

            // ==============================
            // KIỂM TRA CUSTOMER & TABLE
            // ==============================
            Customer customer = customerDAO.getElementByID(customerId);
            if (customer == null) {
                setPopup(request, false, "Customer not found.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
                return;
            }

            model.Table selectedTable = tableDAO.getElementByID(tableId);
            if (selectedTable == null) {
                setPopup(request, false, "Table not found.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
                return;
            }

            // ==============================
            // LẤY EMPLOYEE ĐANG ĐĂNG NHẬP
            // ==============================
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session == null) {
                setPopup(request, false, "Employee is not logged in.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
                return;
            }

            Employee emp = (Employee) session.getAttribute("employeeSession");
            if (emp == null) {
                setPopup(request, false, "Employee is not logged in.");
                response.sendRedirect(request.getContextPath() + "/reservation?view=add&tableId=" + tableId);
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
                    timeEnd,
                    description
            );

            if (check < 1) {
                setPopup(request, false, "Add failed. SQL error: " + getSqlErrorCode(check));
            } else {
                setPopup(request, true, "Reservation created successfully.");

                try {
                    tableDAO.updateStatus(tableId, "Reserved");
                } catch (Exception ex) {
                    ex.printStackTrace(); // log nhẹ, không làm crash flow
                }
            }

            response.sendRedirect(request.getContextPath() + "/reservation");
            return;
            // ====== APPROVE / REJECT / COMPLETE (ADMIN) ======
        } else if ("approve".equalsIgnoreCase(action)
                || "reject".equalsIgnoreCase(action)
                || "serving".equalsIgnoreCase(action)
                || "unpaid".equalsIgnoreCase(action)
                || "no_show".equalsIgnoreCase(action)
                || "complete".equalsIgnoreCase(action)) {

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
                    String targetStatus = null;

                    if ("approve".equalsIgnoreCase(action)) {
                        targetStatus = "Approved";
                    } else if ("reject".equalsIgnoreCase(action)) {
                        targetStatus = "Rejected";
                    } else if ("serving".equalsIgnoreCase(action)) {
                        targetStatus = "Serving";
                    } else if ("unpaid".equalsIgnoreCase(action)) {
                        targetStatus = "Unpaid";
                    } else if ("no_show".equalsIgnoreCase(action)) {
                        targetStatus = "No_show";
                    } else if ("complete".equalsIgnoreCase(action)) {
                        targetStatus = "Completed";    // reservation hiển thị Complete
                    }

                    // ====== RULE NGHIỆP VỤ ======
                    if ("Cleaning".equalsIgnoreCase(currentStatus)
                            || "Cancelled".equalsIgnoreCase(currentStatus)
                            || "Completed".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Cannot change status of this reservation.";
                    } else if ("approve".equalsIgnoreCase(action)
                            && !"Waiting_deposit".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only Waiting_deposit reservations can be approved.";
                    } else if ("reject".equalsIgnoreCase(action)
                            && !"Waiting_deposit".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only Waiting_deposit reservations can be rejected.";
                    } else if ("serving".equalsIgnoreCase(action)
                            && !"Approved".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only approved reservations can be moved to reserving.";
                    } else if ("no_show".equalsIgnoreCase(action)
                            && !"Approved".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only approved reservations can be marked no-show.";
                    } else if ("unpaid".equalsIgnoreCase(action)
                            && !"Serving".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only serving reservations can be marked unpaid.";
                    } else if ("complete".equalsIgnoreCase(action)
                            && !"Serving".equalsIgnoreCase(currentStatus)) {
                        popupStatus = false;
                        popupMessage = "Only serving reservations can be completed.";
                    } else {
                        int check = 1;
                        if (targetStatus.equalsIgnoreCase("approved")) {
                            Reservation reservation = reservationDAO.getElementByID(id);
                            Voucher voucher = reservation.getVoucher();
                            if (voucher != null) {
                                if (voucherDAO.decrease1Quantity(voucher.getVoucherId()) <= 0) {
                                    check = -1;
                                }
                            }
                        }
                        if (check == 1) {
                            check = reservationDAO.updateStatus(id, targetStatus);
                        }
                        if (check < 1) {
                            popupStatus = false;
                            popupMessage = "Update failed. SQL error: " + getSqlErrorCode(check);
                        } else {
                            popupMessage = "Reservation (ID: " + id + ") updated -> " + targetStatus;

//                            // ✅ Sau khi APPROVE -> Table = Serving
//                            if ("approve".equalsIgnoreCase(action)) {
//                                try {
//                                    int tableId = current.getTable().getId();
//                                    tableDAO.updateStatus(tableId, "Reserved");
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }

                            if ("serving".equalsIgnoreCase(action)) {
                                try {
                                    int tableId = current.getTable().getId();
                                    tableDAO.updateStatus(tableId, "Serving");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            if ("unpaid".equalsIgnoreCase(action)) {
                                try {
                                    int tableId = current.getTable().getId();
                                    tableDAO.updateStatus(tableId, "Available");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            if ("no_show".equalsIgnoreCase(action)) {
                                try {
                                    int tableId = current.getTable().getId();
                                    tableDAO.updateStatus(tableId, "Available");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            // ✅ Sau khi COMPLETE -> Table = Cleaning
                            if ("complete".equalsIgnoreCase(action)) {
                                try {
                                    int tableId = current.getTable().getId();
                                    tableDAO.updateStatus(tableId, "Cleaning");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
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
