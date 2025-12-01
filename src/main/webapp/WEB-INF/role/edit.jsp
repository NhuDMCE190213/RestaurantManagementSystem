<%-- 
    Document   : remove
    Created on : 21 Jun 2025, 4:58:35 PM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Edit Role - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Role</h1>
            </div>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentRole}">
                    <form method="post" action="<c:url value="role">
                              <c:param name="id" value="${param.id}"/>  
                          </c:url>">
                        <table class="table">
                            <tr>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="id">ID</label>
                                </th>
                                <td>
                                    <label id="id"><c:out value="${currentRole.id}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="name">Name</label>
                                </th>
                                <td>
                                    <input type="text" name="name" id="name" value="<c:out value="${currentRole.name}"/>" class="form-control" required>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="name">Description</label>
                                </th>
                                <td>
                                    <input type="text" name="description" id="description" value="<c:out value="${currentRole.description}"/>" class="form-control">
                                </td>
                            </tr>

                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value="role"/>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the Role which id <c:out value="${param.id}"/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value="role"/>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
