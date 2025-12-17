<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:set var="title" value="Homepage - Yummy"/>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">

    <section id="hero" class="hero section light-background">

        <div class="container">
            <div class="row gy-4 justify-content-center justify-content-lg-between">
                <div class="col-lg-5 order-2 order-lg-1 d-flex flex-column justify-content-center">
                    <h1 data-aos="fade-up">Enjoy Your Healthy<br>Delicious Food</h1>
                    <p data-aos="fade-up" data-aos-delay="100">We are team of talented designers making websites with Bootstrap</p>
                    <div class="d-flex" data-aos="fade-up" data-aos-delay="200">
                        <a href="booktable" class="btn-get-started">Book a Table</a>            </div>
                </div>
                <div class="col-lg-5 order-1 order-lg-2 hero-img" data-aos="zoom-out">
                    <img src="assets/img/hero-img.png" class="img-fluid animated" alt="">
                </div>
            </div>
        </div>

    </section><section id="why-us" class="why-us section light-background">

        <div class="container">

            <div class="row gy-4">

                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="100">
                    <div class="why-box">
                        <h3>Why Choose Yummy</h3>
                        <p>
                            At Yummy Restaurant, we serve fresh ingredients, crafted by passionate chefs, in a cozy and welcoming atmosphere.
                            Your satisfaction is always our top priority.
                        </p>
                    </div>
                </div><div class="col-lg-8 d-flex align-items-stretch">
                    <div class="row gy-4" data-aos="fade-up" data-aos-delay="200">

                        <div class="col-xl-4">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-star-fill"></i>
                                <h4>Fresh & Quality Ingredients</h4>
                                <p>We select only the finest and freshest ingredients every day.</p>
                            </div>
                        </div><div class="col-xl-4" data-aos="fade-up" data-aos-delay="300">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-gem"></i>
                                <h4>Professional Chefs</h4>
                                <p>Our experienced chefs create dishes that combine flavor, creativity, and care.</p>
                            </div>
                        </div><div class="col-xl-4" data-aos="fade-up" data-aos-delay="400">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-person-circle"></i>
                                <h4>Friendly Service & Cozy Space</h4>
                                <p>Enjoy delicious meals in a warm atmosphere with attentive service.</p>
                            </div>
                        </div></div>
                </div>

            </div>

        </div>

    </section><section id="stats" class="stats section dark-background">

        <img src="assets/img/stats-bg.jpg" alt="" data-aos="fade-in">

        <div class="container position-relative" data-aos="fade-up" data-aos-delay="100">

            <div class="row gy-4">

                <div class="col-lg-4 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="${numberOfCustomer}" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Clients</p>
                    </div>
                </div><div class="col-lg-4 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="${numberOfReservation}" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Reservations</p>
                    </div>
                </div><div class="col-lg-4 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="${numberOfEmployee}" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Workers</p>
                    </div>
                </div></div>

        </div>

    </section><section id="menu" class="menu section">

        <div class="container section-title" data-aos="fade-up">
            <h2>Our Menu</h2>
            <p><span>Check Our</span> <span class="description-title">Yummy Menu</span></p>
        </div><div class="container">

            <ul class="nav nav-tabs d-flex justify-content-center" data-aos="fade-up" data-aos-delay="100">
                <c:forEach var="categoryName" items="${categoryNames}" varStatus="status">
                    <c:set var="tabId" value="menu-${fn:replace(fn:toLowerCase(categoryName), ' ', '')}" />
                    <li class="nav-item">
                        <a class="nav-link ${status.first ? 'active show' : ''}" data-bs-toggle="tab" data-bs-target="#${tabId}">
                            <h4>${categoryName}</h4>
                        </a>
                    </li>
                </c:forEach>


                <li class="nav-item">
                    <a href="menu" class="nav-link">
                        <h4>View More</h4>
                    </a>
                </li>

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
                                <div class="col-12"><p class="text-center text-muted">No items available in this category.</p></div>
                            </c:if>

                            <c:forEach var="item" items="${menuList}">
                                <div class="col-lg-4 menu-item">
                                    <%-- START: S?A ??I ?? S? D?NG MODAL (gi?ng list.jsp) --%>
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
                                    <p class="price">${item.priceVND} </p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
    <section id="chefs" class="chefs section">

        <div class="container section-title" data-aos="fade-up">
            <h2>chefs</h2>
            <p><span>Our</span> <span class="description-title">Proffesional Chefs<br></span></p>
        </div><div class="container">

            <div class="row gy-4">

                <div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="100">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/chefs-1.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>Walter White</h4>
                            <span>Master Chef</span>
                            <p>Velit aut quia 
                                fugit et et. Dolorum ea voluptate vel tempore tenetur ipsa quae aut.
                                Ipsum exercitationem iure minima enim corporis et voluptate.</p>
                        </div>
                    </div>
                </div><div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="200">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/chefs-2.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>Sarah Jhonson</h4>
                            <span>Patissier</span>
                            <p>Quo esse repellendus quia id. Est eum et accusantium pariatur fugit nihil minima suscipit corporis.
                                Voluptate sed quas reiciendis animi neque sapiente.</p>
                        </div>
                    </div>
                </div><div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="300">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/MinhNhuChef.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>Minh Nhu</h4>
                            <span>Cook</span>
                            <p>Can only cook simple things and make Trung chien</p>
                        </div>
                    </div>
                </div></div>

        </div>

    </section><section id="gallery" class="gallery section light-background">

        <div class="container section-title" data-aos="fade-up">
            <h2>Gallery</h2>
            <p><span>Check</span> <span class="description-title">Our Gallery</span></p>
        </div><div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="swiper init-swiper">
                <script type="application/json" class="swiper-config">
                    {
                    "loop": true,
                    "speed": 600,
                    "autoplay": {
                    "delay": 5000
                    },
                    "slidesPerView": "auto",
                    "centeredSlides": true,
                    "pagination": {
                    "el": ".swiper-pagination",
                    "type": "bullets",
                    "clickable": true
                    },
                    "breakpoints": {
                    "320": {
                    "slidesPerView": 1,
                    "spaceBetween": 0
                    },
                    "768": {
                    "slidesPerView": 3,
                    "spaceBetween": 20
                    },
                    "1200": {
                    "slidesPerView": 5,
                    "spaceBetween": 20
                    }
                    }
                    }
                </script>
                <div class="swiper-wrapper align-items-center">
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-1.jpg"><img src="assets/img/gallery/gallery-1.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-2.jpg"><img src="assets/img/gallery/gallery-2.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-3.jpg"><img src="assets/img/gallery/gallery-3.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-4.jpg"><img src="assets/img/gallery/gallery-4.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-5.jpg"><img src="assets/img/gallery/gallery-5.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-6.jpg"><img src="assets/img/gallery/gallery-6.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-7.jpg"><img src="assets/img/gallery/gallery-7.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-8.jpg"><img src="assets/img/gallery/gallery-8.jpg" class="img-fluid" alt=""></a></div>
                </div>
                <div class="swiper-pagination"></div>
            </div>

        </div>

    </section><section id="contact" class="contact section">

        <div class="container section-title" data-aos="fade-up">
            <h2>Contact</h2>
            <p><span>Need Help?</span> <span class="description-title">Contact Us</span></p>
        </div><div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="mb-5">
                <iframe style="width: 100%;
                        height: 400px;" src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d12097.433213460943!2d-74.0062269!3d40.7101282!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xb89d1fe6bc499443!2sDowntown+Conference+Center!5e0!3m2!1smk!2sbg!4v1539943755621" frameborder="0" allowfullscreen=""></iframe>
            </div><div class="row gy-4">

                <div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="200">
                        <i class="icon bi bi-geo-alt flex-shrink-0"></i>
                        <div>
                            <h3>Address</h3>
                            <p>Nguyen Van Truong St., Can Tho City, Vietnam</p>
                        </div>
                    </div>
                </div><div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="300">
                        <i class="icon bi bi-telephone flex-shrink-0"></i>
                        <div>
                            <h3>Call Us</h3>
                            <p>0925 XXX YYY</p>
                        </div>
                    </div>
                </div><div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="400">
                        <i class="icon bi bi-envelope flex-shrink-0"></i>
                        <div>
                            <h3>Email Us</h3>
                            <p>notARealEmail@forSure.com</p>
                        </div>
                    </div>
                </div><div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="500">
                        <i class="icon bi bi-clock flex-shrink-0"></i>
                        <div>
                            <h3>Opening Hours<br></h3>
                            <p><strong>Every Day:</strong> 5:00 AM - 12:00 AM</p>
                        </div>
                    </div>
                </div></div>
        </div>

    </section></main>
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
                                    <c:when test="${not empty item.items}">

                                        <p class="small text-muted mb-2">Total Ingredients: <strong>${fn:length(item.items)}</strong></p>
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
                                                    <c:forEach var="recipeItem" items="${item.items}">
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