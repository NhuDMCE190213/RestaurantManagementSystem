<%-- 
    Document   : create
    Created on : Dec 8, 2025, 3:09:46 PM
    Author     : Tiêu Gia Huy - CE191594
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Reservation</title>

        <!-- Bootstrap giống add.jsp -->
        <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

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
            <h4 class="text-center mb-4">Create Reservation (Employee)</h4>

            <form id="createForm" action="${pageContext.request.contextPath}/reservation?action=add" method="post">

                <!-- CUSTOMER -->
                <div class="mb-3">
                    <label class="form-label">Customer</label>
                    <select name="customerId" class="form-control" required>
                        <option value="">-- Select customer --</option>
                        <c:forEach var="c" items="${listCustomer}">
                            <option value="${c.customerId}">
                                ${c.customerName} (${c.phoneNumber})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- TABLE -->
                <div class="mb-3">
                    <label class="form-label">Table</label>
                    <select name="tableId" id="tableId" class="form-control" required>
                        <option value="">-- Select table --</option>
                        <c:forEach var="t" items="${listTable}">
                            <option value="${t.id}">
                                Table ${t.number} (${t.capacity} seats)
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- DATE -->
                <div class="mb-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" id="reservationDate" class="form-control" required>
                </div>

                <!-- TIME START -->
                <div class="mb-3">
                    <label class="form-label">Start Time</label>
                    <input type="time" name="timeStart" id="timeStart" class="form-control" required>
                    <p class="text-muted mb-1" style="font-size:13px;">
                        Cannot book time between 05:00 and 22:00
                    </p>
                </div>

                <!-- TIME END -->
                <div class="mb-3">
                    <label class="form-label">End Time</label>
                    <input type="time" name="timeEnd" id="timeEnd" class="form-control" required>
                </div>

                <!-- MESSAGE -->
                <div id="availabilityMsg" class="mb-3"></div>

                <!-- BUTTON -->
                <div class="d-flex justify-content-between">
                    <button type="submit" id="btnSubmit" class="btn btn-confirm">Confirm</button>
                    <a href="${pageContext.request.contextPath}/reservation" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <!-- Bootstrap JS -->
        <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- DATA CHECK TRÙNG -->
        <script>
            const existingReservations = [
            <c:forEach var="r" items="${existingReservations}" varStatus="loop">
            {
            tableId: '${r.table.id}',
                    date: '${r.reservationDate}',
                    timeStart: '${r.timeStart}'
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];
        </script>

        <!-- VALIDATE GIỜ -->
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const today = new Date().toISOString().split('T')[0];

                const tableEl = document.getElementById('tableId');
                const dateEl = document.getElementById('reservationDate');
                const timeStartEl = document.getElementById('timeStart');
                const timeEndEl = document.getElementById('timeEnd');
                const btnSubmit = document.getElementById('btnSubmit');
                const availMsg = document.getElementById('availabilityMsg');

                dateEl.setAttribute('min', today);

                function showMessage(text, type) {
                    const cls = type === 'success' ? 'text-success'
                            : type === 'danger' ? 'text-danger'
                            : 'text-muted';
                    availMsg.innerHTML = '<span class="' + cls + '">' + text + '</span>';
                }

                function toMinutes(timeStr) {
                    const parts = timeStr.split(':');
                    return parseInt(parts[0]) * 60 + parseInt(parts[1]);
                }

                function isConflict(tableId, selectedDate, selectedTime) {
                    const selectedMins = toMinutes(selectedTime);
                    for (const r of existingReservations) {
                        if (r.tableId === tableId && r.date === selectedDate) {
                            const existingMins = toMinutes(r.timeStart);
                            if (Math.abs(selectedMins - existingMins) <= 195)
                                return true;
                        }
                    }
                    return false;
                }

                function validate() {
                    const tableId = tableEl.value;
                    const date = dateEl.value;
                    const timeStart = timeStartEl.value;
                    const timeEnd = timeEndEl.value;

                    if (!tableId || !date || !timeStart || !timeEnd) {
                        showMessage('Please select full information.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    if (timeStart < '05:00' || timeStart >= '22:00') {
                        showMessage('Cannot book between 22:00 - 05:00.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    if (timeEnd <= timeStart) {
                        showMessage('End time must be later than start time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    if (isConflict(tableId, date, timeStart)) {
                        showMessage('This time slot is already booked.', 'danger');
                        btnSubmit.disabled = true;
                    } else {
                        showMessage('Available time.', 'success');
                        btnSubmit.disabled = false;
                    }
                }

                tableEl.addEventListener('change', validate);
                dateEl.addEventListener('change', validate);
                timeStartEl.addEventListener('change', validate);
                timeEndEl.addEventListener('change', validate);
            });
        </script>

    </body>
</html>
