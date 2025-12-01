<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header id="header" class="header d-flex align-items-center sticky-top">
    <div class="container position-relative d-flex align-items-center justify-content-between">

      <!-- Logo / sitename v?n gi? ?? layout ?n ??nh -->
      <a href="homepage" class="logo d-flex align-items-center me-auto me-xl-0">
        <h1 class="sitename">Yummy</h1>
        <span>.</span>
      </a>

      <!-- Nav ?? tr?ng theo yêu c?u (không có các link nh? Dashboard/Menu/Contact...) -->
      <nav id="navmenu" class="navmenu">
        <ul>
          <!-- intentionally left empty -->
        </ul>
        <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
      </nav>

      <!-- Ph?n user dropdown v?n gi? (login/profile/logout) nh?ng ?ã b? nút "Book a Table" -->
      <div class="d-flex align-items-center gap-2">

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
                        <img src="https://ui-avatars.com/api/?name=${sessionScope.employeeSession.empName}&background=eeeeee&color=000000&size=40"
                             alt="Employee Avatar"
                             class="rounded-circle"
                             style="width: 40px; height: 40px; object-fit: cover;">
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="employeeDropdown">
                        <li>
                            <h5 class="dropdown-header text-center text-dark">
                                Hello, ${sessionScope.employeeSession.empName}!
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
        </c:choose>
      </div>

    </div>
</header>