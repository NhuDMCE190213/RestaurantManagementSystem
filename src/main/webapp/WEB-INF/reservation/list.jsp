<%-- 
    Document   : list
    Created on : Oct 28, 2025, 8:20:48 PM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div><h1 class="section-title mb-1">Reservation List</h1></div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='/reservation'/>" method="get" class="search-box input-group">
                        <input type="hidden" name="view" value="list"/>
                        <input type="hidden" name="page" value="1"/>
                        <span class="input-group-text"><i class="bi bi-search"></i></span>
                        <input type="search" name="keyword" value="${param.keyword}" class="form-control" placeholder="Search id / table / status">
                    </form>
                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="8%">ID</th>
                        <th width="12%">Customer</th>
                        <th width="10%">Table</th>
                        <th width="15%">Date</th>
                        <th width="12%">Time</th>
                        <th width="15%">Status</th>
                        <th width="18%" class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${reservationList == null || empty reservationList}">
                            <tr><td colspan="8" style="color:red;">No data to display</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="r" items="${reservationList}">
                                <tr>
                                    <td><c:out value="${r.reservationId}"/></td>
                                    <td><c:out value="${r.customer.customerName}"/></td>
                                    <td><c:out value="${r.table.number}"/></td>
                                    <td><c:out value="${r.reservationDate}"/></td>
                                    <td><c:out value="${r.reservationTime}"/></td>
                                    <td>
                                        <span class="badge
                                              ${r.status == 'Approved' ? 'bg-success' : 
                                                (r.status == 'Rejected' ? 'bg-danger' : 
                                                (r.status == 'Cancelled' ? 'bg-secondary' : 'bg-warning text-dark'))}">
                                              <c:out value="${r.status}"/>
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">

                                            <!-- Approve -->
                                            <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="approve"/>
                                                <input type="hidden" name="id" value="${r.reservationId}"/>
                                                <button type="submit" class="btn btn-success btn-icon" 
                                                        title="Approve" aria-label="Approve"
                                                        ${r.status == 'Approved' ? 'disabled' : ''}>
                                                    <i class="bi bi-check2-circle"></i>
                                                </button>
                                            </form>

                                            <!-- Reject -->
                                            <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="reject"/>
                                                <input type="hidden" name="id" value="${r.reservationId}"/>
                                                <button type="submit" class="btn btn-danger btn-icon" 
                                                        title="Reject" aria-label="Reject"
                                                        ${r.status == 'Rejected' ? 'disabled' : ''}>
                                                    <i class="bi bi-x-octagon"></i>
                                                </button>
                                            </form>

                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <!-- Pagination -->
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <li class="page-item ${((empty param.page) || param.page <= 1)?'disabled':''}">
                        <a class="page-link" href="<c:url value='/reservation'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${param.page - 1}'/>
                               <c:param name='keyword' value='${param.keyword}'/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i==1) || param.page == i)?'active':''}">
                            <a class="page-link" href="<c:url value='/reservation'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                                   <c:param name='keyword' value='${param.keyword}'/>
                               </c:url>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1)?'disabled':''}">
                        <a class="page-link" href="<c:url value='/reservation'>
                               <c:param name='view' value='list'/>
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

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>


