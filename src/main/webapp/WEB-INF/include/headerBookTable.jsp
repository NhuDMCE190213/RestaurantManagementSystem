<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <title>Book a Table - Yummy Restaurant</title>
        <meta name="description" content="Reserve your seat and explore available tables at Yummy Restaurant.">
        <meta name="keywords" content="restaurant reservation, book table, dining">

        <!-- Favicons -->
        <link href="assets/img/favicon.png" rel="icon">
        <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&family=Inter:wght@400;600;700&family=Amatic+SC:wght@700&display=swap" rel="stylesheet">

        <!-- Vendor CSS -->
        <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="assets/vendor/aos/aos.css" rel="stylesheet">
        <link href="assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
        <link href="assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

        <!-- Main CSS -->
        <link href="assets/css/main.css" rel="stylesheet">
        <link href="assets/css/booktable.css" rel="stylesheet">
    </head>

    <body class="index-page">

        <header id="header" class="header d-flex align-items-center sticky-top">
            <div class="container position-relative d-flex align-items-center justify-content-between">

                <a href="homepage" class="logo d-flex align-items-center me-auto me-xl-0">
                    <!-- Uncomment the line below if you also wish to use an image logo -->
                    <!-- <img src="assets/img/logo.png" alt=""> -->
                    <h1 class="sitename">Yummy</h1>
                    <span>.</span>
                </a>

                <nav id="navmenu" class="navmenu">
                    <ul>
                        <li><a href="<c:url value="homepage"/>#hero">Home<br></a></li>
                        <li><a href="<c:url value="homepage"/>#menu">Menu</a></li>
                        <li><a href="<c:url value="homepage"/>#chefs">Chefs</a></li>
                        <li><a href="<c:url value="homepage"/>#gallery">Gallery</a></li>
                        <li><a href="<c:url value="homepage"/>#contact">Contact</a></li>
                        <li>
                            <a href="<c:url value='/my-reservation'>
                                   <c:param name='customerId' value='${sessionScope.customerSession.customerId}'/>
                               </c:url>">
                                My Reservation
                            </a>
                        </li>

                        <li><a href="<c:url value="myOrder"/>">My Order</a></li>
                    </ul>
                    <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
                </nav>
                <div class="d-flex align-items-center gap-2">


                    <a class="btn-getstarted" href="booktable">Book a Table</a>

                    <c:choose>

                        <c:when test="${empty sessionScope.customerSession and empty sessionScope.employeeSession}">
                            <a class="btn btn-outline-danger text-danger round" href="login">Login</a>
                        </c:when>


                        <c:when test="${not empty sessionScope.customerSession}">
                            <div class="dropdown">
                                <a class="btn btn-link dropdown-toggle p-0" href="#" role="button"
                                   id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                    <img src="https://ui-avatars.com/api/?name=${sessionScope.customerSession.customerName}&background=f5f5f5&color=000000&size=40"
                                         alt="User Avatar"
                                         class="rounded-circle"
                                         style="width: 40px; height: 40px; object-fit: cover;">
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                    <li>
                                        <h5 class="dropdown-header text-center text-dark">
                                            Hello, ${sessionScope.customerSession.customerName}!
                                        </h5>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item" href="customer-profile">
                                            <i class="bi bi-person-circle me-2"></i>My Profile
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item text-danger" href="logout">
                                            <i class="bi bi-box-arrow-right me-2"></i>Logout
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </c:when>


                        <c:when test="${not empty sessionScope.employeeSession}">
                            <div class="dropdown">
                                <a class="btn btn-link dropdown-toggle p-0" href="#" role="button"
                                   id="employeeDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                    <img src="https://ui-avatars.com/api/?name=$<c:out value="${sessionScope.employeeSession.empName}"/>&background=eeeeee&color=000000&size=40"
                                         alt="Employee Avatar"
                                         class="rounded-circle"
                                         style="width: 40px; height: 40px; object-fit: cover;">
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="employeeDropdown">
                                    <li>
                                        <h5 class="dropdown-header text-center text-dark">
                                            Hello, <c:out value="${sessionScope.employeeSession.empName}"/>!
                                        </h5>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item" href="dashboard">
                                            <i class="bi bi-speedometer2 me-2"></i>Dashboard
                                        </a>
                                    </li>

                                    <li>
                                        <a class="dropdown-item" href="employee-profile">
                                            <i class="bi bi-speedometer2 me-2"></i>My Profile
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item text-danger" href="logout">
                                            <i class="bi bi-box-arrow-right me-2"></i>Logout
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </header>
