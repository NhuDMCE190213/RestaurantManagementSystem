<%-- 
    Document   : edit-item
    Created on : Nov 5, 2025, 7:57:09 AM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<style>
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
            <h2>Edit Item</h2>
        </div>

        <c:if test="${empty currentItem}">
            <div class="alert alert-warning mt-3">Item not found.</div>
        </c:if>

        <c:if test="${not empty currentItem}">
            <div class="form-card mt-3">
                <form method="post" action="${pageContext.request.contextPath}/recipe" class="w-100">
                    <input type="hidden" name="action" value="edit_item" />
                    <input type="hidden" name="recipe_item_id" value="${currentItem.recipeItemId}" />
                    <input type="hidden" name="recipe_id" value="${currentItem.recipeId}">

                    <!-- Ingredient -->
                    <div class="form-row-box row">
                        <div class="col-md-3 form-label-col">
                            <label class="form-label mb-0">Ingredient</label>
                        </div>
                        <div class="col-md-9 form-input-col">
                            <select name="ingredient_id" class="form-select" required>
                                <option value="">-- Select ingredient --</option>
                                <c:forEach var="ing" items="${ingredients}">
                                    <option value="${ing.ingredientId}"
                                            <c:if test="${ing.ingredientId == currentItem.ingredientId}">selected</c:if>>
                                        ${ing.ingredientName}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${empty ingredients}">
                                <div class="form-note">No ingredients available. Please add ingredients first.</div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Quantity + Unit on same row -->
                    <div class="form-row-box row">
                        <div class="col-md-3 form-label-col">
                            <label class="form-label mb-0">Quantity</label>
                        </div>
                        <div class="col-md-9">
                            <div class="row g-2">
                                <div class="col-md-4 form-input-col">
                                    <input id="edit_quantity" name="quantity" type="number" step="0.01" class="form-control"
                                           value="${currentItem.quantity}" />
                                </div>
                                <div class="col-md-4 form-input-col">
                                    <input id="edit_unit" name="unit" type="text" class="form-control"
                                           value="${fn:escapeXml(currentItem.unit)}" />
                                </div>
                                <div class="col-md-4">
                                    <!-- spare column if needed -->
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
                            <input id="edit_note" name="note" type="text" class="form-control"
                                   value="${fn:escapeXml(currentItem.note)}" />
                        </div>
                    </div> 

                    <!-- Action buttons -->
                    <div class="form-row-box row action-buttons">
                        <div class="col-md-3"></div>
                        <div class="col-md-9">
                            <button class="btn btn-success me-2" type="submit">Save</button>
                            <a href="${pageContext.request.contextPath}/recipe?view=view&id=${currentItem.recipeId}" class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </div>

                </form>
            </div>
        </c:if>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>