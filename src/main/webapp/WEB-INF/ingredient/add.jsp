<%-- 
    Document   : add
    Created on : Oct 22, 2025
    Author     : TruongBinhTrong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Includes the header, navigation, and opening tags for the admin layout --%>
<title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Ingredient Add Form">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">Add Ingredient</h1>
        </div>

        <div class="container px-4 py-3">
            <form method="post" action="<c:url value='ingredient'/>">
                <table class="table">

                    <%-- Ingredient Name --%>
                    <tr>
                        <th width="30%">
                            <label for="ingredientName">Name</label>
                        </th>
                        <td>
                            <input type="text" name="ingredientName" id="ingredientName" class="form-control" required>
                        </td>
                    </tr>

                    <%-- Ingredient Type (Dropdown) --%>
                    <tr>
                        <th>
                            <label for="typeSelect">Type</label>
                        </th>
                        <td>
                            <select name="typeId" id="typeSelect" class="form-select" required>
                                <option value="">Select a type</option>
                                <c:forEach var="t" items="${typesList}">
                                    <option value="${t.typeId}">${t.typeName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="unit">Unit</label>
                        </th>
                        <td>
                            <div style="display: flex; gap: 10px; align-items: center;">
                                <select id="unit" name="unit" onchange="toggleBoxInput()" class="form-select">
                                    <option value="">-- Select Unit --</option>
                                    <option value="Kilogram">Kilogram</option>
                                    <option value="Gram">Gram</option>
                                    <option value="Liter">Liter</option>
                                    <option value="Milliliter">Milliliter</option>
                                    <option value="Bottle">Bottle</option>
                                    <option value="Can">Can</option>
                                    <option value="Box">Box</option>
                                    <option value="Piece">Piece</option>
                                </select>

                            </div>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="expirationDate">Expiration date</label>
                        </th>
                        <td>
                            <input type="date" id="expirationDate" name="expirationDate" class="form-control" aria-describedby="expirationHelp" min="${today}">
                            <small id="expirationHelp" class="text-muted">Optional. Leave blank if the ingredient does not expire.</small>
                        </td>
                    </tr>




                    <%-- Action Buttons --%>
                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='ingredient'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>