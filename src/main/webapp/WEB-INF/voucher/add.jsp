<%-- 
    Document   : add
    Created on : Oct 11, 2025, 5:21:51 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="title" value="Add Voucher - Yummy"/>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add New Voucher</h1>
        </div>

        <div class="container">
            <%-- Popup modal markup (used for validation/SQL errors when forwarded) --%>
            <c:if test="${not empty popupMessage}">
                <div id="popupData" data-status="${popupStatus}" data-message="<c:out value='${popupMessage}'/>" style="display:none"></div>
            </c:if>

            <%-- Custom styles: make modal background white and header light gray like the screenshot --%>
            <style>
                /* ensure modal interior is white */
                .modal-content {
                    background: #ffffff;
                }

                /* header light gray with dark text like the screenshot */
                .modal-header {
                    background: #f1f1f1;
                    color: #333;
                    border-bottom: 1px solid #ddd;
                }

                /* modal content subtle border */
                .modal-content.popup-highlight {
                    border: 1px solid rgba(0,0,0,0.08);
                }

                /* message styles: plain white body with colored text */
                .popup-message-success { color: #198754; /* green */ }
                .popup-message-error { color: #bd2130; /* red */ }

                /* smaller, less prominent close button (match screenshot tone) */
                .modal-footer .btn-secondary {
                    background: #6c757d;
                    border-color: #6c757d;
                }
            </style>

            <div class="modal fade" id="popupModal" tabindex="-1" aria-labelledby="popupModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="popupModalLabel">Notification</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Message set by JS -->
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <form method="post" action="<c:url value='voucher'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                        <th><label for="voucherCode">Code</label></th>
                        <td><input type="text" name="voucher_code" id="voucherCode" class="form-control" required value="${param.voucher_code != null ? param.voucher_code : ''}"></td>
                    </tr>

                    <tr>
                        <th><label for="voucherName">Name</label></th>
                        <td><input type="text" name="voucher_name" id="voucherName" class="form-control" required value="${param.voucher_name != null ? param.voucher_name : ''}"></td>
                    </tr>

                    <tr>
                        <th><label for="discountType">Type</label></th>
                        <td>
                            <select name="discount_type" id="discountType" class="form-control" onchange="onDiscountTypeChange()" required>
                                <option value="Amount" ${param.discount_type == null || param.discount_type == 'Amount' ? 'selected' : ''}>Amount</option>
                                <option value="Percent" ${param.discount_type == 'Percent' ? 'selected' : ''}>Percent</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="discountValue">Discount Value</label></th>
                        <td>
                            <div class="input-group">
                                <input type="number" step="0.01" min="0" name="discount_value" id="discountValue" class="form-control" required value="${param.discount_value != null ? param.discount_value : ''}">
                                <span class="input-group-text d-none" id="percentSuffix">%</span>
                            </div>
                            <small class="text-muted">If type = Percent, enter integer percent (e.g. 20 for 20%). If Amount, enter the amount (e.g. 40000).</small>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="quantity">Quantity</label></th>
                        <td><input type="number" name="quantity" id="quantity" class="form-control" required value="${param.quantity != null ? param.quantity : ''}"></td>
                    </tr>

                    <tr>
                        <th><label for="startDate">Start Date</label></th>
                        <td><input type="date" name="start_date" id="startDate" class="form-control" required value="${param.start_date != null ? param.start_date : ''}"></td>
                    </tr>

                    <tr>
                        <th><label for="endDate">End Date</label></th>
                        <td><input type="date" name="end_date" id="endDate" class="form-control" required value="${param.end_date != null ? param.end_date : ''}"></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='voucher'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<script>
    function onDiscountTypeChange() {
        var type = document.getElementById('discountType').value;
        var valueInput = document.getElementById('discountValue');
        var percentSuffix = document.getElementById('percentSuffix');
        if (type === 'Percent') {
            valueInput.step = "1";
            valueInput.max = "100";
            valueInput.min = "0";
            percentSuffix.classList.remove('d-none');
        } else {
            valueInput.step = "0.01";
            valueInput.removeAttribute('max');
            valueInput.min = "0";
            percentSuffix.classList.add('d-none');
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        onDiscountTypeChange();

        // Show modal if popupData exists
        var popupData = document.getElementById('popupData');
        if (!popupData) return;

        var status = popupData.dataset.status === 'true';
        var message = popupData.dataset.message || '';
        var modalEl = document.getElementById('popupModal');
        if (!modalEl) {
            alert(message);
            return;
        }

        var header = modalEl.querySelector('.modal-header');
        var title = modalEl.querySelector('.modal-title');
        var body = modalEl.querySelector('.modal-body');
        var content = modalEl.querySelector('.modal-content');

        // reset classes
        header.style.background = '';
        header.style.color = '';
        content.classList.remove('popup-content-success', 'popup-content-error', 'popup-highlight');

        // Apply look: white body, light-gray header, and colored message text (no colored alert background)
        content.classList.add('popup-highlight');
        header.style.background = '#f1f1f1'; // light gray
        header.style.color = '#333';

        // Message: plain paragraph with colored text (green/red)
        if (status) {
            title.textContent = 'Action Successful';
            body.innerHTML = '<p class="popup-message-success mb-0">' + message + '</p>';
        } else {
            title.textContent = 'Error';
            body.innerHTML = '<p class="popup-message-error mb-0">' + message + '</p>';
        }

        // Show modal (Bootstrap 5)
        try {
            var popupModal = new bootstrap.Modal(modalEl);
            popupModal.show();
        } catch (e) {
            alert(message);
        }
    });
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>