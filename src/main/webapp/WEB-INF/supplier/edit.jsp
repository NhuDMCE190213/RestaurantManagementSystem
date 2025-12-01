<%-- 
    Document   : edit
    Created on : Oct 11, 2025, 5:22:09 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Edit Supplier</h1>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentSupplier}">
                    <form method="post" action="<c:url value='supplier'/>">
                        <input type="hidden" name="id" value="${currentSupplier.supplierId}" />
                        <table class="table">
                            <tr><td></td><td></td></tr>

                            <tr>
                                <th>
                                    <label for="id">ID</label>
                                </th>
                                <td>
                                    <label name="id" id="id"><c:out value="${currentSupplier.supplierId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="name">Name</label>
                                </th>
                                <td>
                                    <input type="text" name="name" id="name" value="<c:out value='${currentSupplier.supplierName}'/>" class="form-control" required>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="phoneNumber">Phone Number</label>
                                </th>
                                <td>
                                    <input type="text" name="phoneNumber" id="phoneNumber" value="<c:out value='${currentSupplier.phoneNumber}'/>" class="form-control">
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="email">Email</label>
                                </th>
                                <td>
                                    <input type="text" name="email" id="email" value="<c:out value='${currentSupplier.email}'/>" class="form-control">
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="address">Address</label>
                                </th>
                                <td>
                                    <input type="text" name="address" id="address" value="<c:out value='${currentSupplier.address}'/>" class="form-control">
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="contactPerson">Contact Person</label>
                                </th>
                                <td>
                                    <input type="text" name="contactPerson" id="contactPerson" value="<c:out value='${currentSupplier.contactPerson}'/>" class="form-control">
                                </td>
                            </tr>

                            <input type="hidden" name="status" id="status" value="${currentSupplier.status}">


                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='supplier'/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                    <c:otherwise>
                        <h2 class="mt-5">Not found the supplier which id <c:out value="${param.id}"/>. Please check the information.</h2>
                        <a class="btn btn-outline-dark" href="<c:url value='supplier'/>">Back</a>
                    </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>