<%-- 
    Document   : edit
    Created on : 5 Nov 2025
    Author     : Huynh Thai Duy Phuong
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Edit Menu Item form">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Menu Item</h1>
            </div>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty menuItem}">


                    <form method="post" action="<c:url value="menuitem"/>" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="edit"/>
                        <input type="hidden" name="id" value="${menuItem.menuItemId}"/>
                        <input type="hidden" name="existingImageUrl" value="${menuItem.imageUrl}"/>

                        <table class="table">

                            <tr>
                                <th><label class="form-label">Item ID</label></th>
                                <td><label class="form-control"><c:out value="${menuItem.menuItemId}"/></label></td>
                            </tr>

                            <tr>
                                <th><label class="form-label">Current Image</label></th>
                                <td>
                                    <img src="${menuItem.imageUrl}" alt="Current Menu Image" style="max-height: 100px; max-width: 100%; border: 1px solid #ccc; margin-bottom: 10px;">
                                </td>
                            </tr>

                            <tr>
                                <th><label for="imageFile" class="form-label">New Image (Optional)</label></th>
                                <td>
                                    <%-- Changed to type="file". Not required on edit if image exists --%>
                                    <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/*">
                                    <small class="form-text text-muted">Leave blank to keep the current image.</small>
                                </td>
                            </tr>

                            <tr>
                                <th><label for="itemName" class="form-label">Item Name</label></th>
                                <td><input type="text" id="itemName" name="itemName" class="form-control" value="<c:out value="${menuItem.itemName}"/>" required></td>
                            </tr>

                            <tr>
                                <th><label for="categoryId" class="form-label">Category</label></th>
                                <td>
                                    <select name="categoryId" id="categoryId" class="form-select" required>
                                        <c:forEach var="category" items="${categories}">
                                            <option value="${category.categoryId}" class="form-options" 
                                                    ${(menuItem.category.categoryId eq category.categoryId) ? 'selected' : ''}>
                                                <c:out value="${category.categoryName}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th><label for="recipeId" class="form-label">Recipe</label></th>
                                <td>
                                    <select name="recipeId" id="recipeId" class="form-select" required>
                                        <c:forEach var="recipe" items="${recipes}">
                                            <option value="${recipe.recipeId}" class="form-options" 
                                                    ${(menuItem.recipe.recipeId eq recipe.recipeId) ? 'selected' : ''}>
                                                <c:out value="${recipe.recipeName}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th><label for="price" class="form-label">Price(VND)</label></th>
                                <td><input type="number" id="price" name="price" class="form-control" step="1" min="1" value="<c:out value="${menuItem.price}"/>" required></td>
                            </tr>

                            <tr>
                                <th><label for="description" class="form-label">Description</label></th>
                                <td><textarea id="description" name="description" class="form-control"><c:out value="${menuItem.description}"/></textarea></td>
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
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Menu Item with ID <c:out value="${param.id}"/> not found. Please check the information.</h2>

                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>