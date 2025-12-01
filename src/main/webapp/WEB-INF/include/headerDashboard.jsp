<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>${title}</title>

        <!-- Favicons -->
        <link href="<%=request.getContextPath()%>/assets/img/favicon.png" rel="icon">
        <link href="<%=request.getContextPath()%>/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Amatic+SC:wght@400;700&display=swap" rel="stylesheet">

        <!-- Vendor CSS Files -->
        <link href="<%=request.getContextPath()%>/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/assets/vendor/aos/aos.css" rel="stylesheet">

        <!-- Main Site CSS -->
        <link href="<%=request.getContextPath()%>/assets/css/main.css" rel="stylesheet">

        <!-- Admin Listing CSS -->
        <link href="<%=request.getContextPath()%>/assets/css/dashboard0.css" rel="stylesheet">

        <c:if test="${not empty dashboard_cssjs}">
            <style>
                .bd-placeholder-img {
                    font-size: 1.125rem;
                    text-anchor: middle;
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    user-select: none;
                }

                @media (min-width: 768px) {
                    .bd-placeholder-img-lg {
                        font-size: 3.5rem;
                    }
                }
            </style>

            <link href="<%=request.getContextPath()%>/assets/css/dashboard.css" rel="stylesheet">
        </c:if>

    </head>

    <body class="admin-list-page">
        <header class="admin-header shadow-sm">
            <div class="container-fluid d-flex align-items-center justify-content-between">
                <div class="brand d-flex align-items-center gap-3">
                    <div class="brand-info"> 
                        <a href="<c:url value="dashboard"/>">
                            <h2 class="brand-name mb-0">Dashboard</h2>
                            <p class="brand-subtitle mb-0">Staff &amp; Operations</p>
                        </a>
                    </div>
                </div>
                <div class="header-actions d-flex align-items-center gap-3">
                    <c:set var="employee" value="${sessionScope.employeeSession}"/>
                    <c:if test="${employee != null}">
                        <div class="dropdown profile-chip d-flex align-items-center gap-2">
                            <a href="#" class="d-flex align-items-center gap-2 text-decoration-none dropdown-toggle"
                               id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <div class="avatar">${employee.empName.charAt(0)}</div>
                                <div>
                                    <p class="mb-0 fw-semibold text-dark">${employee.empName}</p>
                                    <span class="role">${employee.role.name}</span>
                                </div>
                            </a>

                            <ul class="dropdown-menu dropdown-menu-end shadow-sm" aria-labelledby="profileDropdown">
                                <li> <a class="dropdown-item" href="employee-profile">
                                        <i class="bi bi-person-circle me-2"></i>My Profile
                                    </a></li>
                                <li> <a class="dropdown-item text-danger" href="logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Logout
                                    </a></li>
                            </ul>
                        </div>


                    </div>
                </c:if>
            </div>
        </header>

        <main class="admin-layout">
            <div class="container-fluid">
                <div class="row">
                    <aside class="col-12 col-lg-3 col-xxl-2 left-menu" aria-label="Admin navigation">
                        <nav class="menu-panel">
                            <h1 class="menu-title">Browse Lists</h1>
                            <ul class="menu-links list-unstyled mb-0">
                                <li><a href="order"><i class="bi bi-receipt"></i> Order List</a></li>
                                    <c:if test="${sessionScope.employeeSession.role.id eq 1}">
                                    <li><a href="role"><i class="bi bi-shield-lock"></i> Role List</a></li>
                                    <li><a href="category"><i class="bi bi-tags"></i> Category List</a></li>
                                    <li><a href="type"><i class="bi bi-diagram-2"></i> Type List</a></li>
                                    <li><a href="voucher"><i class="bi bi-ticket-perforated"></i> Voucher List</a></li>
                                    <li><a href="inventory-report"><i class="bi bi-clipboard-data"></i> Inventory Report</a></li>
                                    </c:if>
                                    <c:if test="${sessionScope.employeeSession.role.id eq 1
                                                  or sessionScope.employeeSession.role.id eq 2}">
                                    <li><a href="menuitem"><i class="bi bi-list-ul"></i> Menu Item List</a></li>
                                    <li><a href="supplier"><i class="bi bi-truck"></i> Supplier List</a></li>
                                    <li><a href="employee"><i class="bi bi-person-badge"></i> Employee List</a></li>
                                    <li><a href="customer"><i class="bi bi-person-badge"></i> Customer List</a></li>
                                    </c:if>
                                    <c:if test="${sessionScope.employeeSession.role.id eq 1
                                                  or sessionScope.employeeSession.role.id eq 2
                                                  or sessionScope.employeeSession.role.id eq 3}">
                                    <li><a href="reservation"><i class="bi bi-calendar-check"></i> Reservation List</a></li>
                                    <li><a href="table"><i class="bi bi-grid-3x3"></i> Table List</a></li>
                                    </c:if>
                                    <c:if test="${sessionScope.employeeSession.role.id eq 1
                                                  or sessionScope.employeeSession.role.id eq 2
                                                  or sessionScope.employeeSession.role.id eq 4}">
                                    <li><a href="ingredient"><i class="bi bi-basket"></i> Ingredient List</a></li>
                                    <li><a href="recipe"><i class="bi bi-book"></i> Recipe List</a></li>
                                    <li><a href="import"><i class="bi bi-download"></i> Import List</a></li>
                                    </c:if>
                            </ul>

                        </nav>
                    </aside>
