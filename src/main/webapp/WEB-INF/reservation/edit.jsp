<%-- 
    Document   : edit
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
        <title>Edit Reservation - Table <%= (selected != null) ? selected.getNumber() : ""%></title>

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
                Edit Reservation - Table <%= (selected != null) ? selected.getNumber() : ""%>
            </h4>

            <form id="bookingForm" action="<c:url value='/my-reservation'/>" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="reservationId" value="${currentReservation.reservationId}">
                <input type="hidden" name="customerId" value="${sessionScope.customerSession.customerId}">
                <input type="hidden" name="from" value="mylist">

                <!-- FIXED Duplicate tableId -->
                <div class="mb-3">
                    <label class="form-label">Table</label>
                    <input type="number" class="form-control" value="${currentReservation.table.id}" readonly>
                    <input type="hidden" name="tableId" value="${currentReservation.table.id}">
                    <small class="text-muted">Current Table Number: ${currentReservation.table.number}</small>
                </div>

                <div class="mb-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" id="reservationDate" class="form-control"
                           required value="${currentReservation.reservationDate}">
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
                    <label class="form-label">Voucher (optional)</label>
                    <select class="form-select" name="voucherId">
                        <option value="">-- No voucher --</option>

                        <c:forEach var="v" items="${voucherList}">
                            <option value="${v.voucherId}"
                                    <c:if test="${not empty currentReservation.voucher && currentReservation.voucher.voucherId == v.voucherId}">
                                        selected
                                    </c:if>
                                    >
                                ${v.voucherCode} - ${v.voucherName} (${v.currentDiscount})
                            </option>
                        </c:forEach>
                    </select>
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

                    <!-- FIXED: không dùng <li> -->
                    <a class="btn btn-outline-secondary"
                       href="<c:url value='/my-reservation'>
                           <c:param name='view' value='mylist'/>
                           <c:param name='customerId' value='${sessionScope.customerSession.customerId}'/>
                       </c:url>">
                        Cancel
                    </a>
                </div>
            </form>
        </div>

        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <script>
            const currentReservationId = ${currentReservation.reservationId};

            const existingReservations = [
            <c:forEach var="r" items="${existingReservations}" varStatus="loop">
            {
            id: ${r.reservationId},
                    date: '${r.reservationDate}',
                    timeStart: '${r.timeStart}'
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];
        </script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const today = new Date().toISOString().split('T')[0];

                const dateEl = document.getElementById('reservationDate');
                const timeStartEl = document.getElementById('timeStart');
                const timeEndEl = document.getElementById('timeEnd');

                const btnSubmit = document.getElementById('btnSubmit');
                const availMsg = document.getElementById('availabilityMsg');

                // Edit thì nếu bạn muốn vẫn cho chọn ngày quá khứ -> bỏ dòng này
                dateEl.setAttribute('min', today);

                // ✅ Khóa luôn trên UI
                timeStartEl.min = "05:00";
                timeStartEl.max = "21:59";
                timeEndEl.min = "05:00";
                timeEndEl.max = "22:00";

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
                    const h = parseInt(parts[0], 10) || 0;
                    const m = parseInt(parts[1], 10) || 0;
                    return h * 60 + m;
                }

                function isConflict(selectedDate, selectedTime) {
                    if (!selectedDate || !selectedTime)
                        return false;

                    const selectedMins = toMinutes(selectedTime);

                    for (const r of existingReservations) {
                        if (r.id === currentReservationId)
                            continue; // ✅ bỏ qua chính nó

                        if (r.date === selectedDate) {
                            const existingMins = toMinutes(r.timeStart);
                            const diff = Math.abs(selectedMins - existingMins);

                            if (diff <= 195)
                                return true; // trong 3h15p
                        }
                    }
                    return false;
                }

                function validate() {
                    const date = dateEl.value;
                    const start = timeStartEl.value;
                    const end = timeEndEl.value;

                    if (!date || !start || !end) {
                        showMessage('Please select date, start time and end time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    const startM = toMinutes(start);
                    const endM = toMinutes(end);

                    // ✅ Chỉ cho đặt trong 05:00 -> 22:00
                    // Start: [05:00, 22:00)
                    if (startM < 5 * 60 || startM >= 22 * 60) {
                        showMessage('Start time must be between 05:00 and 21:59.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    // End: (05:00, 22:00] và phải > start
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

                    // ✅ Check conflict theo start time (như bạn đang làm)
                    if (isConflict(date, start)) {
                        showMessage('This time has already been booked. Please choose another time slot.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    showMessage('Available time.', 'success');
                    btnSubmit.disabled = false;
                }

                // ✅ Bắt event cho cả 3 input
                dateEl.addEventListener('change', validate);
                timeStartEl.addEventListener('change', validate);
                timeEndEl.addEventListener('change', validate);

                timeStartEl.addEventListener('input', validate);
                timeEndEl.addEventListener('input', validate);

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
