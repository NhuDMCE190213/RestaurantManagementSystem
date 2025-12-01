<%-- 
    Document   : create
    Created on : 20 Jun 2025, 10:03:41 PM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Table - Yummy"/>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add new Table</h1>
        </div>

        <div class="container">
            <form method="post" action="<c:url value='table'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                        <th><label for="number">Number</label></th>
                        <td><input type="text" name="number" id="number" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="table_capacity">Capacity</label></th>
                        <td><input type="number" name="table_capacity" id="table_capacity" class="form-control" required></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='table'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>