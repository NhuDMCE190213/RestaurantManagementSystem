<%-- 
    Document   : create
    Created on : Dec 8, 2025, 3:09:46 PM
    Author     : Tiêu Gia Huy - CE191594
--%>

<%@page import="model.Table"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
    Table selected = (Table) request.getAttribute("selectedTable");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Reservation - <%=(selected != null) ? selected.getNumber() : ""%></title>

        <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .booking-container {
                max-width: 500px;
                margin: 80px auto;
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
                Create Reservation 
                <% if (selected != null) {%> - Table <%= selected.getNumber()%><% }%>
            </h4>

            <form id="createForm" action="${pageContext.request.contextPath}/reservation?action=add" method="post">
                <!-- gửi tableId -->
                <input type="hidden" name="tableId" id="tableId" value="<%= (selected != null) ? selected.getId() : ""%>">

                <!-- SEARCH BY PHONE -->
                <div class="mb-3">
                    <label class="form-label">Search by phone</label>
                    <input type="text" id="searchPhone" class="form-control" placeholder="Enter phone number to filter customers...">
                    <!-- Hiện khi không tìm được khách -->
                    <div id="noCustomerHint" class="form-text text-danger mt-1" style="display:none;">
                        No customer found with this phone.
                        <button type="button" class="btn btn-link p-0 ms-1" 
                                data-bs-toggle="modal" data-bs-target="#quickCustomerModal">
                            Create new customer
                        </button>
                    </div>
                </div>

                <!-- CUSTOMER SELECT -->
                <div class="mb-3">
                    <label class="form-label">Customer</label>
                    <select name="customerId" id="customerSelect" class="form-control" required>
                        <option value="">-- Select customer --</option>
                        <c:forEach var="c" items="${listCustomer}">
                            <option value="${c.customerId}"
                                    data-phone="${c.phoneNumber}"
                                    ${param.newCustomerId == c.customerId ? "selected" : ""}>
                                ${c.customerName} (${c.phoneNumber})
                            </option>
                        </c:forEach>    
                    </select>

                </div>

                <!-- TABLE INFO (READONLY) -->
                <div class="mb-3">
                    <label class="form-label">Table</label>
                    <input type="text" class="form-control" value="Table <%=(selected != null) ? selected.getNumber() : ""%> (capacity: <%=(selected != null) ? selected.getCapacity() : 0%> Guests)" disabled>
                </div>

                <!-- DATE -->
                <div class="mb-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" id="reservationDate" class="form-control" required>
                </div>

                <!-- START TIME -->
                <div class="mb-3">
                    <label class="form-label">Start Time</label>
                    <input type="time" name="timeStart" id="timeStart" class="form-control" required>
                    <p class="text-muted mb-1" style="font-size: 13px;">
                        Cannot book table a time between 05:00 and 22:00
                    </p>
                </div>

                <!-- END TIME -->
                <div class="mb-3">
                    <label class="form-label">End Time</label>
                    <input type="time" name="timeEnd" id="timeEnd" class="form-control" required>
                </div>

                <label>Description (Health / Food note)</label>
                <textarea name="description" class="form-control"
                          placeholder="Eg: Vegetarian, no spicy, seafood allergy..."></textarea>

                <c:if test="${not empty existingReservations}">
                    <div class="mt-3">
                        <label class="form-label fw-semibold">Booked time slots (Approved / Serving)</label>
                        <div class="table-responsive">
                            <table class="table table-sm table-bordered align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th style="width: 40%;">Date</th>
                                        <th style="width: 30%;">Start</th>
                                        <th style="width: 30%;">End</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="r" items="${existingReservations}">
                                        <tr>
                                            <td>${r.reservationDate}</td>
                                            <td>${r.timeStart}</td>
                                            <td>${r.timeEnd}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <!-- MESSAGE -->
                <div id="availabilityMsg" class="mb-3"></div>

                <div class="d-flex justify-content-between">
                    <button type="submit" id="btnSubmit" class="btn btn-confirm">Confirm</button>
                    <a href="${pageContext.request.contextPath}/reservation?view=bookatable" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <c:if test="${not empty sessionScope.popupMessage}">
            <div class="modal fade show" id="popupModal" tabindex="-1" style="display:block; background:rgba(0,0,0,.4);">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">
                                <c:choose>
                                    <c:when test="${sessionScope.popupStatus}">Success</c:when>
                                    <c:otherwise>Action Fail</c:otherwise>
                                </c:choose>
                            </h5>
                            <a href="${pageContext.request.contextPath}/reservation?view=add&tableId=${param.tableId}" class="btn-close"></a>
                        </div>
                        <div class="modal-body">
                            <p style="color:${sessionScope.popupStatus ? 'green' : 'red'};">
                                ${sessionScope.popupMessage}
                            </p>
                        </div>
                        <div class="modal-footer">
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reservation?view=add&tableId=${param.tableId}">
                                Close
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <%-- clear popup after show --%>
            <%
                session.removeAttribute("popupMessage");
                session.removeAttribute("popupStatus");
            %>
        </c:if>


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
                const timeStartEl = document.getElementById('timeStart');
                const timeEndEl = document.getElementById('timeEnd');
                const btnSubmit = document.getElementById('btnSubmit');
                const availMsg = document.getElementById('availabilityMsg');

                const customerSelect = document.getElementById('customerSelect');
                const searchPhoneInput = document.getElementById('searchPhone');
                const noCustomerHint = document.getElementById('noCustomerHint');
                const quickCustomerPhone = document.getElementById('quickCustomerPhone');

                const originalCustomerOptions = Array.from(customerSelect.options);

                dateEl.setAttribute('min', today);

                // SEARCH THEO SĐT
                searchPhoneInput.addEventListener('keyup', function () {
                    const keyword = this.value.trim().toLowerCase();

                    customerSelect.innerHTML = '';
                    customerSelect.appendChild(originalCustomerOptions[0]);

                    let matchCount = 0;

                    for (let i = 1; i < originalCustomerOptions.length; i++) {
                        const opt = originalCustomerOptions[i];
                        const phone = (opt.getAttribute('data-phone') || '').toLowerCase();
                        if (phone.includes(keyword)) {
                            customerSelect.appendChild(opt);
                            matchCount++;
                        }
                    }

                    if (keyword.length >= 3 && matchCount === 0) {
                        noCustomerHint.style.display = 'block';
                        if (quickCustomerPhone)
                            quickCustomerPhone.value = this.value;
                    } else {
                        noCustomerHint.style.display = 'none';
                    }
                });

                function showMessage(text, type) {
                    const cls = type === 'success' ? 'text-success'
                            : type === 'danger' ? 'text-danger'
                            : 'text-muted';
                    availMsg.innerHTML = '<span class="' + cls + '">' + text + '</span>';
                }

                function toMinutes(t) {
                    if (!t)
                        return 0;
                    const parts = t.split(':');
                    const h = parseInt(parts[0], 10) || 0;
                    const m = parseInt(parts[1], 10) || 0;
                    return h * 60 + m;
                }

                function isOverlap(aStart, aEnd, bStart, bEnd) {
                    return aStart < bEnd && aEnd > bStart;
                }

                // ✅ Check trùng theo overlap + trừ 15' cho slot đã đặt
                function isConflict(selectedDate, selectedStart, selectedEnd) {
                    if (!selectedDate || !selectedStart || !selectedEnd)
                        return false;

                    const ns = toMinutes(selectedStart);
                    const ne = toMinutes(selectedEnd);

                    for (const r of existingReservations) {
                        if (r.date === selectedDate) {
                            const es = toMinutes(r.start) - 15; // trừ 15 phút
                            const ee = toMinutes(r.end);        // end giữ nguyên
                            if (isOverlap(ns, ne, es, ee))
                                return true;
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

                    // Giờ mở cửa 05:00 - 22:00
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
                        showMessage('End time must be later than start time.', 'danger');
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
                timeStartEl.addEventListener('change', validate);
                timeEndEl.addEventListener('change', validate);

                timeStartEl.addEventListener('input', validate);
                timeEndEl.addEventListener('input', validate);

                setTimeout(validate, 100);

                document.getElementById('createForm').addEventListener('submit', function (e) {
                    validate();
                    if (btnSubmit.disabled) {
                        e.preventDefault();
                        showMessage('Unable to submit because the time is unavailable.', 'danger');
                    }
                });
            });
        </script>

        <!-- Quick create customer modal -->
        <div class="modal fade" id="quickCustomerModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/quick-customer" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Create New Customer</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <!-- giữ tableId để redirect về đúng reservation -->
                            <input type="hidden" name="tableId" value="<%= (selected != null) ? selected.getId() : ""%>">

                            <div class="mb-3">
                                <label class="form-label">Customer Name</label>
                                <input type="text" name="customerName" id="quickCustomerName" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="text" name="phoneNumber" id="quickCustomerPhone" class="form-control" required>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-danger">Create</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>

