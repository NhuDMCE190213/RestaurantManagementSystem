<%-- 
    Document   : list
    Created on : Oct 11, 2025, 3:23:41?PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Employee List - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Employee List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value="employee">
                              <c:param name="page" value="1"/>
                          </c:url>" method="get" class="search-box input-group">
                        <div class="search-box input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" class="form-control" placeholder="Search employee">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value="employee">
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
                        <th width="10%" scope="col">Account</th>
                        <th width="15%" scope="col">Full Name</th>
                        <th width="5%" scope="col">Gender</th>
                        <th width="10%" scope="col">Date of Birth</th>
                        <th width="5%" scope="col">Phone</th>
                        <th width="5%" scope="col">Email</th>
                        <th width="5%" scope="col">Address</th>
                        <th width="10%" scope="col">Role</th>
                        <th width="5%" scope="col">Status</th>
                        <th width="20%" scope="col" class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${employeeList == null || empty employeeList}">
                            <tr>
                                <td colspan="11" style="color:red;">No data to display</td>
                            </tr>   
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="emp" items="${employeeList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${emp.empId}"/></td>
                                    <td><c:out value="${emp.empAccount}"/></td>
                                    <td><c:out value="${emp.empName}"/></td>
                                    <td><c:out value="${emp.gender}"/></td>
                                    <td><c:out value="${emp.dob}"/></td>
                                    <td><c:out value="${emp.phoneNumber}"/></td>
                                    <td><c:out value="${emp.email}"/></td>
                                    <td><c:out value="${emp.address}"/></td>
                                    <td><c:out value="${emp.role.name}"/></td>
                                    <td><c:out value="${emp.status}"/></td>

                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">
                                            <a class="btn btn-outline-secondary btn-icon btn-edit"
                                               title="Edit Role" aria-label="Edit"
                                               href="<c:url value="employee">
                                                   <c:param name="view" value="edit"/>
                                                   <c:param name="id" value="${emp.empId}"/>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <form action="<c:url value='employee'>
                                                      <c:param name="id" value="${emp.empId}"/>
                                                  </c:url>" method="post" style="display:inline;">
                                                <c:choose>
                                                    <c:when test="${emp.status ne 'Active'}">
                                                        <button type="submit" class="btn btn-outline-primary btn-icon"
                                                                name="action" value="unban"
                                                                title="Unban" aria-label="Unban">
                                                            <i class="bi-person-check"></i>
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="submit" class="btn btn-outline-secondary btn-icon btn-delete"
                                                                name="action" value="ban"
                                                                title="Ban" aria-label="Ban">
                                                            <i class="bi-person-x"></i>
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </form>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                    title="Delete" aria-label="Delete" onclick="showDeletePopup('${emp.empId}')">
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
                        <a class="page-link" href="<c:url value='/employee'>
                               <c:param name='view' value='list'/>
                               <c:param name='page' value='${param.page - 1}'/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value='/employee'>
                                   <c:param name='view' value='list'/>
                                   <c:param name='page' value='${i}'/>
                               </c:url>">${i}</a></li>
                        </c:forEach>
                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                        <a class="page-link" href="<c:url value='/employee'>
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
                <h6 id="idForDeletePopup">Are you sure you want to delete this employee?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='employee'/>">
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
