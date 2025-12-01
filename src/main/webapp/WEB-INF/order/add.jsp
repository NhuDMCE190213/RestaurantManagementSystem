<%-- 
    Document   : add
    Created on : 2 Nov 2025, 10:59:23 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Order - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Order</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="order"/>">
                <table class="table table align-middle admin-table">
                    <tr>
                        <td>
                        </td>
                        <td>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="table" class="form-label">Table</label>
                        </th>
                        <td>
                            <select name="tableId" class="form-select">                                
                                <c:forEach var="table" items="${tablesList}">
                                    <option value="${table.id}" class="form-options">
                                        <c:out value="${table.number}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <!--Lay employee trong session-->

                    <tr>
                        <th>
                            <label for="employee" class="form-label">Employee</label>
                        </th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty sessionScope.employeeSession}">

                                    <label id="employee" class="form-control">
                                        <c:out value="${sessionScope.employeeSession.empName}"/>
                                    </label>
                                    <input type="hidden" name="empId" value="${sessionScope.employeeSession.empId}">
                                </c:when>
                                <c:otherwise>
                                    <label class="form-control" style="color: red">Please login employee account</label>
                                </c:otherwise>
                            </c:choose>
                        <td>
                    </tr>

                    <tr>
                        <th>
                            <label for="voucher" class="form-label">Voucher</label>
                        </th>
                        <td>
                            <select name="voucherId" class="form-select">
                                <option value="0" class="form-options">None</option>
                                <c:forEach var="voucher" items="${vouchersList}">
                                    <option value="${voucher.voucherId}" class="form-options">
                                        <c:out value="${voucher.voucherCode}"/> (<c:out value="${voucher.currentDiscount}"/>)
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="paymentMethod" class="form-label">Payment Method</label>
                        </th>
                        <td>
                            <select name="paymentMethod" class="form-select" required>
                                <option class="form-options">Cash</option>
                                <option class="form-options">Pay later</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="order"/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>