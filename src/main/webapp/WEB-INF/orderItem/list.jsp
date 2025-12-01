<%-- 
    Document   : list
    Created on : 5 Nov 2025, 7:33:57 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Order Item List - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="border-0 px-4 py-3">
            <div class="card-header">
                <h1 class="section-title mb-1 text-start">Order Item list</h1>
            </div>

            <c:choose>
                <c:when test="${not empty currentOrder}">
                    <div class=" card-body row g-3 g-md-4 mb-4">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Order ID</small>
                                <p class="fs-5 fw-semibold mb-0">#<c:out value='${currentOrder.orderId}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Employee</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.emp.empName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Table</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.reservation.table.number}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Customer</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.reservation.customer.customerName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Date</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.orderDate}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Time</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.orderTime}'/></p>
                            </div>
                        </div>
                        <c:if test="${not empty currentOrder.voucher.voucherCode}">
                            <div class="col-12 col-lg-4">
                                <div class="border rounded-3 p-3 bg-light">
                                    <small class="text-uppercase text-muted fw-semibold">Voucher</small>
                                    <p class="mb-0 fw-semibold"><c:out value='${currentOrder.voucher.voucherCode}'/></p>
                                </div>
                            </div>
                        </c:if>
                        <div class="col-12 col-lg-4">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Total Price</small>
                                <p class="mb-0 fw-semibold"><c:out value='${totalPrice}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-lg-4">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Status</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentOrder.status}'/></p>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card-body px-4 pb-4">
                        <div class="alert alert-warning mb-3" role="alert">
                            The order not found. Please check information again.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="card-footer actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <c:if test="${not empty currentOrder}">
                        <form method="post" action="<c:url value="orderItem">
                                  <c:param name="orderId" value="${param.orderId}"/>
                              </c:url>">
                            <button type="submit" name="action" value="exportIngredient" class="btn btn-outline-secondary add-btn">
                                <i class="bi bi-save"></i>Export Ingredient
                            </button>
                            <button type="submit" name="action" value="exportBill" class="btn btn-outline-secondary add-btn">
                                <i class="bi bi-save"></i>Export Bill
                            </button>
                        </form>
                    </c:if>
                    <%--<c:if test="${currentOrder.status eq 'Pending'}">
                        <a class="btn btn-primary add-btn" href="<c:url value="orderItem">
                               <c:param name="view" value="add"/>
                               <c:param  name="orderId" value="${param.orderId}"/>
                           </c:url>"><i class="bi bi-plus-circle"></i>Add
                        </a>
                    </c:if>--%>
                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="5%" scope="col">Id</th>
                        <th width="30%" scope="col">Item Name</th>
                        <th width="15%" scope="col">Unit Price</th>
                        <th width="15%" scope="col">Quantity</th>
                            <c:if test="${currentOrder.status eq 'Pending'}">
                            <th width="20%"scope="col" class="text-end">Actions</th>
                            </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${orderItemsList == null || empty orderItemsList}">
                            <tr>
                                <td colspan="9" style="color:red;">No data to display</td>
                            </tr>   
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="orderItem" items="${orderItemsList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${orderItem.orderItemId}"/></td>
                                    <td><c:out value="${orderItem.menuItem.itemName}"/></td>
                                    <td><c:out value="${orderItem.priceVND}"/></td>
                                    <td><c:out value="${orderItem.quantity}"/></td>
                                    <c:if test="${currentOrder.status eq 'Pending'}">
                                        <td class="text-end">
                                            <div class="action-button-group d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-secondary btn-icon btn-edit"
                                                   title="Edit" aria-label="Edit"
                                                   href="<c:url value="orderItem">
                                                       <c:param name="view" value="edit"/>
                                                       <c:param name="orderId" value="${param.orderId}"/>
                                                       <c:param name="id" value="${orderItem.orderItemId}"/>
                                                   </c:url>#editForm">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                        title="Delete" aria-label="Delete" onclick="showDeletePopup('${orderItem.orderItemId}')">
                                                    <i class="bi bi-x-circle"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                        <a class="page-link" href="<c:url value="/orderItem">
                               <c:param name="view" value="list"/>
                               <c:param  name="orderId" value="${param.orderId}"/>
                               <c:param name="page" value="${param.page - 1}"/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value="/orderItem">
                                   <c:param name="view" value="list"/>
                                   <c:param  name="orderId" value="${param.orderId}"/>
                                   <c:param name="page" value="${i}"/>
                               </c:url>">${i}</a></li>
                        </c:forEach>
                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                        <a class="page-link" href="<c:url value="/orderItem">
                               <c:param name="view" value="list"/>
                               <c:param  name="orderId" value="${param.orderId}"/>
                               <c:param name="page" value="${(empty param.page)?2:param.page + 1}"/>
                           </c:url>" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <div class="container" id="editForm">
            <c:if test="${currentOrder.status eq 'Pending'}">
                <h1 class="mb-1 text-start" style="font-size: 25px; margin-left: 10px">Add Order Item</h1>
                <form method="post" action="<c:url value="orderItem">
                          <c:param name="orderId" value="${param.orderId}"/>
                      </c:url>">
                    <table class="table table align-middle admin-table">
                        <tr>
                            <th width="50%">
                                <label for="menuItem" class="form-label">Menu Item</label>
                            </th>
                            <th>
                                <label for="quantity" class="form-label">Quantity</label>
                            </th>
                            <th>

                            </th>
                        </tr>

                        <tr>
                            <td>
                                <select name="menuItemId" id="menuItem" class="form-select">                                
                                    <c:forEach var="item" items="${menuItemsList}">
                                        <option value="${item.menuItemId}" class="form-options">
                                            <c:out value="${item.itemName}"/>(<c:out value="${item.priceVND}"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input type="number" name="quantity" id="quantity" class="form-control" step="1" min="1" required>
                            </td>
                            <td class="text-end">
                                <button class="btn btn-outline-success" type="submit" name="action" value="add">Add</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </c:if>
        </div>

        <div class="container">
            <c:if test="${not empty currentOrderItem and currentOrder.status eq 'Pending'}">
                <%--<c:if test="${currentOrder.status eq 'Pending'}">--%>
                <h1 class="mb-1 text-start" style="font-size: 25px; margin-left: 10px">Edit Order Item</h1>
                <form method="post" action="<c:url value="orderItem">
                          <c:param name="orderId" value="${param.orderId}"/>  
                          <c:param name="id" value="${currentOrderItem.orderItemId}"/>  
                      </c:url>">
                    <table class="table">
                        <tr>
                            <th>
                                <label for="orderId" class="form-label">ID</label>
                            </th>
                            <th width="50%">
                                <label for="menuItem" class="form-label">Menu Item</label>
                            </th>
                            <th>
                                <label for="quantity" class="form-label">Quantity</label>
                            </th>
                            <th>

                            </th>
                        </tr>

                        <tr>
                            <td>
                                <label id="orderId" class="form-control"><c:out value="${currentOrderItem.orderItemId}"/></label>
                            </td>
                            <td>
                                <select name="menuItemId" id="menuItem" class="form-select">                                
                                    <c:forEach var="item" items="${menuItemsList}">
                                        <option value="${item.menuItemId}" class="form-options" 
                                                ${currentOrderItem.menuItem.menuItemId eq item.menuItemId ? 'Selected' : '' }>
                                            <c:out value="${item.itemName}"/>(<c:out value="${item.priceVND}"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input type="number" name="quantity" id="quantity" class="form-control" value="${currentOrderItem.quantity}" step="1" min="1" required>
                            </td>
                            <td class="text-end">
                                <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </c:if>
        </div>
    </div>
</section>

<div class="modal" id="deletePopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger">
                <h6 id="idForDeletePopup">Are you sure you want to delete this object?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value="orderItem">
                      <c:param  name="orderId" value="${param.orderId}"/>
                  </c:url>">
                <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>

