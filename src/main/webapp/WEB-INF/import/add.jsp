<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add Import</h1>
        </div>
        
        <div class="container">
            <form method="post" action="<c:url value='import'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                        <th>
                            <label for="name">Supplier Name</label>
                        </th>
                            <td>
                                <select name="supplierId" class="form-select">    
                                    <c:forEach var="supplier" items="${supplierList}">
                                        <option value="${supplier.supplierId}" required class="form-control">${supplier.supplierName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                    </tr>
                        
                        <tr>
                            <th>
                                <label for="name">Manager</label>
                            </th>
                            <td>
                                <select name="empId" class="form-select">    
                                    <c:forEach var="employee" items="${employeeList}">
                                        <option value="${employee.empId}" required class="form-control">${employee.empName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>
                            </td>
                            <td>
                                <button class="btn btn-success" type="submit" name="action" value="add">Save</button>
                                <a class="btn btn-outline-dark" href="<c:url value='import'/>">Cancel</a>
                            </td>
                        </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>