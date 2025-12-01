<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Inventory usage report">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Inventory Usage Report</h1>
            </div>
            <div class="d-flex flex-column flex-sm-row gap-2 align-items-stretch align-items-sm-center">
                <form method="get" action="<c:url value='inventory-report'/>" class="d-flex flex-column flex-sm-row gap-2 align-items-stretch align-items-sm-center">
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-calendar-event"></i></span>
                        <input type="date" class="form-control" name="date" value="${reportDate}" aria-label="Report date selector">
                    </div>
                    <button type="submit" class="btn btn-outline-primary"><i class="bi bi-arrow-repeat"></i> View</button>
                </form>
                <c:if test="${hasData}">
                    <form method="post" action="<c:url value='inventory-report'/>" class="d-inline-flex">
                        <input type="hidden" name="action" value="confirm" />
                        <input type="hidden" name="date" value="${reportDate}" />
                        <button type="submit" class="btn btn-primary" ${alreadyProcessed ? 'disabled' : ''}>
                            <i class="bi bi-check2-circle"></i>
                            <c:choose>
                                <c:when test="${alreadyProcessed}">Report confirmed</c:when>
                                <c:otherwise>Confirm Report</c:otherwise>
                            </c:choose>
                        </button>
                    </form>
                </c:if>
            </div>
        </div>

        <div class="card-body px-4 pb-4">
            <c:if test="${alreadyProcessed}">
                <div class="alert alert-info d-flex align-items-center gap-2" role="alert">
                    <i class="bi bi-info-circle"></i>
                    <span>Report for <strong>${reportDate}</strong> has already been finalized<c:if test="${not empty processedByName}"> by <strong>${processedByName}</strong></c:if><c:if test="${not empty processedAt}"> at <strong>${processedAt}</strong></c:if>.</span>
                </div>
            </c:if>

            <div class="row g-3 g-md-4 mb-4">
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Completed orders</small>
                        <p class="fs-4 fw-semibold mb-0">${summary.completedOrders}</p>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Menu items prepared</small>
                        <p class="fs-4 fw-semibold mb-0">${summary.itemsPrepared}</p>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Total ingredient usage</small>
                        <p class="fs-4 fw-semibold mb-0">
                            <fmt:formatNumber value="${totalUsage}" maxFractionDigits="2" minFractionDigits="0"/>
                        </p>
                    </div>
                </div>
            </div>

            <c:choose>
                <c:when test="${hasData}">
                    <div class="table-responsive border rounded-3">
                        <table class="table align-middle mb-0" id="usageTable">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Ingredient</th>
                                    <th scope="col">Unit</th>
                                    <th scope="col" class="text-end">Stock before</th>
                                    <th scope="col" class="text-end">Used</th>
                                    <th scope="col" class="text-end">Remaining</th>
                                    <th scope="col">Note</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${usageList}" varStatus="loop">
                                    <tr
                                        data-ingredient-id="${item.ingredientId}"
                                        data-ingredient-name="${item.ingredientName}"
                                        data-unit="${item.unit}"
                                        data-stock-before="${item.stockBefore}"
                                        data-stock-after="${item.stockAfter}"
                                        data-quantity-used="${item.quantityUsed}"
                                        data-note="${fn:escapeXml(item.note)}"
                                    >
                                        <td class="usage-index">${loop.index + 1}</td>
                                        <td class="usage-name">${item.ingredientName}</td>
                                        <td class="usage-unit">${item.unit}</td>
                                        <td class="text-end usage-stock-before"><fmt:formatNumber value="${item.stockBefore}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td class="text-end text-danger fw-semibold usage-quantity"><fmt:formatNumber value="${item.quantityUsed}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td class="text-end usage-stock-after"><fmt:formatNumber value="${item.stockAfter}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td class="usage-note">${empty item.note ? '-' : fn:escapeXml(item.note)}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning" role="alert">
                        No completed orders found for <strong>${reportDate}</strong>. Stock remains unchanged.
                    </div>
                </c:otherwise>
            </c:choose>

            <c:if test="${not empty ingredientList}">
                <div class="mt-4 p-4 border rounded-3 bg-light-subtle">
                    <div class="d-flex flex-column flex-lg-row justify-content-between gap-3 mb-3">
                        <div>
                            <h2 class="h5 mb-1">Manual Ingredient Deduction</h2>
                        </div>
                    </div>
                    <form method="post" action="<c:url value='inventory-report'/>" class="row gy-3">
                        <input type="hidden" name="action" value="manual" />
                        <input type="hidden" name="date" value="${reportDate}" />
                        <div class="col-12 col-md-4">
                            <label class="form-label" for="manualIngredient">Ingredient</label>
                            <select id="manualIngredient" name="ingredientId" class="form-select">
                                <c:forEach var="ingredient" items="${ingredientList}">
                                    <option
                                        value="${ingredient.ingredientId}"
                                        data-unit="${ingredient.unit}"
                                        data-expiration="${ingredient.expirationDate}"
                                        data-quantity="${ingredient.totalQuantity}"
                                        data-expired="${ingredient.expired}"
                                        data-expiring-soon="${ingredient.expiringSoon}"
                                    >
                                        ${ingredient.ingredientName} (${ingredient.unit})
                                        - stock: <fmt:formatNumber value="${ingredient.totalQuantity}" maxFractionDigits="2" minFractionDigits="0"/>
                                        <c:choose>
                                            <c:when test="${not empty ingredient.expirationDate}">
                                                - Exp: ${ingredient.expirationDate}
                                            </c:when>
                                            <c:otherwise>
                                                - Exp: Not set
                                            </c:otherwise>
                                        </c:choose>
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="mt-2">
                                <div id="ingredientMetaPanel" class="border rounded-3 p-2 bg-white">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <small class="text-muted">Status</small>
                                        <span id="ingredientMetaStatus" class="badge text-bg-secondary">Not selected</span>
                                    </div>
                                    <div class="mb-1">
                                        <small class="text-muted">Expiration</small>
                                        <div id="ingredientMetaExpiration" class="fw-semibold">--</div>
                                    </div>
                                    <div>
                                        <small class="text-muted">Available stock</small>
                                        <div id="ingredientMetaQuantity" class="fw-semibold">--</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 col-md-4">
                            <label class="form-label" for="remainingQuantity">Ending quantity (after service)</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-clipboard-data"></i></span>
                                <input type="number" step="0.01" min="0" class="form-control" id="remainingQuantity" name="remainingQuantity" placeholder="e.g., 5" required />
                            </div>
                        </div>
                        <div class="col-12">
                            <label class="form-label" for="manualNote">Note (optional)</label>
                            <textarea id="manualNote" name="note" class="form-control" rows="2" maxlength="255" placeholder="e.g., Discarded 5kg due to expiration."></textarea>
                        </div>
                        <div class="col-12 col-md-4 d-flex align-items-end justify-content-end ms-auto">
                            <button type="submit" class="btn btn-outline-primary"><i class="bi bi-save"></i> Save Adjustment</button>
                        </div>
                    </form>
                </div>
            </c:if>

        </div>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const ingredientSelect = document.getElementById('manualIngredient');
        const metaPanel = document.getElementById('ingredientMetaPanel');
        const metaStatus = document.getElementById('ingredientMetaStatus');
        const metaExpiration = document.getElementById('ingredientMetaExpiration');
        const metaQuantity = document.getElementById('ingredientMetaQuantity');

        if (ingredientSelect && metaPanel && metaStatus && metaExpiration && metaQuantity) {
            const quantityFormatter = new Intl.NumberFormat('en-US', {
                minimumFractionDigits: 0,
                maximumFractionDigits: 2
            });

            const updateMetaPanel = () => {
                const option = ingredientSelect.selectedOptions.length ? ingredientSelect.selectedOptions[0] : null;
                if (!option) {
                    metaStatus.textContent = 'Not selected';
                    metaStatus.className = 'badge text-bg-secondary';
                    metaExpiration.textContent = '--';
                    metaQuantity.textContent = '--';
                    return;
                }

                const expiration = option.getAttribute('data-expiration');
                const quantityRaw = option.getAttribute('data-quantity');
                const unit = option.getAttribute('data-unit') || '';
                const expired = option.getAttribute('data-expired') === 'true';
                const expiringSoon = option.getAttribute('data-expiring-soon') === 'true';

                if (expiration && expiration.trim() !== '') {
                    metaExpiration.textContent = expiration;
                } else {
                    metaExpiration.textContent = 'Not set';
                }

                const parsedQuantity = parseFloat(quantityRaw);
                if (!Number.isNaN(parsedQuantity)) {
                    metaQuantity.textContent = `${quantityFormatter.format(parsedQuantity)} ${unit}`.trim();
                } else {
                    metaQuantity.textContent = '--';
                }

                if (expired) {
                    metaStatus.textContent = 'EXPIRED';
                    metaStatus.className = 'badge text-bg-danger';
                } else if (expiringSoon) {
                    metaStatus.textContent = 'Expiring soon';
                    metaStatus.className = 'badge text-bg-warning';
                } else {
                    metaStatus.textContent = 'Available';
                    metaStatus.className = 'badge text-bg-success';
                }
            };

            ingredientSelect.addEventListener('change', updateMetaPanel);
            updateMetaPanel();
        }

        const usageTable = document.getElementById('usageTable');
        if (!usageTable) {
            return;
        }

        const tbody = usageTable.querySelector('tbody');
        if (!tbody) {
            return;
        }

        const rows = Array.from(tbody.querySelectorAll('tr[data-ingredient-id]'));
        if (rows.length === 0) {
            return;
        }

        const groupedOrder = [];
        const groupedMap = new Map();

        const parseNumber = (value) => {
            const parsed = parseFloat(value);
            return Number.isNaN(parsed) ? 0 : parsed;
        };

        const getCellText = (row, selector) => {
            const cell = row.querySelector(selector);
            return cell ? cell.textContent.trim() : '';
        };

        rows.forEach((row) => {
            const id = row.getAttribute('data-ingredient-id');
            if (!id) {
                return;
            }

            const nameFromAttr = row.getAttribute('data-ingredient-name');
            const unitFromAttr = row.getAttribute('data-unit');

            const existing = groupedMap.get(id);
            const snapshot = {
                ingredientId: id,
                name: nameFromAttr || getCellText(row, '.usage-name'),
                unit: unitFromAttr || getCellText(row, '.usage-unit'),
                quantityUsed: parseNumber(row.getAttribute('data-quantity-used')),
                stockBefore: parseNumber(row.getAttribute('data-stock-before')),
                stockAfter: parseNumber(row.getAttribute('data-stock-after')),
                notes: []
            };
            const noteAttr = row.getAttribute('data-note');
            if (noteAttr) {
                snapshot.notes.push(noteAttr);
            }

            if (!existing) {
                groupedMap.set(id, snapshot);
                groupedOrder.push(id);
            } else {
                existing.quantityUsed += snapshot.quantityUsed;
                existing.stockAfter = snapshot.stockAfter;
                if (snapshot.stockBefore > existing.stockBefore) {
                    existing.stockBefore = snapshot.stockBefore;
                }
                snapshot.notes.forEach((note) => {
                    if (!existing.notes.includes(note)) {
                        existing.notes.push(note);
                    }
                });
            }
        });

        if (groupedOrder.length === rows.length) {
            return; // nothing to merge
        }

        const formatter = new Intl.NumberFormat('en-US', {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        });

        tbody.innerHTML = '';

        const appendCell = (row, text, className) => {
            const cell = document.createElement('td');
            if (className) {
                cell.className = className;
            }
            cell.textContent = text;
            row.appendChild(cell);
        };

        groupedOrder.forEach((id, index) => {
            const item = groupedMap.get(id);
            if (!item) {
                return;
            }

            const tr = document.createElement('tr');
            tr.setAttribute('data-ingredient-id', id);

            appendCell(tr, String(index + 1), 'usage-index');
            appendCell(tr, item.name || '-', 'usage-name');
            appendCell(tr, item.unit || '-', 'usage-unit');
            appendCell(tr, formatter.format(item.stockBefore), 'text-end usage-stock-before');
            appendCell(tr, formatter.format(item.quantityUsed), 'text-end text-danger fw-semibold usage-quantity');
            appendCell(tr, formatter.format(item.stockAfter), 'text-end usage-stock-after');
            const noteText = item.notes && item.notes.length ? item.notes.join('; ') : '-';
            appendCell(tr, noteText, 'usage-note');

            tbody.appendChild(tr);
        });
    });
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
