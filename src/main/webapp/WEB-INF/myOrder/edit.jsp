<%-- 
    Document   : add
    Created on : 22 Nov 2025, 5:24:54 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Edit Order - Yummy"/>

<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" style="padding-left: 200px" aria-label="Listing table">
    <div class="content-card shadow-sm ">
        <div class="border-0 px-4 py-3">
            <div class="card-header">
                <h1 class="section-title mb-1 text-start">Edit Order</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="myReservationOrder">
                      <c:param name="orderId" value="${param.orderId}"/>
            </c:url>">
                <table class="table table align-middle admin-table">
                    <c:choose>
                        <c:when test="${not empty currentOrder}">
                            <tr>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="reservation" class="form-label">Reservation</label>
                                </th>
                                <td>

                                    <input type="hidden" name="reservationId" value="${param.reservationId}">
                                    <input type="text" for="reservationId" 
                                           value="<c:out value="(Date: ${currentReservation.reservationDate} - Time: ${currentReservation.timeStart} ~ ${currentReservation.timeEnd} )"/>"
                                           class="form-control" disabled>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="voucher" class="form-label">Voucher</label>
                                </th>
                                <td>
                                    <select name="voucherId" class="form-select" ${(currentOrder.status ne 'Pending')?'disabled':''}>
                                        <option value="0" class="form-options">None</option>
                                        <c:forEach var="voucher" items="${vouchersList}">
                                            <option value="${voucher.voucherId}" class="form-options"
                                                    ${(currentOrder.voucher.voucherId eq voucher.voucherId)?'Selected':''}>
                                                <c:out value="${voucher.voucherCode}"/> (<c:out value="${voucher.currentDiscount}"/>)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="paymentMethod" class="form-label">Payment Method</label>
                                </th>
                                <td>
                                    <select name="paymentMethod" class="form-select" required ${(currentOrder.status ne 'Pending')?'disabled':''}>
                                        <option class="form-options" ${(currentOrder.paymentMethod eq 'Pay at Ordering')?'Selected':''}>
                                            Pay at Ordering
                                        </option>
                                        <option class="form-options" ${(currentOrder.paymentMethod eq 'Pay after Dining')?'Selected':''}>
                                            Pay after Dining
                                        </option>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <th class="form-label">Item</th>
                                <td>
                                </td>

                            </tr>

                            <c:choose>
                                <c:when test="${not empty categoryList}">
                                    <c:forEach var="category" items="${categoryList}">
                                        <tr>
                                            <td>
                                                <label class="form-label">
                                                    <c:out value="${category.categoryName}"/>
                                                </label>
                                            </td>
                                            
                                            <td class="p-0">
                                                <div class="overflow-auto" style="max-height: 300px;">
                                                    <table class="table table-sm mb-0">
                                                        <tr>
                                                            <th width="20%">Image</th>
                                                            <th width="20%">Name</th>
                                                            <th width="20%">Price</th>
                                                            <th width="20%">Quantity</th>
                                                        </tr>

                                                        <c:choose>
                                                            <c:when test="${not empty itemsList}">                               
                                                                <c:forEach var="item" items="${itemsList}">
                                                                    <c:if test="${category.categoryId eq item.category.categoryId}">
                                                                        <tr>
                                                                            <td>
                                                                                <img src="${item.imageUrl}" 
                                                                                     class="menu-img img-fluid" 
                                                                                     alt="${item.itemName}"
                                                                                     onerror="this.onerror=null;
                                                                                     var fallbackPath = '${pageContext.request.contextPath}/assets/img/menu/NIA.png';
                                                                                     this.src = fallbackPath;"
                                                                                     width="200px"/>    
                                                                            </td>
                                                                            <td>
                                                                                <label class="form-label">
                                                                                    <c:out value="${item.itemName}"/>
                                                                                </label>
                                                                            </td>
                                                                            <td>
                                                                                <label class="form-label">
                                                                                    <c:out value="${item.priceVND}"/>
                                                                                </label>
                                                                            </td>
                                                                            <td>
                                                                                <input type="hidden" name="itemIdList" value="${item.menuItemId}">
                                                                                
                                                                                <c:set var="oldQty" value="0"/>
                                                                                
                                                                                <c:forEach var="orderItem" items="${currentOrderItem}">
                                                                                    <c:if test="${(orderItem.menuItem.menuItemId eq item.menuItemId)}">
                                                                                        <c:set var="oldQty" value="${orderItem.quantity}"/>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                                
                                                                                <input class="form-control" name="quantityList" type="number" min="0" max="99" value="${oldQty}"  ${(currentOrder.status ne 'Pending')?'disabled':''}>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </c:when>

                                                            <c:otherwise>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>

                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    No data to display
                                </c:otherwise>
                            </c:choose>

                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit"  ${(currentOrder.status ne 'Pending')?'disabled':''}>Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value="myReservationOrder">
                                           <c:param name="view" value="list"/>
                                           <c:param name="reservationId" value="${param.reservationId}"/>
                                       </c:url>">Cancel</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                            <tr>
                                <td style="color: red">
                                    <h4>Not found the order.</h4>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <a class="btn btn-outline-dark" href="<c:url value="myReservationOrder">
                                           <c:param name="view" value="list"/>
                                           <c:param name="reservationId" value="${param.reservationId}"/>
                                       </c:url>">Back to list</a>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>
            </form>

        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
