<%-- 
    Document   : view
    Created on : Oct 25, 2025, 2:37:56 PM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="title" value="Recipe Item List - Yummy"/>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>
<style>
    /* Tăng kích thước và cân bằng các info card */
    .info-row .info-card {
        min-height: 110px; /* tăng chiều cao để ko bị khoảng trống */
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 20px;
        border-radius: .5rem;
        background: #f8f9fa;
    }
    .info-row .info-card small {
        letter-spacing: .03em;
    }
    .info-row .info-card p {
        margin: 0;
        font-weight: 600;
    }
    /* Nếu muốn các card cùng chiều cao ở cùng 1 hàng */
    .info-row .col-md-4 {
        display: flex;
    }
    .info-row .col-md-4 > .info-card {
        flex: 1;
    }

    /* Button style (giữ consistent) */
    .btn-pill {
        border-radius: 999px;
        padding-left: 14px;
        padding-right: 14px;
    }
</style>
<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Recipe Detail</h2>

        </div>

        <c:choose>
            <c:when test="${empty currentRecipe}">
                <div class="alert alert-warning mt-3">Recipe not found.</div>
            </c:when>
            <c:otherwise>

                <!-- Info cards: 3 cards rộng, đều, không khoảng trống -->
                <div class="row g-3 g-md-4 mb-3 info-row">
                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Menu Item ID</small>
                            <p class="fs-5 fw-semibold">#<c:out value='${currentRecipe.recipeId}'/></p>
                        </div>
                    </div>

                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Menu Item Name</small>
                            <p class="mb-0 fw-semibold"><c:out value='${currentRecipe.recipeName}'/></p>
                        </div>
                    </div>

                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Items count</small>
                            <p class="mb-0 fw-semibold"><c:out value='${fn:length(currentRecipe.items)}'/> item(s)</p>
                        </div>
                    </div>
                </div>



                <!-- Items table -->
                <div class="table-responsive mt-3">
                    <table class="table align-middle admin-table">
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">Ingredient</th>
                                <th scope="col">Quantity</th>
                                <th scope="col">Unit</th>
                                <th scope="col">Note</th>
                                <th scope="col" class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty currentRecipe.items}">
                                    <tr>
                                        <td colspan="7" class="text-center text-muted">No items</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="it" items="${currentRecipe.items}" varStatus="loop">
                                        <tr>
                                            <td><c:out value="${loop.index + 1}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty it.ingredientName}">
                                                        <c:out value="${it.ingredientName}" />
                                                    </c:when>
                                                    <c:otherwise>Unknown</c:otherwise>
                                                </c:choose>
                                            </td>

                                            <td><c:out value="${it.quantity}" /></td>
                                            <td><c:out value="${it.unit}" /></td>
                                            <td><c:out value="${it.note}" /></td>
                                            <td class="text-end">
                                                <div class="action-button-group d-flex justify-content-end gap-2">
                                                    <!-- Edit icon -->
                                                    <button type="button" class="btn btn-outline-secondary btn-icon btn-edit"
                                                            title="Edit Item" aria-label="Edit"
                                                            onclick='openEditItemModal(${it.recipeItemId}, ${it.ingredientId}, "${it.quantity}", "${fn:escapeXml(it.unit)}", "${fn:escapeXml(it.note)}", "${it.status}")'>
                                                        <i class="bi bi-pencil"></i>
                                                    </button>

                                                    <!-- Delete -->
                                                    <button type="button" class="btn btn-outline-danger btn-icon btn-delete"
                                                            title="Delete Item" aria-label="Delete"
                                                            onclick="showDeleteItemPopup(${it.recipeItemId})">
                                                        <i class="bi bi-x-circle"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="mt-4">
        <h4 class="mb-2">Add Item</h4>

        <div class="add-item-card">
            <form method="post" action="${pageContext.request.contextPath}/recipe" class="row g-3 add-item-row align-items-end">
                <input type="hidden" name="action" value="add_item" />
                <input type="hidden" name="menu_item_id" value="${currentRecipe.recipeId}" />

                <!-- Ingredient select: full width -->
                <div class="col-12">
                    <label class="form-label mb-1">Ingredient Name</label>
                    <select id="add_ingredient_select" name="ingredient_id" class="form-select" required>
                        <option value="">-- Select ingredient --</option>
                        <c:forEach var="ing" items="${ingredients}">
                            <%-- embed ingredient's declared unit in data-unit attribute (null-safe) --%>
                            <option value="${ing.ingredientId}" data-unit="${fn:toLowerCase(ing.unit)}">${ing.ingredientName}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty ingredients}">
                        <div class="form-note small mt-1">No ingredients available. Please add ingredients first.</div>
                    </c:if>
                </div>

                <!-- Row: Quantity | Unit | Note | Button -->
                <div class="col-12 col-md-2">
                    <label class="form-label">Quantity</label>
                    <input name="quantity" type="number" min="0.01" step="0.01" class="form-control" placeholder="e.g. 1.5" required />
                </div>

                <div class="col-12 col-md-2">
                    <label class="form-label">Unit</label>
                    <select id="add_unit_select" name="unit" class="form-select">
                        <option value="">-- Select Unit --</option>
                        <option value="pcs">pcs</option>
                        <option value="kg">kg</option>
                        <option value="g">g</option>
                        <option value="l">l</option>
                        <option value="ml">ml</option>
                    </select>
                </div>

                <div class="col-12 col-md-6">
                    <label class="form-label">Note</label>
                    <input name="note" type="text" class="form-control" placeholder="Optional note (e.g. chopped, diced...)" />
                </div>

                <div class="col-12 col-md-2 d-flex justify-content-md-end">
                    <button class="btn btn-success btn-add-item" type="submit">
                        <i class="bi bi-plus-circle me-1"></i> Add Item
                    </button>
                </div>
            </form>
        </div>
    </div>

