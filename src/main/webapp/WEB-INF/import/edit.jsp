<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Edit Import</h1>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentImport}">
                    <form method="post" action="<c:url value='import'/>">
                        <input type="hidden" name="id" value="<c:out value='${currentImport.importId}'/>" />
                        <table class="table">
                            <tr><td></td><td></td></tr>

                            <tr>
                                <th>
                                    <label for="id">ID</label>
                                </th>
                                <td>
                                    <label id="id"><c:out value="${currentImport.importId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="supplierId">Supplier</label>
                                </th>
                                <td>
                                    <select name="supplierId" id="supplierId" class="form-select">
                                        <c:forEach var="supplier" items="${supplierList}">
                                            <option value="${supplier.supplierId}" ${supplier.supplierId == currentImport.supplierId ? 'selected' : ''}><c:out value='${supplier.supplierName}'/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="empId">Manager</label>
                                </th>
                                <td>
                                    <select name="empId" id="empId" class="form-select">
                                        <c:forEach var="emp" items="${employeeList}">
                                            <option value="${emp.empId}" ${emp.empId == currentImport.empId ? 'selected' : ''}><c:out value='${emp.empName}'/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="importDate">Import Date</label>
                                </th>
                                <td>
                                    <input type="date" name="importDate" id="importDate" value="<c:out value='${currentImport.importDate}'/>" class="form-control">
                                </td>
                            </tr>

                            <tr>
                                <td></td>
                                <td>
                                    <button class="btn btn-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='import'>
                                           <c:param name='view' value='list'/>
                                       </c:url>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the import which id <c:out value='${param.id}'/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value='import'>
                           <c:param name='view' value='list'/>
                       </c:url>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
