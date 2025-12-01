<%-- 
    Document   : add
    Created on : 22 Nov 2025, 5:24:54 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Order - Yummy"/>

<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section d-flex align-items-center justify-content-center" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Order</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="myOrder"/>">
                <table class="table table align-middle admin-table">
                    <tr>
                        <td>
                        </td>
                        <td>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="reservation" class="form-label">Reservation</label>
                        </th>
                        <td>
                            <select name="reservationId" class="form-select">                                
                                <c:forEach var="reservation" items="${reservationsList}">
                                    <option value="${reservation.reservationId}" class="form-options">
                                        <c:out value="(Date: ${reservation.reservationDate} - Time: ${reservation.reservationTime})"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
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
                            <button class="btn btn-outline-success" type="submit" name="action" value="addOrder">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="myOrder"/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
