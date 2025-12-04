<%-- 
    Document   : add
    Created on : 22 Nov 2025, 5:24:54 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Order - Yummy"/>

<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" style="padding-left: 200px" aria-label="Listing table">
    <div class="content-card shadow-sm ">
        <div class="border-0 px-4 py-3">
            <div class="card-header">
                <h1 class="section-title mb-1 text-start">Add Order</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="myReservationOrder"/>">
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

                            <c:choose>
                                <c:when test="${not empty reservationsList}">
                                    <select name="reservationId" class="form-select">                                
                                        <c:forEach var="reservation" items="${reservationsList}">
                                            <option value="${reservation.reservationId}" class="form-options">
                                                <c:out value="(Date: ${reservation.reservationDate} - Time: ${reservation.timeStart} ~ ${reservation.timeEnd} )"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="reservationId" value="${param.reservationId}">
                                    <label for="reservationId" class="form-control">
                                        <c:out value="(Date: ${currentReservation.reservationDate} - Time: ${currentReservation.timeStart} ~ ${currentReservation.timeEnd} )"/>
                                    </label>
                                </c:otherwise>
                            </c:choose>
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
                                <option class="form-options">Pay at Ordering</option>
                                <option class="form-options">Pay after Dining</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="myReservationOrder">
                                   <c:param name="view" value="list"/>
                                   <c:param name="reservationId" value="${param.reservationId}"/>
                               </c:url>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