</div>
</section>

<!-- Edit item modal -->
<div class="modal fade" id="editItemModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <form id="editItemForm" method="post" action="${pageContext.request.contextPath}/recipe">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Recipe Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" name="action" value="edit_item"/>
                    <input type="hidden" id="edit_recipe_item_id" name="recipe_item_id" value=""/>
                    <input type="hidden" id="edit_recipe_id" name="menu_item_id" value="${currentRecipe.recipeId}" />

                    <div class="edit-item-card">
                        <div class="row g-3 edit-item-row align-items-end">
                            <div class="col-12">
                                <label class="form-label mb-1">Ingredient</label>
                                <select id="edit_ingredient_id" name="ingredient_id" class="form-select" required>
                                    <option value="">-- Select ingredient --</option>
                                    <c:forEach var="ing" items="${ingredients}">
                                        <option value="${ing.ingredientId}" data-unit="${fn:toLowerCase(ing.unit)}">${ing.ingredientName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-12 col-md-2">
                                <label class="form-label">Quantity</label>
                                <input id="edit_quantity" name="quantity" type="number" min="0.01" step="0.01" class="form-control" placeholder="e.g. 1.0" />
                            </div>

                            <div class="col-12 col-md-2">
                                <label class="form-label">Unit</label>
                                <select id="edit_unit" name="unit" class="form-select">
                                    <option value="">-- Select Unit --</option>
                                    <option value="pcs">pcs</option>
                                    <option value="kg">kg</option>
                                    <option value="g">g</option>
                                    <option value="l">l</option>
                                    <option value="ml">ml</option>
                                </select>
                            </div>

                            <div class="col-12 col-md-6">
                                <label class="form-label">Note</label>
                                <input id="edit_note" name="note" type="text" class="form-control" placeholder="Optional note" />
                            </div>

                            <div class="col-12 col-md-2 d-flex justify-content-md-end">
                                <button type="submit" class="btn btn-primary btn-save-item">
                                    <i class="bi bi-save me-1"></i> Save changes
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <small class="text-muted me-auto">You can edit ingredient, quantity, unit or note here.</small>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete item modal -->
<div class="modal fade" id="deleteItemPopup" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/recipe">
                <div class="modal-header"><h5 class="modal-title">Confirm Delete</h5></div>
                <div class="modal-body text-danger">
                    <p>Are you sure to delete this item?</p>
                    <input type="hidden" id="hiddenDeleteRecipeItemId" name="recipe_item_id" value=""/>
                    <input type="hidden" name="menu_item_id" value="${currentRecipe.recipeId}">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" name="action" value="delete_item" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Helper: lấy nhóm unit giống backend
    function unitGroup(unit) {
        if (!unit)
            return 'unknown';
        unit = unit.toString().trim().toLowerCase();
        if (['g', 'gram', 'grams', 'kg', 'kilogram', 'kilograms'].includes(unit))
            return 'mass';
        if (['ml', 'milliliter', 'milliliters', 'l', 'liter', 'litre'].includes(unit))
            return 'volume';
        if (['pc', 'pcs', 'piece', 'pieces'].includes(unit))
            return 'count';
        return 'unknown';
    }

    function isAllowedOption(ingredientUnit, optionUnit) {
        if (!ingredientUnit)
            return true;
        return unitGroup(ingredientUnit) === unitGroup(optionUnit);
    }

    /**
     * Lọc các option của unitSelect dựa vào ingredientSelect.
     * preserveValue:
     *   - true  : giữ giá trị hiện tại nếu hợp lệ; nếu không sẽ chọn canonical (nếu có) hoặc first allowed.
     *   - false : KHÔNG tự chọn; nếu current value rỗng giữ nguyên (placeholder).
     */
    function filterUnitSelectByIngredient(ingredientSelectElem, unitSelectElem, preserveValue) {
        var ingOption = ingredientSelectElem.selectedOptions[0];
        var ingUnit = ingOption ? ingOption.getAttribute('data-unit') : null;
        var currentValue = unitSelectElem.value;

        // ensure placeholder option (value="") is visible & enabled
        for (var i = 0; i < unitSelectElem.options.length; i++) {
            var opt = unitSelectElem.options[i];
            if (opt.value === "") {
                opt.hidden = false;
                opt.disabled = false;
                break;
            }
        }

        // hide/disable incompatible options
        for (var i = 0; i < unitSelectElem.options.length; i++) {
            var opt = unitSelectElem.options[i];
            var optVal = opt.value;
            if (optVal === '')
                continue; // placeholder kept visible
            if (isAllowedOption(ingUnit, optVal)) {
                opt.hidden = false;
                opt.disabled = false;
            } else {
                opt.hidden = true;
                opt.disabled = true;
            }
        }

        // Selection behavior:
        if (preserveValue) {
            // prefer to keep current value if still allowed
            if (currentValue && currentValue !== "" && !unitSelectElem.querySelector('option[value="' + currentValue + '"]').disabled) {
                unitSelectElem.value = currentValue;
                return;
            }
            // otherwise try to select canonical (ingredient's unit) if present and allowed
            if (ingUnit) {
                for (var i = 0; i < unitSelectElem.options.length; i++) {
                    var opt = unitSelectElem.options[i];
                    if (opt.value && opt.value.toLowerCase() === ingUnit.toLowerCase() && !opt.disabled) {
                        unitSelectElem.value = opt.value;
                        return;
                    }
                }
            }
            // fallback: choose first allowed non-empty option
            for (var i = 0; i < unitSelectElem.options.length; i++) {
                var opt = unitSelectElem.options[i];
                if (opt.value && !opt.disabled) {
                    unitSelectElem.value = opt.value;
                    return;
                }
            }
            // if none found, leave placeholder
            unitSelectElem.value = "";
        } else {
            // preserveValue === false -> DO NOT auto-select.
            // If current value is empty (placeholder), keep it.
            // If current value is non-empty but now disabled, reset to placeholder.
            var curOpt = unitSelectElem.querySelector('option[value="' + currentValue + '"]');
            if (currentValue && curOpt && curOpt.disabled) {
                unitSelectElem.value = "";
            }
            // otherwise keep whatever user had (or placeholder)
        }
    }

    // Open edit modal and filter; preserveValue = true so we restore passed unit when possible
    function openEditItemModal(recipeItemId, ingredientId, quantity, unit, note, status) {
        document.getElementById('edit_recipe_item_id').value = recipeItemId;
        document.getElementById('edit_ingredient_id').value = ingredientId;
        document.getElementById('edit_quantity').value = quantity;
        document.getElementById('edit_note').value = note || '';

        var editIngredientSelect = document.getElementById('edit_ingredient_id');
        var editUnitSelect = document.getElementById('edit_unit');

        // ensure the ingredient select has the value set before filtering
        setTimeout(function () {
            filterUnitSelectByIngredient(editIngredientSelect, editUnitSelect, true);
            // set unit after filtering (only if allowed)
            if (unit) {
                var opt = editUnitSelect.querySelector('option[value="' + unit + '"]');
                if (opt && !opt.disabled) {
                    editUnitSelect.value = unit;
                }
            }
        }, 0);

        var modal = new bootstrap.Modal(document.getElementById('editItemModal'));
        modal.show();
    }

    function showDeleteItemPopup(id) {
        document.getElementById('hiddenDeleteRecipeItemId').value = id;
        var modal = new bootstrap.Modal(document.getElementById('deleteItemPopup'));
        modal.show();
    }

    // DOM ready: attach listeners
    document.addEventListener('DOMContentLoaded', function () {
        var addIng = document.getElementById('add_ingredient_select');
        var addUnit = document.getElementById('add_unit_select');
        if (addIng && addUnit) {
            addIng.addEventListener('change', function () {
                // preserveValue = false so placeholder stays if user hasn't chosen unit
                filterUnitSelectByIngredient(addIng, addUnit, false);
            });
            // initial: don't auto-select, keep placeholder
            filterUnitSelectByIngredient(addIng, addUnit, false);
        }

        var editIng = document.getElementById('edit_ingredient_id');
        var editUnit = document.getElementById('edit_unit');
        if (editIng && editUnit) {
            editIng.addEventListener('change', function () {
                // when user changes ingredient in modal, we can preserve current unit selection if any
                filterUnitSelectByIngredient(editIng, editUnit, true);
            });
        }
    });
</script>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>