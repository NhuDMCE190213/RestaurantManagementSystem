<%-- 
    Document   : list
    Created on : Oct 11, 2025, 5:22:00 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Voucher List - Yummy"/>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Voucher List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='voucher'><c:param name='page' value='1'/></c:url>" method="get" class="search-box input-group">
                            <div class="search-box input-group">
                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                <input type="search" name="keyword" class="form-control" placeholder="Search by code or name" value="${param.keyword != null ? param.keyword : (requestScope.keyword != null ? requestScope.keyword : '')}">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value='voucher'><c:param name='view' value='add'/></c:url>"><i class="bi bi-plus-circle"></i> Add</a>
                    </div>
                </div>
            </div>

            <div class="table-responsive px-4 pb-2">
                <table class="table align-middle admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Code</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Discount Value</th>
                            <th>Quantity</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${voucherList == null || empty voucherList}">
                            <tr>
                                <td colspan="10" style="color:red;">No data to display</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="voucher" items="${voucherList}">
                                <tr>
                                    <td><c:out value="${voucher.voucherId}"/></td>
                                    <td><c:out value="${voucher.voucherCode}"/></td>
                                    <td><c:out value="${voucher.voucherName}"/></td>
                                    <td><c:out value="${voucher.discountType}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${voucher.discountType == 'Percent'}">
                                                <c:out value="${voucher.discountValue}"/>%
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:setLocale value="vi_VN"/>
                                                <fmt:formatNumber value="${voucher.discountValue}" groupingUsed="true" maxFractionDigits="0"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${voucher.quantity}"/></td>
                                    <td><c:out value="${voucher.startDate}"/></td>
                                    <td><c:out value="${voucher.endDate}"/></td>
                                    
                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">
                                            <a class="btn btn-outline-secondary btn-icon btn-edit" title="Edit"
                                               href="<c:url value='voucher'>
                                                   <c:param name='view' value='edit'/>
                                                   <c:param name='id' value='${voucher.voucherId}'/>
                                                   <c:if test='${not empty param.keyword || not empty requestScope.keyword}'>
                                                       <c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/>
                                                   </c:if>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete" title="Delete" onclick="showDeletePopup(${voucher.voucherId})">
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
                    <li class="page-item ${((empty param.page) || param.page <= 1) ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='voucher'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${(empty param.page) ? 1 : param.page - 1}'/>
                               <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i) ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='voucher'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                                   <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                               </c:url>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${ (requestScope.totalPages <= (empty param.page ? 1 : param.page)) ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='voucher'>
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
                <h6 id="idForDeletePopup">Are you sure you want to delete this voucher?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='voucher'/>">
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
        document.getElementById("idForDeletePopup").textContent = "Are you sure you want to delete the voucher with id = " + id + "?";
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
                            <p style="color: green">The operation <c:out value="${param.lastAction}"/> was successful.</p>
                        </c:when>
                        <c:otherwise>
                            <p style="color: red">Failed to <c:out value="${param.lastAction}"/> the voucher. Please check the information.</p>
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