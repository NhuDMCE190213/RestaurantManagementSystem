<%-- 
    Document   : edit
    Created on : 5 Nov 2025, 3:05:29 PM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Edit Order Item- Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Edit Order</h1>
            </div>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${not empty currentOrderItem}">
                    <form method="post" action="<c:url value="orderItem">
                              <c:param name="orderId" value="${param.orderId}"/>  
                              <c:param name="id" value="${currentOrderItem.orderItemId}"/>  
                          </c:url>">
                        <table class="table">
                            <tr>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="orderId" class="form-label">Order Item ID</label>
                                </th>
                                <td>
                                    <label id="orderId" class="form-control"><c:out value="${currentOrderItem.orderItemId}"/></label>
                                </td>
                            </tr>

                            <tr>
                                <th>
                                    <label for="menuItem" class="form-label">Menu Item</label>
                                </th>
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
                            </tr>

                            <tr>
                                <th>
                                    <label for="quantity" class="form-label">Quantity</label>
                                </th>
                                <td>
                                    <input type="number" name="quantity" id="quantity" class="form-control" value="${currentOrderItem.quantity}" required>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value="orderItem">
                                       <c:param name="orderId" value="${param.orderId}"/>
                                    </c:url>">Cancel</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2 class="mt-5">Not found the OrderItem which id <c:out value="${param.id}"/>. Please check the information.</h2>
                    <a class="btn btn-outline-dark" href="<c:url value="orderItem"/>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>