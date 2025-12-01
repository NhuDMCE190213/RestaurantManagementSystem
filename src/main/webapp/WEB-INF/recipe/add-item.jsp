<%-- 
    Document   : add-item
    Created on : Nov 5, 2025, 7:57:00 AM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<style>
    /* Style để tạo dạng hàng giống ảnh 2 */
    .form-row-box {
        border-top: 1px solid #e9ecef;
        padding: 18px 0;
        display: flex;
        align-items: center;
    }
    .form-row-box:first-of-type {
        border-top: none;
        padding-top: 24px;
    }
    .form-label-col {
        padding-right: 20px;
        text-align: left;
        font-weight: 600;
        color: #212529;
    }
    .form-input-col .form-control,
    .form-input-col .form-select {
        border-radius: 6px;
        height: 44px;
        box-shadow: none;
        border: 1px solid #e9ecef;
    }
    .form-note {
        color: #6c757d;
        font-size: 0.9rem;
        margin-top: 6px;
    }
    .form-card {
        background: #fff;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 0 0 1px rgba(0,0,0,0.02);
    }
    .action-buttons {
        padding: 18px 0 6px 0;
    }
</style>

<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Add Item to Recipe</h2>
        </div>

        <c:if test="${empty currentRecipe}">
            <div class="alert alert-warning mt-3">Recipe not found.</div>
        </c:if>

        <c:if test="${not empty currentRecipe}">
            <div class="form-card mt-3">
                <form method="post" action="${pageContext.request.contextPath}/recipe" class="w-100">
                    <input type="hidden" name="action" value="add_item" />
                    <input type="hidden" name="recipe_id" value="${currentRecipe.recipeId}" />

                    <!-- Ingredient -->
                    <div class="form-row-box row">
                        <div class="col-md-3 form-label-col">
                            <label class="form-label mb-0">Code</label>
                        </div>
                        <div class="col-md-9 form-input-col">
                            <select name="ingredient_id" class="form-select" required>
                                <option value="">-- Select ingredient --</option>
                                <c:forEach var="ing" items="${ingredients}">
                                    <option value="${ing.ingredientId}">${ing.ingredientName}</option>
                                </c:forEach>
                            </select>
                            <c:if test="${empty ingredients}">
                                <div class="form-note">No ingredients available. Please add ingredients first.</div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Quantity + Unit on the same row in form style of image2 -->
                    <div class="form-row-box row">
                        <div class="col-md-3 form-label-col">
                            <label class="form-label mb-0">Quantity</label>
                        </div>
                        <div class="col-md-9">
                            <div class="row g-2">
                                <div class="col-md-4 form-input-col">
                                    <input name="quantity" type="number" step="0.01" class="form-control" />
                                </div>
                                <div class="col-md-4 form-input-col">
                                    <input name="unit" type="text" class="form-control" />
                                </div>
                                
                            </div>
                        </div>
                    </div>

                    <!-- Note -->
                    <div class="form-row-box row">
                        <div class="col-md-3 form-label-col">
                            <label class="form-label mb-0">Note</label>
                        </div>
                        <div class="col-md-9 form-input-col">
                            <input name="note" type="text" class="form-control" />
                        </div>
                    </div>

                    <div class="form-row-box row action-buttons">
                        <div class="col-md-3"></div>
                        <div class="col-md-9">
                            <button class="btn btn-success me-2" type="submit">Save</button>
                            <a href="<c:url value="recipe">
                                   <c:param name="view" value="view"/>
                                   <c:param name="id" value="${currentRecipe.recipeId}"/>
                               </c:url>"class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </div>

                </form>
            </div>
        </c:if>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>
