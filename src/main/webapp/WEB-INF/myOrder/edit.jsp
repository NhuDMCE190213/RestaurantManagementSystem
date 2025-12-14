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

            <div class="container">
                <form method="post" action="<c:url value="myOrder">
                          <c:param name="reservationId" value="${param.reservationId}"/>
                      </c:url>">
                    <table class="table table align-middle admin-table">
                        <tr>
                            <td>
                            </td>
                            <td>
                            </td>
                        </tr>

                        <c:choose>
                            <c:when test="${not empty categoryList}">
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
                                            <div id="category_${category.categoryId}" class="collapse border border-2 rounded-3 p-2 mb-3">
                                                <div class="overflow-auto" style="max-height: 300px;">
                                                    <table class="table table-sm mb-0">
                                                        <tr>
                                                            <th width="20%">Image</th>
                                                            <th width="20%">Name</th>
                                                            <th width="15%">Price</th>
                                                            <th width="15%">Completed</th>
                                                            <th width="15%">Cooking</th>
                                                            <th width="15%">Pending</th>
                                                        </tr>

                                                        <c:forEach var="item" items="${itemsList}">
                                                            <c:set var="key" value="${item.menuItemId}_${item.price}"/>
                                                            <c:set var="orderItem" value="${orderItemsMap[key]}"/>                                                            
                                                            <c:if test="${category.categoryId eq item.category.categoryId and not empty orderItem and not empty orderItem['Pending']}">
                                                                <tr>
                                                                    <td>
                                                                        <img src="${item.imageUrl}" 
                                                                             class="menu-img img-fluid" 
                                                                             alt="${item.itemName}"
                                                                             onerror="this.onerror=null;
                                                                             var fallbackPath = '${pageContext.request.contextPath}/assets/img/menu/NIA.png';
                                                                             this.src = fallbackPath;"
                                                                             width="120px"/>    
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
                                                                        <input class="form-control" disabled
                                                                               value="${(not empty orderItem['Completed'])?orderItem['Completed']:0}">
                                                                    </td>
                                                                    <td>
                                                                        <input class="form-control" disabled 
                                                                               value="${(not empty orderItem['Cooking'])?orderItem['Cooking']:0}">
                                                                    </td>
                                                                    <td>
                                                                        <input type="hidden" name="itemIdList" value="${item.menuItemId}">
                                                                        <input class="form-control" name="quantityList" type="number" min="0" max="99" 
                                                                               value="${(not empty orderItem['Pending'])?orderItem['Pending']:0}">
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </c:forEach>
                                                    </table>
                                                </div>
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
                                <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                                <a class="btn btn-outline-dark" href="<c:url value="myOrder">
                                       <c:param name="view" value="list"/>
                                       <c:param name="reservationId" value="${param.reservationId}"/>
                                   </c:url>">Cancel</a>
                            </td>
                        </tr>
                    </table>
                </form>

            </div>
        </div>
</section>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
