<%-- 
    Document   : edit
    Created on : 3 Nov 2025, 7:26:34 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Edit Order - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Order</h1>
            </div>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentOrder}">
                    <form method="post" action="<c:url value="order">
                              <c:param name="orderId" value="${param.id}"/>  
                          </c:url>">
                        <table class="table">
                            <tr>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="orderId" class="form-label">Order ID</label>
                                </th>
                                <td>
                                    <label id="orderId" class="form-control"><c:out value="${currentOrder.orderId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="tableId" class="form-label">Table</label>
                                </th>
                                <td>
                                    <%--not fix reservation yet--%>
                                    <select name="tableId" class="form-select">                                
                                        <c:forEach var="table" items="${tablesList}">
                                            <option value="${table.id}" class="form-options">
                                                <c:out value="${table.number}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

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
                                            <option value="${voucher.voucherId}" class="form-options" ${(currentOrder.voucher.voucherId eq voucher.voucherId)?'selected':''}>
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
                                        <option class="form-options" ${(currentOrder.paymentMethod eq 'Cash')?'Selected':''}>Cash</option>
                                        <option class="form-options" ${(currentOrder.paymentMethod eq 'Pay later')?'Selected':''}>Pay later</option>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value="order"/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the Order which id <c:out value="${param.id}"/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value="order"/>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>

