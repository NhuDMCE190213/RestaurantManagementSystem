<%-- 
    Document   : mylist
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>
<c:if test="${sessionScope.popupPage == 'my-reservation'}">
    <div class="alert ${sessionScope.popupStatus ? 'alert-success' : 'alert-danger'}
         d-flex align-items-center gap-2 px-4 py-3 shadow-sm rounded-3"
         style="border-left: 5px solid ${sessionScope.popupStatus ? '#198754' : '#dc3545'};">

        <!-- Icon -->
        <i class="bi ${sessionScope.popupStatus ? 'bi-check-circle-fill text-success' : 'bi-x-circle-fill text-danger'} fs-4"></i>

        <!-- Message -->
        <span class="fw-semibold">${sessionScope.popupMessage}</span>
    </div>

    <c:remove var="popupMessage" scope="session"/>
    <c:remove var="popupStatus" scope="session"/>
    <c:remove var="popupPage" scope="session"/>
</c:if>
<main class="d-flex justify-content-center align-items-center vh-100 bg-light">

    <section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="My reservation">
        <div class="content-card shadow-sm">

            <!-- Header -->
            <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
                <div>
                    <h1 class="section-title mb-1">My Reservation</h1>
                </div>
                <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                    <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                        <!-- Search box -->
                        <form action="<c:url value='/my-reservation'/>" method="get" class="search-box input-group">
                            <input type="hidden" name="customerId" value="${requestScope.customerId}"/>
                            <input type="hidden" name="page" value="1"/>
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" value="${param.keyword}" class="form-control" placeholder="Search table / status">
                        </form>

                    </div>
                </div>
            </div>

            <!-- Table -->
            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th width="10%">Table</th>
                            <th width="10%">Date</th>
                            <th width="10%">Time</th>
                            <th width="10%">Status</th>
                            <th width="20%" class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty reservationList}">
                                <tr><td colspan="7" style="color:red;">No data to display</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="r" items="${reservationList}">
                                    <tr>
                                        <td><c:out value="${r.table.number}"/></td>
                                        <td><c:out value="${r.reservationDate}"/></td>
                                        <td><c:out value="${r.reservationTime}"/></td>
                                        <td>
                                            <span class="badge
                                                  ${r.status == 'Approved' ? 'bg-success' : 
                                                    (r.status == 'Rejected' ? 'bg-danger' :
                                                    (r.status == 'Cancelled' ? 'bg-secondary' : 'bg-warning text-dark'))}">
                                                      ${r.status}
                                                  </span>
                                            </td>
                                            <td class="text-end">
                                                <div class="action-button-group d-flex justify-content-end gap-2">
                                                    <!-- Edit -->
                                                    <a class="btn btn-outline-secondary btn-icon"
                                                       <a class="btn btn-outline-secondary btn-icon"
                                                       href="<c:url value='/my-reservation'>
                                                           <c:param name='view' value='edit'/>
                                                           <c:param name='id' value='${r.reservationId}'/>
                                                           <c:param name='from' value='mylist'/>
                                                           <c:param name='customerId' value='${requestScope.customerId}'/>
                                                       </c:url>"
                                                       title="Edit" aria-label="Edit"
                                                       ${r.status != 'Pending' ? 'disabled' : ''}>
                                                        <i class="bi bi-pencil-square"></i>
                                                    </a>


                                                    <!-- Cancel -->
                                                    <button type="button" class="btn btn-outline-danger btn-icon"
                                                            title="Cancel" aria-label="Cancel"
                                                            onclick="showCancelPopup('${r.reservationId}', '${requestScope.customerId}')"
                                                            ${r.status != 'Pending' ? 'disabled' : ''}>
                                                        <i class="bi bi-x-circle"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <nav aria-label="Page navigation example">
                        <ul class="pagination">
                            <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                                <a class="page-link" href="<c:url value='/my-reservation'>
                                       <c:param name='view' value='mylist'/>
                                       <c:param name='customerId' value='${requestScope.customerId}'/>
                                       <c:param name='page' value='${param.page - 1}'/>
                                       <c:param name='keyword' value='${param.keyword}'/>
                                   </c:url>" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>

                            <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                                <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                                    <a class="page-link" href="<c:url value='/my-reservation'>
                                           <c:param name='view' value='mylist'/>
                                           <c:param name='customerId' value='${requestScope.customerId}'/>
                                           <c:param name='page' value='${i}'/>
                                           <c:param name='keyword' value='${param.keyword}'/>
                                       </c:url>">${i}</a></li>
                                </c:forEach>

                            <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1)?"disabled":""}">
                                <a class="page-link" href="<c:url value='/my-reservation'>
                                       <c:param name='view' value='mylist'/>
                                       <c:param name='customerId' value='${requestScope.customerId}'/>
                                       <c:param name='page' value='${(empty param.page)?2:param.page + 1}'/>
                                       <c:param name='keyword' value='${param.keyword}'/>
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

    <!-- Cancel confirmation modal -->
    <div class="modal" id="cancelPopup" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Cancellation</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body text-danger">
                    <h6 id="idForCancelPopup">Are you sure you want to cancel this reservation?</h6>
                    <small class="text-muted">This action cannot be undone.</small>
                </div>
                <form method="post" action="<c:url value='/my-reservation'/>">
                    <input type="hidden" name="action" value="cancel"/>
                    <input type="hidden" id="hiddenReservationId" name="id" value=""/>
                    <input type="hidden" id="hiddenCustomerId" name="customerId" value="${requestScope.customerId}"/>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-danger">Cancel Reservation</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function showCancelPopup(resId, cusId) {
            document.getElementById('hiddenReservationId').value = resId;
            document.getElementById('hiddenCustomerId').value = cusId;
            document.getElementById('idForCancelPopup').innerText = 'Cancel reservation #' + resId + '?';
            var modal = new bootstrap.Modal(document.getElementById('cancelPopup'));
            modal.show();
        }
    </script>

    <%@include file="/WEB-INF/include/footerCustomer.jsp" %>
