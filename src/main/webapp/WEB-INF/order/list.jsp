<%-- 
    Document   : list
    Created on : 2 Nov 2025, 9:05:59 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Order List - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm ">
        <div class="border-0 px-4 py-3">
            <div class="card-header">
                <h1 class="section-title mb-1 text-start">My Order</h1>
            </div>

            <c:choose>
                <c:when test="${not empty currentReservation}">
                    <div class=" card-body row g-3 g-md-4 mb-4">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Table</small>
                                <p class="fs-5 fw-semibold mb-0"><c:out value='${currentReservation.table.number}'/></p>
                            </div>
                        </div>
                        <c:if test="${not empty currentReservation.emp}">
                            <div class="col-12 col-sm-6 col-xl-3">
                                <div class="border rounded-3 p-3 bg-light">
                                    <small class="text-uppercase text-muted fw-semibold">Employee</small>
                                    <p class="fs-5 fw-semibold mb-0"><c:out value='${currentReservation.emp.empName}'/></p>
                                </div>
                            </div>
                        </c:if>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Date</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.reservationDate}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Time Start</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.timeStart}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Time End</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.timeEnd}'/></p>
                            </div>
                        </div>
                        <%--cho voucher--%>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Status</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentReservation.status}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Sub Total</small>
                                <p class="mb-0 fw-semibold"><c:out value='${subTotal}'/></p>
                            </div>
                        </div>
                        <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                            <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                                <button class="btn btn-outline-primary"
                                        data-bs-toggle="modal"
                                        data-bs-target="#billModal">
                                    View Bill
                                </button>
                                <a class="btn btn-primary add-btn" href="<c:url value="order">
                                       <c:param name="view" value="add"/>
                                       <c:param name="reservationId" value="${currentReservation.reservationId}"/>
                                   </c:url>"><i class="bi bi-plus-circle"></i>Add</a>
                                <a class="btn btn-warning add-btn" href="<c:url value="order">
                                       <c:param name="view" value="edit"/>
                                       <c:param name="reservationId" value="${currentReservation.reservationId}"/>
                                   </c:url>"><i class="bi bi-pencil-fill"></i>Edit</a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card-body px-4 pb-4">
                            <div class="alert alert-warning mb-3" role="alert">
                                The reservation not found. Please check information again.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <table class="table table align-middle admin-table">
                <tr>
                    <td>
                    </td>
                    <td>
                    </td>
                </tr>

                <c:choose>
                    <c:when test="${not empty orderItemsList}">
                        <c:forEach var="category" items="${categoryList}">
                            <tr>
                                <td colspan="2">
                                    <button class="btn btn-outline-danger w-100 text-start"
                                            type="button"
                                            data-bs-toggle="collapse"
                                            data-bs-target="#category_${category.categoryId}">
                                        <i class="bi bi-caret-down-fill me-2"></i>
                                        <c:out value="${category.categoryName}"/>
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="p-0">
                                    <div id="category_${category.categoryId}" class="collapse show border border-2 rounded-3 p-2 mb-3">
                                        <div class="overflow-auto" style="max-height: 300px;">
                                            <table class="table table-sm mb-0">
                                                <thead>
                                                    <tr>
                                                        <th width="20%">Image</th>
                                                        <th width="20%">Name</th>
                                                        <th width="15%">Price</th>
                                                        <th width="15%">Completed</th>
                                                        <th width="15%">Cooking</th>
                                                        <th width="15%">Pending</th>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <c:forEach var="item" items="${orderItemForMapList}">
                                                        <c:set var="key" value="${item.menuItem.menuItemId}_${item.unitPrice}"/>
                                                        <c:set var="orderItem" value="${orderItemsMap[key]}"/>
                                                        <c:set var="orderItemId" value="${orderItemsIdMap[key]}"/>
                                                        <c:if test="${category.categoryId eq item.menuItem.category.categoryId and not empty orderItem}">
                                                            <tr id="<c:out value="${item.orderItemId}"/>">
                                                                <td>
                                                                    <img src="${item.menuItem.imageUrl}"
                                                                         class="menu-img img-fluid"
                                                                         alt="${item.menuItem.itemName}"
                                                                         onerror="this.onerror=null;
                                                                         this.src='${pageContext.request.contextPath}/assets/img/menu/NIA.png';"
                                                                         width="120"/>
                                                                </td>

                                                                <td>
                                                                    <c:out value="${item.menuItem.itemName}"/>
                                                                </td>

                                                                <td>
                                                                    <c:out value="${item.priceVND}"/>
                                                                </td>
                                                                <td>
                                                                    <input class="form-control" disabled 
                                                                           value="${(not empty orderItem['Completed'])?orderItem['Completed']:0}">
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${(not empty orderItem['Cooking']) and orderItem['Cooking'] > 0}">
                                                                            <button class="form-control btn btn-outline-success"
                                                                                    title="Complete" aria-label="Complete"
                                                                                    onclick="showPopupComplete(<c:out value="${orderItemId['Cooking']}"/>, '<c:out value="${item.menuItem.itemName}"/>')"
                                                                                    name="action" value="complete">
                                                                                <c:out value="${(not empty orderItem['Cooking'])?orderItem['Cooking']:0}"/>
                                                                            </button>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input class="form-control" disabled 
                                                                                   value="${(not empty orderItem['Cooking'])?orderItem['Cooking']:0}">
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${(not empty orderItem['Pending']) and orderItem['Pending'] > 0}">
                                                                            <button class="form-control btn btn-outline-success"
                                                                                    title="Cook" aria-label="Cook"
                                                                                    onclick="showPopupCook(<c:out value="${orderItemId['Pending']}"/>, '<c:out value="${item.menuItem.itemName}"/>')"
                                                                                    name="action" value="cook">
                                                                                <c:out value="${(not empty orderItem['Pending'])?orderItem['Pending']:0}"/>
                                                                            </button>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input class="form-control" disabled 
                                                                                   value="${(not empty orderItem['Pending'])?orderItem['Pending']:0}">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </td>
                            </tr>

                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="2" class="text-center text-muted">
                                No data to display
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>

            </table>
        </div>
    </div>
