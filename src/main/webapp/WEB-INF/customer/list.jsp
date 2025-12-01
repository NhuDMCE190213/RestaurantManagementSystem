<%-- 
    Document   : list
    Created on : Oct 11, 2025, 3:23:41 PM
    Author     : PHAT (Edited by Huy)
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Customer List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='customer'>
                              <c:param name='page' value='1'/>
                          </c:url>" method="get" class="search-box input-group">
                        <div class="search-box input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" class="form-control" placeholder="Search by customer or ID">
                        </div>
                    </form>
    
                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="5%">ID</th>
                        <th width="10%">Account</th>
                        <th width="15%">Full Name</th>
                        <th width="5%">Gender</th>
                        <th width="10%">Date of Birth</th>
                        <th width="10%">Phone</th>
                        <th width="15%">Email</th>
                        <th width="15%">Address</th>
                        <th width="5%">Status</th>
                        <th width="10%" class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${customerList == null || empty customerList}">
                            <tr>
                                <td colspan="10" style="color:red;">No data to display</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="customer" items="${customerList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${customer.customerId}"/></td>
                                    <td><c:out value="${customer.customerAccount}"/></td>
                                    <td><c:out value="${customer.customerName}"/></td>
                                    <td><c:out value="${customer.gender}"/></td>
                                    <td><c:out value="${customer.dob}"/></td>
                                    <td><c:out value="${customer.phoneNumber}"/></td>
                                    <td><c:out value="${customer.email}"/></td>
                                    <td><c:out value="${customer.address}"/></td>
                                    <td><c:out value="${customer.status}"/></td>
                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">
                                            <form action="<c:url value='customer'/>" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="updateStatus">
                                                <input type="hidden" name="id" value="${customer.customerId}">
                                                <button type="submit" 
                                                        class="btn ${customer.status == 'Active' ? 'btn-danger' : 'btn-success'} btn-icon"
                                                        title="${customer.status == 'Active' ? 'Ban' : 'Unban'}" 
                                                        aria-label="Update Status">
                                                    <i class="bi ${customer.status == 'Active' ? 'bi-person-x' : 'bi-person-check'}"></i>
                                                </button>

                                            </form>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                    title="Delete" aria-label="Delete"
                                                    onclick="showDeletePopup('${customer.customerId}')">
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
                    <li class="page-item ${((empty param.page) || param.page <= 1)?'disabled':''}">
                        <a class="page-link" href="<c:url value='/customer'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${param.page - 1}'/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?'active':''}">
                            <a class="page-link" href="<c:url value='/customer'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                               </c:url>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1)?'disabled':''}">
                        <a class="page-link" href="<c:url value='/customer'>
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

<!-- Delete confirmation modal -->
<div class="modal" id="deletePopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger">
                <h6 id="idForDeletePopup">Are you sure you want to delete this customer?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='customer'/>">
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
