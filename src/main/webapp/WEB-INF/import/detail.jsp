<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Import details">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-lg-row gap-3 gap-lg-0 justify-content-between align-items-lg-center">
            <div class="d-flex flex-column gap-1">
                <div class="d-flex align-items-center gap-3">
                    <h1 class="section-title mb-0">Import Details</h1>
                </div>
            </div>
            <div class="d-flex flex-column flex-sm-row gap-2 align-items-stretch align-items-sm-center">
                <c:if test="${not empty currentImport && fn:toLowerCase(currentImport.status) ne 'completed' && fn:toLowerCase(currentImport.status) ne 'active'}">
                    <form method="post" action="<c:url value='import'/>" class="d-inline-flex">
                        <input type="hidden" name="action" value="confirm">
                        <input type="hidden" name="importId" value="${currentImport.importId}">
                        <button type="submit" class="btn btn-primary"><i class="bi bi-box-seam"></i> Confirm Import</button>
                    </form>
                </c:if>         
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty currentImport}">
                <div class="card-body px-4 pb-4">
                    <div class="row g-3 g-md-4 mb-4">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light h-100">
                                <small class="text-uppercase text-muted fw-semibold">Import ID</small>
                                <p class="fs-5 fw-semibold mb-0">#<c:out value='${currentImport.importId}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light h-100">
                                <small class="text-uppercase text-muted fw-semibold">Manager</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.empName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light h-100">
                                <small class="text-uppercase text-muted fw-semibold">Supplier</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.supplierName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light h-100">
                                <small class="text-uppercase text-muted fw-semibold">Contact</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.contactPerson}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-lg-4">
                            <div class="border rounded-3 p-3 bg-light h-100">
                                <small class="text-uppercase text-muted fw-semibold">Import date</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.importDate}'/></p>
                            </div>
                        </div>
                    </div>

                    <div class="mb-5">
                        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-3 gap-2">
                            <h2 class="h5 mb-0">Current Line Items</h2>
                        </div>
                        <div class="table-responsive border rounded-3">
                            <table class="table align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">No.</th>
                                        <th scope="col">Ingredient</th>
                                        <th scope="col" class="text-end">Quantity</th>
                                        <th scope="col" class="text-end">Unit</th>
                                        <th scope="col" class="text-end">Unit Price (VND)</th>
                                        <th scope="col" class="text-end">Total (VND)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${importDetails == null || empty importDetails}">
                                            <tr>
                                                <td colspan="6" class="text-center text-muted py-4">No line items have been added yet.</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="detail" items="${importDetails}" varStatus="loop">
                                                <tr>
                                                    <td><c:out value='${loop.index + 1}'/></td>
                                                    <td><c:out value='${detail.ingredientName}'/></td>
                                                    <td class="text-end"><c:out value='${detail.quantity}'/></td>
                                                    <td class="text-end"><c:out value='${detail.unit}'/></td>
                                                    <td class="text-end"><c:out value='${detail.unitPrice}'/></td>
                                                    <td class="text-end"><c:out value='${detail.totalPrice}'/></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <c:set var="importCompleted" value="${not empty currentImport && (fn:toLowerCase(currentImport.status) eq 'completed' || fn:toLowerCase(currentImport.status) eq 'active')}"/>

                    <c:if test="${importCompleted}">
                        <div class="alert alert-success d-flex align-items-center gap-2" role="alert">
                            <i class="bi bi-check-circle-fill"></i>
                            <span>This import has been confirmed. Ingredient already reflects these line items.</span>
                        </div>
                    </c:if>

                    <div class="row g-4 align-items-start">
                        <div class="col-12 col-lg-7">
                            <div class="d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3 mb-3">
                                <h2 class="h5 mb-0">Ingredient Catalog</h2>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                                    <input type="search" id="ingredientSearch" class="form-control" placeholder="Search by ingredient, type, or unit">
                                </div>
                            </div>

                            <div class="table-responsive border rounded-3">
                                <table class="table table-hover align-middle mb-0" id="ingredientTable">
                                    <thead class="table-light">
                                        <tr>
                                            <th scope="col">Ingredient</th>
                                            <th scope="col">Type</th>
                                            <th scope="col">Unit</th>
                                            <th scope="col" class="text-end">Quantity</th>
                                            <th scope="col" class="text-end">Select</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${ingredientList == null || empty ingredientList}">
                                                <tr>
                                                    <td colspan="5" class="text-danger text-center py-4">No ingredients available. Please add inventory first.</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="ing" items="${ingredientList}">
                                                    <tr class="ingredient-row" data-id="${ing.ingredientId}" data-name="${ing.ingredientName}" data-unit="${ing.unit}" data-type="${ing.typeName}" data-stock="${ing.totalQuantity}" data-search="${fn:toLowerCase(ing.ingredientName)} ${fn:toLowerCase(ing.typeName)} ${fn:toLowerCase(ing.unit)}">
                                                        <td><strong><c:out value='${ing.ingredientName}'/></strong></td>
                                                        <td><c:out value='${ing.typeName}'/></td>
                                                        <td><span class="badge bg-secondary-subtle text-dark fw-semibold"><c:out value='${ing.unit}'/></span></td>
                                                        <td class="text-end"><c:out value='${ing.totalQuantity}'/></td>
                                                        <td class="text-end">
                                                            <button type="button" class="btn btn-outline-primary btn-sm select-ingredient" aria-label="Select ${ing.ingredientName}"><i class="bi bi-plus-circle"></i></button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="col-12 col-lg-5">
                            <form method="post" action="<c:url value='import'/>" class="border rounded-3 p-4 h-100 bg-light d-flex flex-column gap-3">
                                <input type="hidden" name="action" value="addDetail">
                                <input type="hidden" name="importId" value="${currentImport.importId}">
                                <input type="hidden" name="ingredientId" id="selectedIngredientId">

                                <div>
                                    <span class="text-uppercase text-muted small">Selected ingredient</span>
                                    <p class="fs-5 fw-semibold mb-1" id="selectedIngredientName">No ingredient selected</p>
                                    <div class="d-flex flex-wrap gap-2 text-muted small" id="selectedIngredientMeta">
                                        <span class="badge bg-body-secondary text-dark">Unit: <span id="selectedUnitLabel">--</span></span>
                                        <span class="badge bg-body-secondary text-dark">Type: <span id="selectedTypeLabel">--</span></span>
                                        <span class="badge bg-body-secondary text-dark">Quantity: <span id="selectedStockLabel">--</span></span>
                                    </div>
                                </div>

                                <div>
                                    <label class="form-label fw-semibold" for="quantity">Quantity</label>
                                    <div class="input-group stepper" data-field="quantity">
                                        <button type="button" class="btn btn-outline-secondary stepper-btn" data-step-target="quantity" data-step-value="-1" aria-label="Decrease quantity" ${importCompleted ? 'disabled' : ''}><i class="bi bi-dash"></i></button>
                                        <input type="number" class="form-control text-center" name="quantity" id="quantity" value="1" min="1" ${importCompleted ? 'readonly' : ''} required>
                                        <button type="button" class="btn btn-outline-secondary stepper-btn" data-step-target="quantity" data-step-value="1" aria-label="Increase quantity" ${importCompleted ? 'disabled' : ''}><i class="bi bi-plus"></i></button>
                                    </div>
                                </div>

                                <div>
                                    <label class="form-label fw-semibold" for="unitPrice">Unit price (VND)</label>
                                    <div class="input-group stepper" data-field="unitPrice">
                                        <button type="button" class="btn btn-outline-secondary stepper-btn" data-step-target="unitPrice" data-step-value="-1000" aria-label="Decrease unit price" ${importCompleted ? 'disabled' : ''}><i class="bi bi-dash"></i></button>
                                        <input type="number" class="form-control text-center" name="unitPrice" id="unitPrice" value="1000" min="1" step="1" ${importCompleted ? 'readonly' : ''} required>
                                        <button type="button" class="btn btn-outline-secondary stepper-btn" data-step-target="unitPrice" data-step-value="1000" aria-label="Increase unit price" ${importCompleted ? 'disabled' : ''}><i class="bi bi-plus"></i></button>
                                    </div>
                                </div>

                                <div class="d-flex gap-2 justify-content-end mt-auto">
                                    <button type="submit" class="btn btn-success" id="saveDetailButton" disabled data-import-completed="${importCompleted}">Add item</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-body px-4 pb-4">
                    <div class="alert alert-warning mb-3" role="alert">
                        Unable to locate the requested import record. Please verify the link or return to the list view.
                    </div>
                    <a class="btn btn-outline-secondary" href="<c:url value='import'>
                           <c:param name='view' value='list'/>
                       </c:url>"><i class="bi bi-arrow-left"></i> Back to list</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const ingredientTable = document.getElementById('ingredientTable');
        const searchInput = document.getElementById('ingredientSearch');
        const selectedIngredientId = document.getElementById('selectedIngredientId');
        const selectedIngredientName = document.getElementById('selectedIngredientName');
        const selectedUnitLabel = document.getElementById('selectedUnitLabel');
        const selectedTypeLabel = document.getElementById('selectedTypeLabel');
        const selectedStockLabel = document.getElementById('selectedStockLabel');
        const selectedIngredientMeta = document.getElementById('selectedIngredientMeta');
        const saveButton = document.getElementById('saveDetailButton');

        const quantityInput = document.getElementById('quantity');
        const unitPriceInput = document.getElementById('unitPrice');
        const importCompleted = saveButton ? saveButton.getAttribute('data-import-completed') === 'true' : false;

        if (!selectedIngredientId || !saveButton) {
            return;
        }

        let activeRow = null;

        function highlightRow(row) {
            if (activeRow) {
                activeRow.classList.remove('table-active');
            }
            activeRow = row;
            if (activeRow) {
                activeRow.classList.add('table-active');
            }
        }

        function updateSelectedIngredient(row) {
            const id = row.getAttribute('data-id');
            const name = row.getAttribute('data-name');
            const unit = row.getAttribute('data-unit');
            const type = row.getAttribute('data-type');
            const stock = row.getAttribute('data-stock');

            selectedIngredientId.value = id;
            selectedIngredientName.textContent = name || 'No ingredient selected';
            selectedUnitLabel.textContent = unit || '--';
            selectedTypeLabel.textContent = type || '--';
            selectedStockLabel.textContent = stock || '--';

            if (name) {
                selectedIngredientMeta.classList.remove('text-muted');
                saveButton.removeAttribute('disabled');
            } else {
                selectedIngredientMeta.classList.add('text-muted');
                saveButton.setAttribute('disabled', 'disabled');
            }
        }

        if (ingredientTable) {
            ingredientTable.addEventListener('click', function (event) {
                if (importCompleted) {
                    return;
                }
                const target = event.target.closest('.ingredient-row, .select-ingredient');
                if (!target) {
                    return;
                }

                const row = target.classList.contains('ingredient-row') ? target : target.closest('.ingredient-row');
                if (!row) {
                    return;
                }

                highlightRow(row);
                updateSelectedIngredient(row);
            });
        }

        if (searchInput && ingredientTable) {
            searchInput.addEventListener('input', function () {
                const keyword = searchInput.value.trim().toLowerCase();
                const rows = ingredientTable.querySelectorAll('.ingredient-row');

                rows.forEach(function (row) {
                    const haystack = row.getAttribute('data-search') || '';
                    const visible = haystack.indexOf(keyword) !== -1;
                    row.style.display = visible ? '' : 'none';
                });
            });
        }

        document.querySelectorAll('.stepper-btn').forEach(function (button) {
            button.addEventListener('click', function () {
                if (importCompleted) {
                    return;
                }
                const targetId = button.getAttribute('data-step-target');
                const stepValue = parseInt(button.getAttribute('data-step-value'), 10);
                const input = document.getElementById(targetId);
                if (!input || Number.isNaN(stepValue)) {
                    return;
                }

                const min = parseInt(input.getAttribute('min') || '0', 10);
                const current = parseInt(input.value || String(min), 10);
                const next = current + stepValue;

                if (next < min) {
                    input.value = min;
                } else {
                    input.value = next;
                }
            });
        });

        // Enforce positive values before submitting the form
        if (!importCompleted && quantityInput && unitPriceInput) {
            quantityInput.addEventListener('change', function () {
                if (quantityInput.value === '' || parseInt(quantityInput.value, 10) < 1) {
                    quantityInput.value = 1;
                }
            });

            unitPriceInput.addEventListener('change', function () {
                if (unitPriceInput.value === '' || parseInt(unitPriceInput.value, 10) < 1) {
                    unitPriceInput.value = 1;
                }
            });
        }

        if (!importCompleted && !selectedIngredientId.value) {
            saveButton.setAttribute('disabled', 'disabled');
        }

        if (importCompleted) {
            saveButton.setAttribute('disabled', 'disabled');
        }
    });
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
