<%-- 
    Document   : list
    Created on : 21 Sep 2025, 10:04:02 AM
    Author     : Dai Minh Nhu - CE190213
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Table List - Yummy"/>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Table List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='table'><c:param name='page' value='1'/></c:url>" method="get" class="search-box input-group">
                            <div class="search-box input-group">
                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                <input type="search" name="keyword" class="form-control" 
                                       placeholder="Search by number or capacity"
                                       value="${param.keyword != null ? param.keyword : (requestScope.keyword != null ? requestScope.keyword : '')}">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value='table'><c:param name='view' value='add'/></c:url>">
                            <i class="bi bi-plus-circle"></i> Add
                        </a>
                    </div>
                </div>
            </div>

            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Number</th>
                            <th>Capacity</th>
                            <th>Status</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${tablesList == null || empty tablesList}">
                            <tr>
                                <td colspan="5" style="color:red;">No data to display</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="table" items="${tablesList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${table.id}"/></td>
                                    <td><c:out value="${table.number}"/></td>
                                    <td><c:out value="${table.capacity}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:toLowerCase(table.status) == 'available'}">
                                                <span class="badge bg-success">Available</span>
                                            </c:when>
                                            <c:when test="${fn:toLowerCase(table.status) == 'reserved'}">
                                                <span class="badge bg-warning text-dark">Reserved</span>
                                            </c:when>
                                            <c:when test="${fn:toLowerCase(table.status) == 'occupied'}">
                                                <span class="badge bg-danger">Occupied</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary"><c:out value="${table.status}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <div class="d-flex justify-content-end gap-2 align-items-center">
                                            <a class="btn btn-outline-secondary btn-icon btn-edit" title="Edit"
                                               href="<c:url value='table'>
                                                   <c:param name='view' value='edit'/>
                                                   <c:param name='id' value='${table.id}'/>
                                                   <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete" title="Delete" onclick="showDeletePopup(${table.id})">
                                                <i class="bi bi-x-circle"></i>
                                            </button>

                                            <!-- Change status dropdown -->
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-outline-primary btn-icon dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                                    <i class="bi bi-three-dots"></i>
                                                    
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-end">
                                                    <!-- For each possible status, show form to set it. The current status can still be shown but disabled -->
                                                    <li>
                                                        <form method="post" action="<c:url value='table'/>" class="px-2 py-1">
                                                            <input type="hidden" name="id" value="${table.id}" />
                                                            <input type="hidden" name="newStatus" value="Available" />
                                                            <button type="submit" name="action" value="changeStatus" class="dropdown-item" ${fn:toLowerCase(table.status) == 'available' ? 'disabled' : ''}>
                                                                Set Available
                                                            </button>
                                                        </form>
                                                    </li>
                                                    <li>
                                                        <form method="post" action="<c:url value='table'/>" class="px-2 py-1">
                                                            <input type="hidden" name="id" value="${table.id}" />
                                                            <input type="hidden" name="newStatus" value="Reserved" />
                                                            <button type="submit" name="action" value="changeStatus" class="dropdown-item" ${fn:toLowerCase(table.status) == 'reserved' ? 'disabled' : ''}>
                                                                Set Reserved
                                                            </button>
                                                        </form>
                                                    </li>
                                                    <li>
                                                        <form method="post" action="<c:url value='table'/>" class="px-2 py-1">
                                                            <input type="hidden" name="id" value="${table.id}" />
                                                            <input type="hidden" name="newStatus" value="Occupied" />
                                                            <button type="submit" name="action" value="changeStatus" class="dropdown-item" ${fn:toLowerCase(table.status) == 'occupied' ? 'disabled' : ''}>
                                                                Set Occupied
                                                            </button>
                                                        </form>
                                                    </li>
                                                </ul>
                                            </div>
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
                    <li class="page-item ${((empty param.page) || param.page <= 1) ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='table'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${(empty param.page) ? 1 : param.page - 1}'/>
                               <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i) ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='table'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                                   <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                               </c:url>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${ (requestScope.totalPages <= (empty param.page ? 1 : param.page)) ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='table'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${(empty param.page) ? 2 : param.page + 1}'/>
                               <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
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
                <h6 id="idForDeletePopup">Are you sure you want to delete this?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='table'/>">
                <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function showDeletePopup(id) {
        document.getElementById("hiddenInputIdDelete").value = id;
        document.getElementById("idForDeletePopup").textContent = "Are you sure you want to delete the table with id = " + id + "?";
        var myModal = new bootstrap.Modal(document.getElementById('deletePopup'));
        myModal.show();
    }
</script>

<c:if test="${not empty param.status}">
    <div class="modal" id="exampleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Action ${param.status == "success" ? "Successful" : "Fail"}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <c:choose>
                        <c:when test="${param.status eq 'success'}">
                            <p style="color: green">The action <c:out value="${param.lastAction}"/> successfully.</p>
                        </c:when>
                        <c:otherwise>
                            <p style="color: red">Failed to <c:out value="${param.lastAction}"/>. Please check the information.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
        myModal.show();
    </script>
</c:if>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
