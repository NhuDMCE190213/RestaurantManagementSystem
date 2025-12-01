<%-- 
    Document   : list
    Created on : 2 Nov 2025, 9:05:59 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Order List - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Order list</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <a class="btn btn-primary add-btn" href="<c:url value="order">
                           <c:param name="view" value="add"/>
                       </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="5%" scope="col">ID</th>
                        <th width="15%" scope="col">Employee Name</th>
                        <th width="10%" scope="col">Table Name</th>
                        <th width="10%" scope="col">Voucher</th>
                        <th width="10%" scope="col">Date</th>
                        <th width="10%" scope="col">Time</th>
                        <th width="10%" scope="col">Payment</th>
                        <th width="10%" scope="col">Status</th>
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
                                    <td><c:out value="${order.orderId}"/></td>
                                    <td><c:out value="${order.emp.empName}"/></td>
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
                                               href="<c:url value="orderItem">
                                                   <c:param name="view" value="list"/>
                                                   <c:param name="orderId" value="${order.orderId}"/>
                                               </c:url>">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <c:if test="${!(order.status eq 'Cancel' or order.status eq 'Approved' or order.status eq 'Rejected' or order.status eq 'Completed')}">
                                                <form action="<c:url value="order">
                                                          <c:param name="id" value="${order.orderId}"/>
                                                      </c:url>" method="post">
                                                    <button type="submit" class="btn btn-success btn-icon" 
                                                            title="Approve" aria-label="Approve"
                                                            name="action" value="approve">
                                                        <i class="bi bi-check2-circle"></i>
                                                    </button>
                                                    <button type="submit" class="btn btn-danger btn-icon" 
                                                            title="Reject" aria-label="Reject"
                                                            name="action" value="reject">
                                                        <i class="bi bi-x-octagon"></i>
                                                    </button>
                                                </form>
                                                
                                                <a class="btn btn-outline-secondary btn-icon btn-edit"
                                               title="Edit" aria-label="Edit"
                                               href="<c:url value="order">
                                                   <c:param name="view" value="edit"/>
                                                   <c:param name="id" value="${order.orderId}"/>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            </c:if>
                                            <c:if test="${order.status eq 'Approved'}">
                                                <form action="<c:url value="order">
                                                          <c:param name="id" value="${order.orderId}"/>
                                                      </c:url>" method="post">
                                                    <button type="submit" class="btn btn-success btn-icon" 
                                                            title="Complete" aria-label="Complete"
                                                            name="action" value="complete">
                                                        <i class="bi bi-check2-circle"></i>
                                                    </button>
                                                </form>
                                            </c:if>

<%--                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                    title="Delete" aria-label="Delete" onclick="showDeletePopup('${order.orderId}')">
                                                <i class="bi bi-x-circle"></i>
                                            </button>--%>
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
                        <a class="page-link" href="<c:url value="/order">
                               <c:param name="view" value="list"/>
                               <c:param name="page" value="${param.page - 1}"/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value="/order">
                                   <c:param name="view" value="list"/>
                                   <c:param name="page" value="${i}"/>
                               </c:url>">${i}</a></li>
                        </c:forEach>
                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                        <a class="page-link" href="<c:url value="/order">
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

<div class="modal" id="deletePopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger">
                <h6 id="idForDeletePopup">Are you sure you want to delete this object?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value="order"/>">
                <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