</section>

<div class="modal" id="actionPopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="titlePopup">Title Popup</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger" id="formatTextPopup">
                <h6 id="makeSureQuesttionPopup">Make Sure Questtion Popup?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value="order">
                      <c:param name="reservationId" value="${param.reservationId}"/>
                  </c:url>">
                <input type="hidden" id="orderItemIdHidden" name="orderItemId" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" id="actionHidden" value="" class="btn btn-success">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="billModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <div class="modal-header">
                <h5 class="modal-title">
                    Bill
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
                <p><b>Status:</b> ${currentReservation.status}</p>

                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Qty</th>
                            <th>Price</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${orderItemsList}">
                            <tr>
                                <td>${item.menuItem.itemName}</td>
                                <td>${item.quantity}</td>
                                <td>${item.priceVND}</td>
                                <td>${item.totalPriceVND}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <h5 class="text-end">
                    <table class="table table-bordered">
                        <tbody>
                            <tr>
                                <td>Subtotal</td>
                                <td class="text-end">${subTotal}</td>
                            </tr>

                            <tr>
                                <td>VAT (10%)</td>
                                <td class="text-end">${vat}</td>
                            </tr>

                            <tr>
                                <td>Voucher</td>
                                <td class="text-end text-danger">
                                    -${order.voucherAmount}
                                </td>
                            </tr>

                            <tr>
                                <td>Deposit</td>
                                <td class="text-end text-danger">
                                    -${order.depositAmount}
                                </td>
                            </tr>

                            <tr class="table-active fw-bold">
                                <td>Total to pay</td>
                                <td class="text-end text-success">
                                    
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </h5>

            </div>

            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>

<script>
    function showPopupComplete(id, menuItemName) {
        document.getElementById("orderItemIdHidden").value = id;
        document.getElementById("titlePopup").textContent = "Complete Action";
        document.getElementById("makeSureQuesttionPopup").textContent = "Do you want to complete this cooking " + menuItemName + "?";
        document.getElementById("actionHidden").value = "complete";


        const formatText = document.getElementById("formatTextPopup");
        formatText.classList.remove("text-danger", "text-warning", "text-success");
        formatText.classList.add("text-success");

        var myModal = new bootstrap.Modal(document.getElementById('actionPopup'));
        myModal.show();
    }

    function showPopupCook(id, menuItemName) {
        document.getElementById("orderItemIdHidden").value = id;
        document.getElementById("titlePopup").textContent = "Cook Action";
        document.getElementById("makeSureQuesttionPopup").textContent = "Do you want to cook this pending " + menuItemName + "?";
        document.getElementById("actionHidden").value = "cook";

        const formatText = document.getElementById("formatTextPopup");
        formatText.classList.remove("text-danger", "text-warning", "text-success");
        formatText.classList.add("text-warning");

        var myModal = new bootstrap.Modal(document.getElementById('actionPopup'));
        myModal.show();
    }
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
