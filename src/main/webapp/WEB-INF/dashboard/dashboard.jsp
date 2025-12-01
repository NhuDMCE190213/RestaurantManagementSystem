<%-- 
    Document   : dashboard
    Created on : 7 Nov 2025, 4:17:22 AM
    Author     : Dai Minh Nhu - CE190213
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Dashboard - Yummy"/>
<c:set var="dashboard_cssjs" value="dashboard"/>    

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Dashboard</h1>
            </div>
            <div class="btn-toolbar mb-2 mb-md-0">
<!--                <div class="btn-group me-2">
                    <button type="button" class="btn btn-sm btn-outline-secondary">Share</button>
                    <button type="button" class="btn btn-sm btn-outline-secondary">Export</button>
                </div>-->
                <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle">
                    <span data-feather="calendar"></span>
                    This week(Not update)
                </button>
            </div>
        </div>

        <canvas class="my-4 w-100" id="myChart" width="900" height="380"></canvas>

    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
