<%-- 
    Document   : add
    Created on : Oct 11, 2025, 5:21:51 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add Supplier</h1>
        </div>

        <div class="container">
            <form method="post" action="<c:url value='supplier'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                            <th>
                                <label for="name">Name</label>
                            </th>
                            <td>
                                <input type="text" name="supplierName" id="supplierName" class="form-control" required>
                            </td>
                        </tr>

                        <tr>
                            <th>
                                <label for="description">Phone Number</label>
                            </th>
                            <td>
                                <input type="text" name="phoneNumber" id="phoneNumber" class="form-control">
                            </td>
                        </tr>

                        <tr>
                            <th>
                                <label for="description">Email</label>
                            </th>
                            <td>
                                <input type="text" name="email" id="email" class="form-control">
                            </td>
                        </tr>

                        <tr>
                            <th>
                                <label for="description">Address</label>
                            </th>
                            <td>
                                <input type="text" name="address" id="address" class="form-control">
                            </td>
                        </tr>

                        <tr>
                            <th>
                                <label for="description">Contact Person</label>
                            </th>
                            <td>
                                <input type="text" name="contactPerson" id="contactPerson" class="form-control">
                            </td>
                        </tr>

                        <tr>
                            <td>
                            </td>
                            <td>
                                <button class="btn btn-success" type="submit" name="action" value="add">Save</button>
                                <a class="btn btn-outline-dark" href="<c:url value='supplier'/>">Cancel</a>
                            </td>
                        </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>