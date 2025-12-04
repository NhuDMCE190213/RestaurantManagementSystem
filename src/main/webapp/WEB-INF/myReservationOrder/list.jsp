<%-- 
    Document   : listByReservation
    Created on : 3 Dec 2025, 7:33:33 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="My Order List - Yummy"/>

<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" style="padding-left: 200px" aria-label="Listing table">
    <div class="content-card shadow-sm ">
        <div class="border-0 px-4 py-3">
            <div class="card-header">
                <h1 class="section-title mb-1 text-start">My Order list</h1>
            </div>

            <c:choose>
                <c:when test="${not empty currentReservation}">
                    <div class=" card-body row g-3 g-md-4 mb-4">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Table</small>
                                <p class="fs-5 fw-semibold mb-0"><c:out value='${currentReservation.table.number}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Date</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.reservationDate}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Time Start</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.timeStart}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Time End</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.timeEnd}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Status</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.status}'/></p>
                            </div>
                        </div>
                        <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                            <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                                <a class="btn btn-primary add-btn" href="<c:url value="myReservationOrder">
                                       <c:param name="view" value="add"/>
                                       <c:param name="reservationId" value="${currentReservation.reservationId}"/>
                                   </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-body px-4 pb-4">
                            <div class="alert alert-warning mb-3" role="alert">
                                The reservation not found. Please check information again.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th width="20%" scope="col">Table</th>
                            <th width="20%" scope="col">Voucher</th>
                            <th width="15%" scope="col">Date</th>
                            <th width="15%" scope="col">Time</th>
                            <th width="10%" scope="col">Payment</th>
                            <th width="15%" scope="col">Status</th>
                            <th width="20%"scope="col" class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${ordersList == null || empty ordersList}">
                                <tr>
                                    <td colspan="9" style="color:red;">No data to display</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="order" items="${ordersList}" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${order.reservation.table.number}"/></td>
                                        <td><c:out value="${order.voucher.voucherCode}"/></td>
                                        <td><c:out value="${order.orderDate}"/></td>
                                        <td><c:out value="${order.orderTime}"/></td>
                                        <td><c:out value="${order.paymentMethod}"/></td>
                                        <td><c:out value="${order.status}"/></td>

                                        <td class="text-end">
                                            <div class="action-button-group d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-success btn-icon btn-view"
                                                   title="View details" aria-label="View details"
                                                   href="<c:url value="myReservationOrderItem">
                                                       <c:param name="view" value="list"/>
                                                       <c:param name="orderId" value="${order.orderId}"/>
                                                   </c:url>">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <form action="<c:url value="myReservationOrder">
                                                          <c:param name="orderId" value="${order.orderId}"/>
                                                          <c:param name="reservationId" value="${param.reservationId}"/>
                                                      </c:url>" method="post">
                                                    <c:if test="${order.status eq 'Pending'}">
                                                        <button class="btn btn-outline-danger btn-icon btn-delete"
                                                                title="Cancel" aria-label="Cancel"
                                                                type="submit" name="action" value="cancel">
                                                            <i class="bi bi-x-circle"></i>
                                                        </button>
                                                    </c:if>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <nav aria-label="Page navigation example">
                    <ul class="pagination">
                        <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                            <a class="page-link" href="<c:url value="/myReservationOrder">
                                   <c:param name="view" value="list"/>
                                   <c:param name="reservationId" value="${param.reservationId}"/>
                                   <c:param name="page" value="${param.page - 1}"/>
                               </c:url>" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                                <a class="page-link" href="<c:url value="/myReservationOrder">
                                       <c:param name="view" value="list"/>
                                   <c:param name="reservationId" value="${param.reservationId}"/>
                                       <c:param name="page" value="${i}"/>
                                   </c:url>">${i}</a></li>
                            </c:forEach>
                        <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                            <a class="page-link" href="<c:url value="/myReservationOrder">
                                   <c:param name="reservationId" value="${param.reservationId}"/>
                                   <c:param name="view" value="list"/>
                                   <c:param name="page" value="${(empty param.page)?2:param.page + 1}"/>
                               </c:url>" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>

