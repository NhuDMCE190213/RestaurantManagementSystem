<%-- 
    Document   : create
    Created on : Oct 11, 2025, 3:23:25?PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Employee - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Employee</h1>
            </div>
        </div>
        <div class="container">
            <form method="post" action="<c:url value='employee'/>">
                <table class="table">
                    <tr>
                        <th><label for="empAccount">Account</label></th>
                        <td><input type="text" name="empAccount" id="empAccount" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="password">Password</label></th>
                        <td><input type="text" name="password" id="password" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="empName">Full Name</label></th>
                        <td><input type="text" name="empName" id="empName" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="roleId">Role</label></th>
                        <td>
                            <select name="roleId" id="roleId" class="form-select" required>
                                <c:forEach var="role" items="${rolesList}">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='employee'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
