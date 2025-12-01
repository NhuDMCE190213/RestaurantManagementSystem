<%-- 
    Document   : create
    Created on : 20 Jun 2025, 10:03:41 PM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Role - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Role</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="role"/>">
                <table class="table table align-middle admin-table">
                    <tr>
                        <td>
                        </td>
                        <td>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="name">Name</label>
                        </th>
                        <td>
                            <input type="text" name="role_name" id="name" class="form-control" required>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="description">Description</label>
                        </th>
                        <td>
                            <input type="text" name="description" id="description" class="form-control">
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="role"/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
