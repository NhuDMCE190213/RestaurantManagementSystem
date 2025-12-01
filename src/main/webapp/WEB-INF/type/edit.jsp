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
            <h1 class="section-title mb-1">Edit Type</h1>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentType}">
                    <form method="post" action="<c:url value='type'/>">
                        <input type="hidden" name="id" value="${currentType.typeId}" />
                        <table class="table">
                            <tr><td></td><td></td></tr>

                            <tr>
                                <th>
                                    <label for="id">ID</label>
                                </th>
                                <td>
                                    <label name="id" id="id"><c:out value="${currentType.typeId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="typeName">Name</label>
                                </th>
                                <td>
                                    <input type="text" name="typeName" id="typeName" value="<c:out value='${currentType.typeName}'/>" class="form-control" required>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="description">Description</label>
                                </th>
                                <td>
                                    <input type="text" name="description" id="description" value="<c:out value='${currentType.description}'/>" class="form-control">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='type'/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                    <c:otherwise>
                        <h2 class="mt-5">Not found the type which id <c:out value="${param.id}"/>. Please check the information.</h2>
                        <a class="btn btn-outline-dark" href="<c:url value='type'/>">Back</a>
                    </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>