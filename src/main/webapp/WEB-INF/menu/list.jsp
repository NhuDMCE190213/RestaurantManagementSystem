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
                    <c:set var="listName" value="${fn:replace(fn:toLowerCase(categoryName), ' ', '')}List" />
                    <c:set var="menuList" value="${requestScope[listName]}" />

                    <div class="tab-pane fade ${status.first ? 'active show' : ''}" id="${tabId}">
                        <div class="tab-header text-center">
                            <p>Menu</p>
                            <h3>${categoryName}</h3>
                        </div>

                        <div class="row gy-5">
                            <c:if test="${empty menuList}">
                                <p class="text-center text-muted">No items available.</p>
                            </c:if>

                            <c:forEach var="item" items="${menuList}">
                                <div class="col-lg-4 menu-item">
                                    <a href="${pageContext.request.contextPath}/${item.imageUrl}" class="glightbox">
                                        <img 
                                            src="${pageContext.request.contextPath}/${item.imageUrl}" 
                                            class="menu-img img-fluid" 
                                            alt="${item.itemName}"
                                            onerror="this.onerror=null; 
                                            var fallbackPath = '${pageContext.request.contextPath}/assets/img/menu/NIA.png';
                                            this.src = fallbackPath;
                                            // Update the parent <a> tag's href
                                            this.closest('a').href = fallbackPath;"
                                            />
                                    </a>
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
<%@include file="/WEB-INF/include/footerCustomer.jsp" %>