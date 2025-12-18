<%-- 
    Document   : dashboard
    Created on : 7 Nov 2025, 4:17:22 AM
    Author     : Dai Minh Nhu - CE190213
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Dashboard - Yummy"/>
<c:set var="dashboard_cssjs" value="dashboard"/>    

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <ul class="nav nav-pills gap-2 mb-4">
        <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2 px-3 py-2 text-danger"
               href="#incomeChart">
                <i class="bi bi-graph-up"></i>
                <span>Overview</span>
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2 px-3 py-2 text-danger"
               href="#tableChart">
                <i class="bi bi-table"></i>
                <span>Most-Used Tables</span>
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2 px-3 py-2 text-danger"
               href="#statusChart">
                <i class="bi bi-pie-chart"></i>
                <span>Reservation Status</span>
            </a>
        </li>
    </ul>

    <form id="filterForm" action="dashboard" method="get"
          class="row g-3 align-items-end mb-4">

        <div class="col-md-4">
            <label for="startDate" class="form-label fw-semibold">Start date</label>
            <input type="date" class="form-control"
                   id="startDate" name="startDate"
                   value="${param.startDate}">
        </div>

        <div class="col-md-4">
            <label for="endDate" class="form-label fw-semibold">End date</label>
            <input type="date" class="form-control"
                   id="endDate" name="endDate"
                   value="${param.endDate}">
        </div>

        <div class="col-md-4 d-flex gap-2">
            <button type="submit" class="btn btn-primary px-4">
                <i class="bi bi-funnel-fill me-1"></i> Apply
            </button>

            <button type="button"
                    class="btn btn-outline-secondary"
                    onclick="resetFilter()">
                <i class="bi bi-arrow-counterclockwise me-1"></i> Reset
            </button>
        </div>
    </form>

    <script>
        function resetFilter() {
            const form = document.getElementById("filterForm");

            form.startDate.value = "";
            form.endDate.value = "";

            form.submit();
        }
    </script>




    <div class="container-fluid mt-4">
        <h3 class="mb-4">Manage Statistic Dashboard</h3>

        <div class="row">
            <div class="col-md-4">
                <div class="card text-bg-primary mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Total Reservations</h5>
                        <h2>${totalReservations}</h2>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-bg-success mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Total Income</h5>
                        <h2>${totalIncome}</h2>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-bg-warning mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Active Tables</h5>
                        <h2>${activeTables}</h2>
                    </div>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header bg-danger text-white">
                Income Statistics (Monthly)
            </div>
            <div class="card-body">
                <canvas id="incomeChart"></canvas>
            </div>
            <script>
                const incomeCtx = document.getElementById('incomeChart');

                new Chart(incomeCtx, {
                    type: 'line',
                    data: {
                        labels: ${monthLabels}, // ["Jan","Feb","Mar"]
                        datasets: [{
                                label: 'Income (VND)',
                                data: ${monthlyIncome}, // [12000000,15000000,18000000]
                                tension: 0.4,
                                fill: true
                            }]
                    }
                });
            </script>
        </div>

        <div class="card mb-4">
            <div class="card-header bg-danger text-white">
                Most-Used Dining Tables
            </div>
            <div class="card-body">
                <canvas id="tableChart"></canvas>
            </div>
            <script>
                const tableCtx = document.getElementById('tableChart');

                new Chart(tableCtx, {
                    type: 'bar',
                    data: {
                        labels: ${tableNames}, // ["Table 1","Table 2","Table 3"]
                        datasets: [{
                                label: 'Number of Reservations',
                                data: ${tableUsage}, // [20, 35, 15]
                            }]
                    }
                });
            </script>
        </div>

        <div class="card mb-4">
            <div class="card-header bg-danger text-white">
                Reservation Status
            </div>
            <div class="card-body" style="width:500px; height:500px; margin:0 auto;">
                <canvas id="statusChart"></canvas>
            </div>
        </div>

        <script>
            new Chart(document.getElementById('statusChart'), {
                type: 'pie',
                data: {
                    labels: [
                        'Waiting Deposit',
                        'Serving',
                        'Approved',
                        'Unpaid',
                        'Completed',
                        'Cancelled Before Deposit',
                        'Cancelled After Deposit',
                        'No Show',
                        'Rejected'
                    ],
                    datasets: [{
                            data: [
            ${waitingDepositCount},
            ${servingCount},
            ${approvedCount},
            ${unpaidCount},
            ${completedCount},
            ${cancelledBeforeDepositCount},
            ${cancelledAfterDepositCount},
            ${noShowCount},
            ${rejectedCount}
                            ],
                            backgroundColor: [
                                '#ffc107', // Waiting_deposit
                                '#0dcaf0', // Reserving
                                '#0d6efd', // Approved
                                '#dc3545', // Unpaid
                                '#198754', // Completed
                                '#6c757d', // Cancelled_before_deposit
                                '#b02a37', // Cancelled_after_deposit
                                '#212529', // No_show
                                '#adb5bd'   // Rejected
                            ]
                        }]
                },
                options: {
                    plugins: {
                        legend: {
                            position: 'right'
                        }
                    }
                }
            });
        </script>

    </div>

</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
