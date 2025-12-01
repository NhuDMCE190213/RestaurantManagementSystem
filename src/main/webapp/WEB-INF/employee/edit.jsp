<%-- 
    Document   : edit
    Created on : Oct 11, 2025, 3:23:33?PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Edit Employee - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Employee Role</h1>
            </div>
        </div>
        <div class="container">
            <c:choose>
                <c:when test="${not empty currentEmployee}">
                    <form method="post" action="<c:url value='employee'>
                              <c:param name='id' value='${param.id}'/>
                          </c:url>">
                        <table class="table">
                            <tr>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label class="form-label">ID</label>
                                </th>
                                <td>
                                    <label class="form-label"><c:out value="${currentEmployee.empId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label class="form-label">Account</label>
                                </th>
                                <td>
                                    <label class="form-label"><c:out value="${currentEmployee.empAccount}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label class="form-label">Full Name</label>
                                </th>
                                <td>
                                    <label class="form-label"><c:out value="${currentEmployee.empName}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="roleId">Role</label>
                                </th>
                                <td>
                                    <select name="roleId" id="roleId" class="form-control" required>
                                        <c:forEach var="role" items="${rolesList}">
                                            <option value="${role.id}" ${(role.id == currentEmployee.role.id)?'selected':''}>
                                                <c:out value='${role.name}'/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='employee'/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the Employee with ID <c:out value="${param.id}"/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value='employee'/>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
