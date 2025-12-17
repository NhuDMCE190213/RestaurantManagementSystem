<%-- 
    Document   : add
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>

<%@page import="model.Table"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%
    Table selected = (Table) request.getAttribute("selectedTable");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Book Table <%= (selected != null) ? selected.getNumber() : ""%></title>

        <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .booking-container {
                max-width: 500px;
                margin: 100px auto;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                padding: 30px;
            }
            .btn-confirm {
                background-color: #dc3545;
                color: white;
            }
            .btn-confirm:hover {
                background-color: #c82333;
            }
            #availabilityMsg {
                min-height: 1.4em;
                font-weight: 500;
            }
        </style>
    </head>

    <body>
        <div class="booking-container">
            <h4 class="text-center mb-4">
                Book Table <%= (selected != null) ? selected.getNumber() : ""%>
            </h4>

            <form id="bookingForm" action="<c:url value='/booktable'/>" method="post">
                <input type="hidden" name="tableId" id="tableId" value="<%= (selected != null) ? selected.getId() : ""%>">

                <div class="mb-3">
                    <label class="form-label">Voucher (optional)</label>
                    <select class="form-select" name="voucherId">
                        <option value="">-- No voucher --</option>
                        <c:forEach var="v" items="${voucherList}">
                            <option value="${v.voucherId}">
                                ${v.voucherCode} - ${v.voucherName} (${v.currentDiscount})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" id="reservationDate" class="form-control" required>
                </div>

                <div class="mb-4">
                    <label class="form-label">Start Time</label>
                    <input type="time" name="timeStart" id="timeStart" class="form-control"
                           required value="${currentReservation.timeStart}">
                </div>

                <div class="mb-4">
                    <label class="form-label">End Time</label>
                    <input type="time" name="timeEnd" id="timeEnd" class="form-control"
                           required value="${currentReservation.timeEnd}">
                    <p class="text-muted mb-1">End time must be after start time.</p>
                </div>

                <div class="mb-3">
                    <label class="form-label">Special Note (optional)</label>
                    <textarea name="description" class="form-control"
                              placeholder="Eg: Vegetarian, no spicy, allergy..."></textarea>
                </div>

                <c:if test="${not empty reservedRanges}">
                    <div class="alert alert-warning mt-3">
                        <strong>️The time slots have been booked:</strong>
                        <ul class="mb-0">
                            <c:forEach var="r" items="${reservedRanges}">
                                <li><fmt:formatDate value="${r[0]}" pattern="dd/MM/yyyy"/> : ${r[1]} → ${r[2]}</li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:if>


                <div id="availabilityMsg" class="mb-3"></div>

                <div class="d-flex justify-content-between">
                    <button type="submit" id="btnSubmit" class="btn btn-confirm">Confirm</button>
                    <a href="<c:url value='/booktable'/>" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <script>
            const existingReservations = [
            <c:forEach var="r" items="${existingReservations}" varStatus="loop">
            {
            date: '${r.reservationDate}',
                    start: '${r.timeStart}',
                    end: '${r.timeEnd}'
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];
        </script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const today = new Date().toISOString().split('T')[0];
                const dateEl = document.getElementById('reservationDate');
                const startEl = document.getElementById('timeStart');
                const endEl = document.getElementById('timeEnd');
                const btnSubmit = document.getElementById('btnSubmit');
                const availMsg = document.getElementById('availabilityMsg');

                dateEl.setAttribute('min', today);

                function showMessage(text, type) {
                    const cls = type === 'success' ? 'text-success'
                            : type === 'danger' ? 'text-danger'
                            : 'text-muted';
                    availMsg.innerHTML = '<span class="' + cls + '">' + text + '</span>';
                }

                function toMinutes(t) {
                    if (!t)
                        return 0;
                    const p = t.split(':');
                    return (parseInt(p[0], 10) || 0) * 60 + (parseInt(p[1], 10) || 0);
                }

                function isOverlap(aStart, aEnd, bStart, bEnd) {
                    return aStart < bEnd && aEnd > bStart;
                }

                // ✅ Check conflict theo OVERLAP + trừ 15' cho start của slot đã đặt
                function isConflict(selectedDate, selectedStart, selectedEnd) {
                    if (!selectedDate || !selectedStart || !selectedEnd)
                        return false;

                    const ns = toMinutes(selectedStart);
                    const ne = toMinutes(selectedEnd);

                    for (const r of existingReservations) {
                        if (r.date === selectedDate) {
                            const es = toMinutes(r.start) - 15; // trừ 15 phút
                            const ee = toMinutes(r.end);          // end giữ nguyên
                            if (isOverlap(ns, ne, es, ee))
                                return true;
                        }
                    }
                    return false;
                }

                function validate() {
                    const date = dateEl.value;
                    const start = startEl.value;
                    const end = endEl.value;

                    if (!date || !start || !end) {
                        showMessage('Please select date, start time and end time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    const startM = toMinutes(start);
                    const endM = toMinutes(end);

                    // Giờ hoạt động 05:00 - 22:00
                    if (startM < 5 * 60 || startM >= 22 * 60) {
                        showMessage('Start time must be between 05:00 and 21:59.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }
                    if (endM <= 5 * 60 || endM > 22 * 60) {
                        showMessage('End time must be between 05:01 and 22:00.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }
                    if (endM <= startM) {
                        showMessage('End time must be after start time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    if (isConflict(date, start, end)) {
                        showMessage('This time has already been booked. Please choose another time slot.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    showMessage('Available time.', 'success');
                    btnSubmit.disabled = false;
                }

                dateEl.addEventListener('change', validate);
                startEl.addEventListener('change', validate);
                endEl.addEventListener('change', validate);

                startEl.addEventListener('input', validate);
                endEl.addEventListener('input', validate);

                setTimeout(validate, 100);

                document.getElementById('bookingForm').addEventListener('submit', function (e) {
                    validate();
                    if (btnSubmit.disabled) {
                        e.preventDefault();
                        showMessage('Unable to submit because the time is unavailable.', 'danger');
                    }
                });
            });
        </script>

    </body>
</html>
