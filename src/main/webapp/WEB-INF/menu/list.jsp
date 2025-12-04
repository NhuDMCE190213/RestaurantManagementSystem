<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
 <title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">

    <div class="page-title" data-aos="fade">
        <div class="container">
            <h1>Our Menu</h1>
        </div>
    </div>

    <section id="menu" class="menu section">
        <div class="container">

            <ul class="nav nav-tabs d-flex justify-content-center" data-aos="fade-up" data-aos-delay="100">
                <c:forEach var="categoryName" items="${categoryNames}" varStatus="status">
                    <c:set var="tabId" value="menu-${fn:replace(fn:toLowerCase(categoryName), ' ', '')}" />
                    <li class="nav-item">
                        <a class="nav-link ${status.first ? 'active show' : ''}" data-bs-toggle="tab" data-bs-target="#${tabId}">
                            <h4>${categoryName}</h4>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <div class="tab-content" data-aos="fade-up" data-aos-delay="200">
                <c:forEach var="categoryName" items="${categoryNames}" varStatus="status">
                    <c:set var="tabId" value="menu-${fn:replace(fn:toLowerCase(categoryName), ' ', '')}" />
                    <c:set var="listName" value="${fn:replace(fn:toLowerCase(categoryName), ' ', '').concat('List')}" />
                    
                    <div class="tab-pane fade ${status.first ? 'active show' : ''}" id="${tabId}">
                        <div class="row gy-0">
                            <c:forEach var="item" items="${requestScope[listName]}">
                                <div class="col-lg-4 menu-item">
                                    
                                  
                                    <button type="button" class="p-0 mt-5 border-0 bg-transparent"
                                        data-bs-toggle="modal" data-bs-target="#customModal-${item.menuItemId}"
                                        style="cursor: pointer; display: block;">
                                        <img 
                                            src="${item.imageUrl}" 
                                            class="menu-img img-fluid" 
                                            alt="${item.itemName}"
                                            onerror="this.onerror=null; 
                                            var fallbackPath = '${pageContext.request.contextPath}/assets/img/menu/NIA.png';
                                            this.src = fallbackPath;"
                                            />    
                                    </button>
                                            
                                    <h4>${item.itemName}</h4>
                                    <p class="ingredients">${item.description}</p>
                                    <p class="price">${item.priceVND}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
</main>
<c:forEach var="categoryName" items="${categoryNames}">
    <c:set var="listName" value="${fn:replace(fn:toLowerCase(categoryName), ' ', '').concat('List')}" />
    <c:forEach var="item" items="${requestScope[listName]}">
        <div class="modal fade" id="customModal-${item.menuItemId}" tabindex="-1" aria-labelledby="customModalLabel-${item.menuItemId}" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-xl"> 
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="customModalLabel-${item.menuItemId}">${item.itemName}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <%-- Image (Lightbox effect) --%>
                            <div class="col-md-6 mb-3 mb-md-0">
                                <h6 class="text-uppercase text-muted fw-semibold">Preview</h6>
                                <img src="${item.imageUrl}" class="img-fluid rounded shadow-sm" alt="${item.itemName}" 
                                     onerror="this.src='${pageContext.request.contextPath}/assets/img/menu/NIA.png';"
                                     style="max-height: 50vh; width: 100%; object-fit: cover;"/>
                                <p class="mt-2 text-center text-muted small">${item.description}</p>
                            </div>
                            
                            <%-- Recipe Details --%>
                            <div class="col-md-6">
                                <h6 class="text-uppercase text-muted fw-semibold">Recipe: </h6>
                                <c:choose>
                                    <c:when test="${not empty item.recipe and not empty item.recipe.items}">
                                        <p class="small text-muted mb-2">Total Ingredients: <strong>${fn:length(item.recipe.items)}</strong></p>
                                        <div class="table-responsive" style="max-height: 40vh; overflow-y: auto;">
                                            <table class="table table-striped table-sm align-middle">
                                                <thead>
                                                    <tr>
                                                        <th>Ingredient</th>
                                                        <th>Quantity</th>
                                                        <th>Unit</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="recipeItem" items="${item.recipe.items}">
                                                        <tr>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty recipeItem.ingredientName}">
                                                                        ${recipeItem.ingredientName}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-danger">Unknown (ID: ${recipeItem.ingredientId})</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:if test="${not empty recipeItem.note}">
                                                                    <br><span class="small text-muted">${recipeItem.note}</span>
                                                                </c:if>
                                                            </td>
                                                            <td>${recipeItem.quantity}</td>
                                                            <td>${recipeItem.unit}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="alert alert-warning small">No recipe ingredients found for this menu item.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</c:forEach>
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>