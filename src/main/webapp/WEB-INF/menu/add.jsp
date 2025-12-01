<%-- 
    Document   : add
    Created on : 5 Nov 2025
    Author     : Huynh Thai Duy Phuong
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Add Menu Item form">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Menu Item</h1>
            </div>
        </div>

        <div class="container">

            <form method="post" action="<c:url value="menuitem"/>" enctype="multipart/form-data">
                <input type="hidden" name="action" value="add"/>
                <table class="table table align-middle admin-table">

                    <tr>
                        <th><label for="itemName" class="form-label">Item Name</label></th>
                        <td><input type="text" id="itemName" name="itemName" class="form-control" required></td>
                    </tr>

                    <tr>
                        <th><label for="categoryId" class="form-label">Category</label></th>
                        <td>
                            <select name="categoryId" id="categoryId" class="form-select" required>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.categoryId}" class="form-options"><c:out value="${category.categoryName}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="recipeId" class="form-label">Recipe</label></th>
                        <td>
                            <select name="recipeId" id="recipeId" class="form-select" required>
                                <c:forEach var="recipe" items="${recipes}">
                                    <option value="${recipe.recipeId}" class="form-options"><c:out value="${recipe.recipeName}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="price" class="form-label">Price(VND)</label></th>
                        <td><input type="number" id="price" name="price" class="form-control" step="1" min="1" required></td>
                    </tr>

                    <tr>
                        <th><label for="imageFile" class="form-label">Image File (Upload)</label></th>
                        <td>

                            <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/*" required>
                        </td>
                    </tr>

                    <tr>
                        <th><label for="description" class="form-label">Description</label></th>
                        <td><textarea id="description" name="description" class="form-control"></textarea></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="menuitem"/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>