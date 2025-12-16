<%-- 
    Document   : list
    Created on : Oct 28, 2025, 8:20:48 PM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>
<style>
    .admin-table {
        table-layout: fixed; /* cột cố định, không bị nhảy */
    }
    .admin-table th, .admin-table td {
        vertical-align: middle;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .admin-table .note-cell {
        white-space: normal; /* note được xuống dòng */
    }
    .admin-table .time-cell {
        font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
        letter-spacing: .2px;
    }
    .admin-table .btn-icon {
        width: 38px;
        height: 38px;
        padding: 0;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }

    .admin-table{
        width: 100%;
        table-layout: fixed; /* QUAN TRỌNG: ép theo colgroup */
    }

    .admin-table th, .admin-table td{
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .admin-table td.note-cell{
        white-space: normal;       /* NOTE cho xuống dòng */
        word-break: break-word;
    }

</style>


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
                    <a class="btn btn-primary add-btn" href="<c:url value="reservation?view=bookatable">
                           <c:param name="view" value="add"/>
                       </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <colgroup>
                    <col style="width:60px;">   <!-- ID -->
                    <col style="width:160px;">  <!-- Customer -->
                    <col style="width:160px;">  <!-- Employee -->
                    <col style="width:220px;">  <!-- Voucher -->
                    <col style="width:90px;">   <!-- Table -->
                    <col style="width:120px;">  <!-- Date -->
                    <col style="width:140px;">  <!-- Time -->
                    <col style="width:180px;">   <!-- Note -->
                    <col style="width:120px;">  <!-- Status -->
                    <col style="width:150px;">  <!-- Action -->
                </colgroup>

                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer</th>
                        <th>Employee</th>
                        <th>Voucher</th>
                        <th>Table</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Note</th>
                        <th>Status</th>
                        <th class="text-end">Action</th>
                    </tr>
                </thead>

                <tbody>
                    <c:choose>
                        <c:when test="${reservationList == null || empty reservationList}">
                            <tr><td colspan="9" style="color:red;">No data to display</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="r" items="${reservationList}">
                                <tr>
                                    <td><c:out value="${r.reservationId}"/></td>
                                    <td><c:out value="${r.customer.customerName}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty r.emp}">N/A</c:when>
                                            <c:otherwise><c:out value="${r.emp.empName}"/></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty r.voucher}">None</c:when>
                                            <c:otherwise>
                                                <c:out value="${r.voucher.voucherCode}"/> - <c:out value="${r.voucher.voucherName}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${r.table.number}"/></td>
                                    <td><c:out value="${r.reservationDate}"/></td>
                                    <td>
                                        ${fn:substring(r.timeStart, 0, 5)} - ${fn:substring(r.timeEnd, 0, 5)}
                                    </td>
                                    <td class="note-cell">${r.description}</td>

                                    <td>
                                        <span class="badge
                                              ${r.status == 'Approved' ? 'bg-success' :
                                                (r.status == 'Rejected' ? 'bg-danger' :
                                                (r.status == 'Cancelled' ? 'bg-secondary' :
                                                (r.status == 'Serving' ? 'bg-info' :
                                                (r.status == 'Completed' ? 'bg-primary' :
                                                'bg-warning text-dark'))))}">
                                              <c:out value="${r.status}"/>
                                        </span>
                                    </td>


                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">

                                            <!-- View Order -->
                                            <a class="btn btn-outline-success btn-icon btn-view"
                                               href="<c:url value='/order'>
                                                   <c:param name='view' value='list'/>
                                                   <c:param name='reservationId' value='${r.reservationId}'/>
                                               </c:url>"
                                               title="View Order" aria-label="View Order">
                                                <i class="bi bi-eye-fill"></i>
                                            </a>

                                            <!-- PENDING: Approve + Reject -->
                                            <c:if test="${r.status eq 'Pending'}">
                                                <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="approve"/>
                                                    <input type="hidden" name="id" value="${r.reservationId}"/>
                                                    <button type="submit" class="btn btn-success btn-icon" title="Approve">
                                                        <i class="bi bi-check2-circle"></i>
                                                    </button>
                                                </form>

                                                <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="reject"/>
                                                    <input type="hidden" name="id" value="${r.reservationId}"/>
                                                    <button type="submit" class="btn btn-danger btn-icon" title="Reject">
                                                        <i class="bi bi-x-octagon"></i>
                                                    </button>
                                                </form>
                                            </c:if>

                                            <!-- APPROVED: show Reserving -->
                                            <c:if test="${r.status eq 'Approved'}">
                                                <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="serving"/>
                                                    <input type="hidden" name="id" value="${r.reservationId}"/>
                                                    <button type="submit" class="btn btn-info btn-icon" title="Serving">
                                                        <i class="bi bi-hourglass-split"></i>
                                                    </button>
                                                </form>
                                            </c:if>

                                            <!-- RESERVING: show Complete -->
                                            <c:if test="${r.status eq 'Serving'}">
                                                <form action="<c:url value='/reservation'/>" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="complete"/>
                                                    <input type="hidden" name="id" value="${r.reservationId}"/>
                                                    <button type="submit" class="btn btn-warning btn-icon" title="Complete">
                                                        <i class="bi bi-check2-square"></i>
                                                    </button>
                                                </form>
                                            </c:if>

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


