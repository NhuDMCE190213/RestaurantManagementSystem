<%-- 
    Document   : add
    Created on : 5 Nov 2025, 11:11:28 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Add Order Item - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Add Order Item</h1>
            </div>
        </div>

        <div class="container">
            <form method="post" action="<c:url value="orderItem">
                  <c:param name="orderId" value="${param.orderId}"/>
            </c:url>">
                <table class="table table align-middle admin-table">
                    <tr>
                        <td>
                        </td>
                        <td>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="menuItem" class="form-label">Menu Item</label>
                        </th>
                        <td>
                            <select name="menuItemId" id="menuItem" class="form-select">                                
                                <c:forEach var="item" items="${menuItemsList}">
                                    <option value="${item.menuItemId}" class="form-options">
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
                            <input type="number" name="quantity" id="quantity" class="form-control" required>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="add">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value="orderItem">
                                   <c:param name="orderId" value="${param.orderId}"/>
                            </c:url>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
