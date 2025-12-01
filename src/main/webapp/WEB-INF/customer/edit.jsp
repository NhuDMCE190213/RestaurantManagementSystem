<%-- 
    Document   : edit
    Created on : Oct 11, 2025, 3:23:33 PM
    Author     : PHAT (Edited by Huy)
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Customer</h1>
            </div>
        </div>
        <div class="container">
            <c:choose>
                <c:when test="${not empty currentCustomer}">
                    <form method="post" action="<c:url value='customer'>
                              <c:param name='id' value='${param.id}'/>
                          </c:url>">
                        <table class="table">

                            <tr>
                                <th><label class="form-label">ID</label></th>
                                <td><label class="form-label"><c:out value="${currentCustomer.customerId}"/></label></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Account</label></th>
                                <td><label class="form-label"><c:out value="${currentCustomer.customerAccount}"/></label></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Full Name</label></th>
                                <td><input type="text" name="customerName" class="form-control" 
                                           value="${currentCustomer.customerName}" required></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Phone Number</label></th>
                                <td><input type="text" name="phoneNumber" class="form-control" 
                                           value="${currentCustomer.phoneNumber}" required></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Email</label></th>
                                <td><input type="email" name="email" class="form-control" 
                                           value="${currentCustomer.email}" required></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Address</label></th>
                                <td><input type="text" name="address" class="form-control" 
                                           value="${currentCustomer.address}" required></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Date of Birth</label></th>
                                <td><input type="date" name="dateOfBirth" class="form-control" 
                                           value="${currentCustomer.dob}" required></td>
                            </tr>

                            <tr>
                                <td></td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='customer'/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the customer with ID <c:out value="${param.id}"/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value='customer'/>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
