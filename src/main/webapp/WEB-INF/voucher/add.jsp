<%-- 
    Document   : add
    Created on : Oct 11, 2025, 5:21:51 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Voucher - Yummy"/>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add New Voucher</h1>
        </div>

        <div class="container">
            <form method="post" action="<c:url value='voucher'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                        <th><label for="voucherCode">Code</label></th>
                        <td><input type="text" name="voucher_code" id="voucherCode" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="voucherName">Name</label></th>
                        <td><input type="text" name="voucher_name" id="voucherName" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="discountType">Type</label></th>
                        <td>
                            <select name="discount_type" id="discountType" class="form-control" onchange="onDiscountTypeChange()" required>
                                <option value="Amount" selected>Amount</option>
                                <option value="Percent">Percent</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="discountValue">Discount Value</label></th>
                        <td>
                            <div class="input-group">
                                <input type="number" step="0.01" min="0" name="discount_value" id="discountValue" class="form-control" required>
                                <span class="input-group-text d-none" id="percentSuffix">%</span>
                            </div>
                            <small class="text-muted">If type = Percent, enter integer percent (e.g. 20 for 20%). If Amount, enter the amount (e.g. 40000).</small>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="quantity">Quantity</label></th>
                        <td><input type="number" name="quantity" id="quantity" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="startDate">Start Date</label></th>
                        <td><input type="date" name="start_date" id="startDate" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="endDate">End Date</label></th>
                        <td><input type="date" name="end_date" id="endDate" class="form-control" required></td>
                    </tr>

<!--                    <tr>
                        <th><label for="status">Status</label></th>
                        <td>
                            <select name="status" id="status" class="form-control" required>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                            </select>
                        </td>
                    </tr>-->

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
    document.addEventListener('DOMContentLoaded', onDiscountTypeChange);
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>