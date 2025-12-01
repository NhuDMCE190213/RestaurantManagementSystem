<%-- 
    Document   : list
    Created on : Oct 11, 2025, 3:23:41 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Category List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value="category">
                              <c:param name="page" value="1"/>
                          </c:url>" method="get" class="search-box input-group">
                        <div class="search-box input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" class="form-control" placeholder="Search by customer or ID">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value='category'>
                           <c:param name='view' value='add'/>
                       </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                </div>
            </div>
        </div>
        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="10%" scope="col">ID</th>
                        <th width="15%" scope="col">Category Name</th>
                        <th width="15%" scope="col">Description</th>
                        <th width="15%" scope="col" class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${categoryList == null || empty categoryList}">
                            <tr>
                                <td colspan="11" style="color:red;">No data to display</td>
                            </tr>   
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="category" items="${categoryList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${category.categoryId}"/></td>
                                    <td><c:out value="${category.categoryName}"/></td>
                                    <td><c:out value="${category.description}"/></td>

                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">
                                            <a class="btn btn-outline-secondary btn-icon btn-edit"
                                               title="Edit Role" aria-label="Edit"
                                               href="<c:url value="category">
                                                   <c:param name="view" value="edit"/>
                                                   <c:param name="id" value="${category.categoryId}"/>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                    title="Delete" aria-label="Delete" onclick="showDeletePopup('${category.categoryId}')">
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
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                        <a class="page-link" href="<c:url value='/category'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${param.page - 1}'/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value='/category'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                               </c:url>">${i}</a></li>
                        </c:forEach>
                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                        <a class="page-link" href="<c:url value='/category'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${(empty param.page)?2:param.page + 1}'/>
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
                <h6 id="idForDeletePopup">Are you sure you want to delete this category?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='category'/>">
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
