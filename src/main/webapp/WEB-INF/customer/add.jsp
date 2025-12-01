<%-- 
    Document   : create
    Created on : Oct 11, 2025, 3:23:25 PM
    Author     : PHAT (Edited by Huy)
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add new customer</h1>
            </div>
        </div>
        <div class="container">
            <form method="post" action="<c:url value='customer'/>">
                <table class="table">
                    <tr>
                        <th><label for="customerAccount">Account</label></th>
                        <td><input type="text" name="customerAccount" id="customerAccount" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="password">Password</label></th>
                        <td><input type="text" name="password" id="password" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="customerName">Full Name</label></th>
                        <td><input type="text" name="customerName" id="customerName" class="form-control" required></td>
                    </tr>

<!--                    <tr>
                        <th><label for="gender">Gender</label></th>
                        <td>
                            <select name="gender" id="gender" class="form-control" required>
                                <option value="">-- Select gender --</option>
                                <option value="Nam">Nam</option>
                                <option value="Nữ">Nữ</option>
                            </select>
                        </td>
                    </tr>-->

<!--                    <tr>
                        <th><label for="phoneNumber">Phone Number</label></th>
                        <td><input type="text" name="phoneNumber" id="phoneNumber" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="email">Email</label></th>
                        <td><input type="email" name="email" id="email" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="address">Address</label></th>
                        <td><input type="text" name="address" id="address" class="form-control" required></td>
                    </tr>
                    <tr>
                        <th><label for="dateOfBirth">Date of Birth</label></th>
                        <td><input type="date" name="dateOfBirth" id="dateOfBirth" class="form-control" required></td>
                    </tr>-->
                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='customer'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
