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

                <!-- MESSAGE -->
                <div id="availabilityMsg" class="mb-3"></div>

                <div class="d-flex justify-content-between">
                    <button type="submit" id="btnSubmit" class="btn btn-confirm">Confirm</button>
                    <a href="${pageContext.request.contextPath}/reservation?view=bookatable" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- DATA reservation đã có của bàn này -->
        <script>
            const existingReservations = [
            <c:forEach var="r" items="${existingReservations}" varStatus="loop">
            {
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

                const customerSelect = document.getElementById('customerSelect');
                const searchPhoneInput = document.getElementById('searchPhone');
                const noCustomerHint = document.getElementById('noCustomerHint');
                const quickCustomerPhone = document.getElementById('quickCustomerPhone');

                // LƯU DANH SÁCH OPTION GỐC (chỉ khai báo 1 lần)
                const originalCustomerOptions = Array.from(customerSelect.options);

                dateEl.setAttribute('min', today);

                // SEARCH THEO SĐT
                searchPhoneInput.addEventListener('keyup', function () {
                    const keyword = this.value.trim().toLowerCase();

                    // giữ lại option đầu tiên "-- Select customer --"
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

                    // nếu không tìm thấy ai và có nhập số điện thoại -> hiện gợi ý tạo mới
                    if (keyword.length >= 3 && matchCount === 0) {
                        noCustomerHint.style.display = 'block';
                        if (quickCustomerPhone) {
                            quickCustomerPhone.value = this.value;   // đổ sẵn vào modal
                        }
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

                function toMinutes(timeStr) {
                    if (!timeStr)
                        return 0;
                    const parts = timeStr.split(':');
                    return parseInt(parts[0]) * 60 + parseInt(parts[1]);
                }

                // Check trùng (cùng ngày, lệch <= 195 phút)
                function isConflict(selectedDate, selectedTime) {
                    if (!selectedDate || !selectedTime)
                        return false;
                    const selectedMins = toMinutes(selectedTime);
                    for (const r of existingReservations) {
                        if (r.date === selectedDate) {
                            const existingMins = toMinutes(r.timeStart);
                            const diff = Math.abs(selectedMins - existingMins);
                            if (diff <= 195)
                                return true;
                        }
                    }
                    return false;
                }

                function validate() {
                    const date = dateEl.value;
                    const timeStart = timeStartEl.value;
                    const timeEnd = timeEndEl.value;

                    if (!date || !timeStart || !timeEnd) {
                        showMessage('Please select a date and time.', 'danger');
                        btnSubmit.disabled = true;
                        return;
                    }

                    // Giờ mở cửa 05:00 - 22:00
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

                    if (isConflict(date, timeStart)) {
                        showMessage('This time has already been booked. Please choose another time slot.', 'danger');
                        btnSubmit.disabled = true;
                    } else {
                        showMessage('Available time.', 'success');
                        btnSubmit.disabled = false;
                    }
                }

                dateEl.addEventListener('change', validate);
                timeStartEl.addEventListener('change', validate);
                timeEndEl.addEventListener('change', validate);

                document.getElementById('createForm').addEventListener('submit', function (e) {
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

