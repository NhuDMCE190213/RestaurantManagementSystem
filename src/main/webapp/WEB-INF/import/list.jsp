<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Import List</h1>
            </div>
            <div class="actions d-flex w-100 w-md-auto align-items-center justify-content-md-end ms-md-auto gap-2">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='import'><c:param name='page' value='1'/></c:url>" method="get" class="search-box input-group">
                        <div class="search-box input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" class="form-control"
                                   placeholder="Search by manager or supplier"
                                   value="${param.keyword != null ? param.keyword : (requestScope.keyword != null ? requestScope.keyword : '')}">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value='import'>
                           <c:param name='view' value='add'/>
                       </c:url>"><i class="bi bi-plus-circle"></i> Add</a>

                </div>  
            </div>
            </div>

            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Manager</th>
                            <th scope="col">Contact Person</th>
                            <th scope="col">Supplier</th>
                            <th scope="col">Date</th>
                            <th scope="col" class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${importList == null || empty importList}">
                                <tr>
                                    <td colspan="6" class="text-danger">No data to display</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="imp" items="${importList}" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${imp.importId}"/></td>
                                        <td><c:out value="${imp.empName}"/></td>
                                        <td><c:out value="${imp.contactPerson}"/></td>
                                        <td><c:out value="${imp.supplierName}"/></td>
                                        <td><c:out value="${imp.importDate}"/></td>
                                        <td class="text-end">
                                            <div class="action-button-group d-flex justify-content-end gap-2">
                                                <c:url var="detail" value='import'>
                                                    <c:param name='view' value='detail'/>
                                                    <c:param name='id' value='${imp.importId}'/>
                                                </c:url>
                                                <a class="btn btn-outline-success btn-icon btn-view" title="View details" aria-label="View details" href="${detail}">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <c:url var="edit" value='import'>
                                                    <c:param name='view' value='edit'/>
                                                    <c:param name='id' value='${imp.importId}'/>
                                                </c:url>
                                                <a class="btn btn-outline-secondary btn-icon btn-edit" title="Edit" aria-label="Edit" href="${edit}">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <button type="button" class="btn btn-outline-secondary btn-icon btn-delete" title="Delete" aria-label="Delete" onclick="showDeletePopup('${imp.importId}')">
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
            </div>

            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                        <a class="page-link" href="<c:url value="/import">
                               <c:param name="view" value="list"/>
                               <c:param name="page" value="${param.page - 1}"/>
                               <c:param name="keyword" value="${requestScope.keyword}"/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value="/import">
                                   <c:param name="view" value="list"/>
                                   <c:param name="page" value="${i}"/>
                                   <c:param name="keyword" value="${requestScope.keyword}"/>
                               </c:url>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1)?"disabled":""}">
                        <a class="page-link" href="<c:url value="/import">
                               <c:param name="view" value="list"/>
                               <c:param name="page" value="${(empty param.page)?2:param.page + 1}"/>
                               <c:param name="keyword" value="${requestScope.keyword}"/>
                           </c:url>" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
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
                <h6 id="idForDeletePopup">Are you sure you want to delete this import?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='import'/>">
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
