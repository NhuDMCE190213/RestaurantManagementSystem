<%-- 
    Document   : add
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>

<%@page import="model.Table"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" id="reservationDate" class="form-control" required>
                </div>

                <div class="mb-4">
                    <label class="form-label">Time</label>
                    <input type="time" name="reservationTime" id="reservationTime" class="form-control" required>
                    <p class="text-muted mb-1">Cannot book table a time between 05:00 and 22:00</p>
                </div>

                <c:if test="${not empty reservedRanges}">
                    <div class="alert alert-warning mt-3">
                        <strong>️The time slots have been booked:</strong>
                        <ul class="mb-0">
                            <c:forEach var="r" items="${reservedRanges}">
                                <li>${r[0]} → ${r[1]}</li>
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
                    time: '${r.reservationTime}'
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];
            console.log("existingReservations =", existingReservations);
            console.log("Test compare =>", new Date("2025-11-11T11:30:00") - new Date("2025-11-11T11:00:00"));

        </script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const today = new Date().toISOString().split('T')[0];
                const dateEl = document.getElementById('reservationDate');
                const timeEl = document.getElementById('reservationTime');
                const btnSubmit = document.getElementById('btnSubmit');
                const availMsg = document.getElementById('availabilityMsg');

                dateEl.setAttribute('min', today);

                function showMessage(text, type) {
                    const cls = type === 'success' ? 'text-success'
                            : type === 'danger' ? 'text-danger'
                            : 'text-muted';
                    availMsg.innerHTML = '<span class="' + cls + '">' + text + '</span>';
                }

                // Chuyển HH:mm hoặc HH:mm:ss thành phút trong ngày
                function toMinutes(timeStr) {
                    if (!timeStr)
                        return 0;
                    const parts = timeStr.split(':');
                    const h = parseInt(parts[0]);
                    const m = parseInt(parts[1]);
                    return h * 60 + m;
                }

                function isConflict(selectedDate, selectedTime) {
                    if (!selectedDate || !selectedTime)
                        return false;
                    const selectedMins = toMinutes(selectedTime);
                    for (const r of existingReservations) {
                        if (r.date === selectedDate) {
                            const existingMins = toMinutes(r.time);
                            const diff = Math.abs(selectedMins - existingMins);
                            if (diff <= 195)
                                return true; // trong 3h15p
                        }
                    }
                    return false;
                }

                function validate() {
                    const date = dateEl.value;
                    const time = timeEl.value;

                    if (!date || !time) {
                        showMessage('Please select a date and time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    const hour = parseInt(time.split(':')[0]);
                    if (hour < 5 || hour >= 22) {
                        showMessage('Không thể đặt trong khoảng 22:00 - 05:00.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    if (isConflict(date, time)) {
                        showMessage('This time has already been booked. Please choose another time slot.', 'danger');
                        btnSubmit.disabled = true;
                    } else {
                        showMessage('Available time.', 'success');
                        btnSubmit.disabled = false;
                    }
                }


                dateEl.addEventListener('change', validate);
                timeEl.addEventListener('change', validate);
                setTimeout(validate, 300);

                document.getElementById('bookingForm').addEventListener('submit', function (e) {
                    if (btnSubmit.disabled) {
                        e.preventDefault();
                        showMessage('Unable to submit because the time is unavailable.', 'danger');
                    }
                });
            });
        </script>

    </body>
</html>
