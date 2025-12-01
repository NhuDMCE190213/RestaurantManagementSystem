<%-- 
    Document   : list
    Created on : 7 Nov 2025, 12:41:23 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="My Order List - Yummy"/>

<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="d-flex justify-content-center align-items-center vh-100 bg-light">
    <section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
        <div class="content-card shadow-sm">
            <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
                <div>
                    <h1 class="section-title mb-1">My Order list</h1>
                </div>
                <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                    <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                        <a class="btn btn-primary add-btn" href="<c:url value="myOrder">
                               <c:param name="view" value="addOrder"/>
                           </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                    </div>
                </div>
            </div>

            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th width="10%" scope="col">Table Name</th>
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
                                                   href="<c:url value="myOrder">
                                                       <c:param name="view" value="detail"/>
                                                       <c:param name="orderId" value="${order.orderId}"/>
                                                   </c:url>">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <form action="<c:url value="myOrder">
                                                          <c:param name="orderId" value="${order.orderId}"/>
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
                            <a class="page-link" href="<c:url value="/myOrder">
                                   <c:param name="view" value="list"/>
                                   <c:param name="page" value="${param.page - 1}"/>
                               </c:url>" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                                <a class="page-link" href="<c:url value="/myOrder">
                                       <c:param name="view" value="list"/>
                                       <c:param name="page" value="${i}"/>
                                   </c:url>">${i}</a></li>
                            </c:forEach>
                        <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                            <a class="page-link" href="<c:url value="/myOrder">
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
    </section>
</main>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
