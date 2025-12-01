<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Admin Restaurant Dashboard</title>

        <!--style-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
      <link href="${pageContext.request.contextPath}/assets/css/uistyles.css" rel="stylesheet" />
        <link rel="stylesheet"  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
  
    </head>

    <body>
        <div class="d-flex dashboard-container">
            <!-- Sidebar -->
      <%@include file="/WEB-INF/include/sidebar.jsp" %>


            <!-- Main Content -->
            <main class="main-content">
                <div class="container-fluid">
                    <!-- Header -->
                    <div
                        class="d-flex flex-wrap justify-content-between align-items-center mb-4 border-bottom pb-3"
                        >
                        <h1 class="h3 fw-bold text-success mb-2">Your Orders</h1>
                        <div class="d-flex flex-wrap align-items-center gap-3">
                            <div class="d-flex align-items-center">
                                <label
                                    for="date-sort"
                                    class="me-2 text-secondary fw-medium  d-flex align-items-center"
                                    >
                                    <i class="fas fa-calendar-alt me-1 text-success"></i>Date:
                                </label>
                                <input
                                    type="date"
                                    id="date-sort"
                                    class="form-control form-control-sm"
                                    />
                            </div>
                            <div class="d-flex align-items-center">
                                <label
                                    for="sort-by"
                                    class="me-2 text-secondary fw-medium d-flex align-items-center"
                                    >
                                    <i class="fas fa-sort-amount-down-alt me-1 text-success"></i
                                    >Sort:
                                </label>
                                <select id="sort-by" class="form-select form-select-sm">
                                    <option>Newest</option>
                                    <option>Oldest</option>
                                    <option>Highest Total</option>
                                    <option>Pending Status</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Searchbar -->
                    <div class="input-group mb-4 shadow-sm">
                        <span class="input-group-text bg-success text-white border-success">
                            <i class="fas fa-search"></i>
                        </span>
                        <input
                            type="text"
                            class="form-control border-success"
                            placeholder="Search..."
                            />
                    </div>

                    <!-- Table of orders -->
                    <div class="card shadow-sm border-0">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-success text-secondary">
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Order Date</th>
                                            <th>Customer Name</th>
                                            <th>Total Amount</th>
                                            <th>Order Status</th>
                                            <th>Payment Status</th>
                                            <th>Shipping Address</th>
                                            <th>Message</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#001</td>
                                            <td>2025-10-07</td>
                                            <td>Jack J97</td>
                                            <td class="text-success fw-semibold">5.000.000 VND</td>
                                            <td>
                                                <span class="badge bg-success"
                                                      ><i class="fas fa-check-circle me-1"></i>Completed</span
                                                >
                                            </td>
                                            <td><span class="badge bg-info text-dark">Paid</span></td>
                                            <td>123 Main St, Ben Tre</td>
                                            <td>Tr?m d?ng chân</td>
                                            <td>
                                                <div class="btn-group btn-group-sm gap-3">
                                                    <button class="btn btn-outline-primary">
                                                        <i class="fas fa-eye me-1"></i>View
                                                    </button>
                                                    <button class="btn btn-outline-warning">
                                                        <i class="fas fa-edit me-1"></i>Edit
                                                    </button>
                                                    <button class="btn btn-outline-danger">
                                                        <i class="fas fa-times me-1"></i>Cancel
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#023</td>
                                            <td>2025-10-07</td>
                                            <td>Saint Tone Emvipi</td>
                                            <td class="text-success fw-semibold">100.000 VND</td>
                                            <td>
                                                <span class="badge bg-warning text-dark"
                                                      ><i class="fas fa-clock me-1"></i>Pending</span
                                                >
                                            </td>
                                            <td><span class="badge bg-danger">Unpaid</span></td>
                                            <td>456 Oak Ave, Town</td>
                                            <td>None</td>
                                            <td>
                                                <div class="btn-group btn-group-sm gap-3">
                                                    <button class="btn btn-outline-primary gap-1">
                                                        <i class="fas fa-eye me-1"></i>View
                                                    </button>
                                                    <button class="btn btn-outline-warning gap-1">
                                                        <i class="fas fa-edit me-1"></i>Edit
                                                    </button>
                                                    <button class="btn btn-outline-danger gap-1">
                                                        <i class="fas fa-times me-1"></i>Cancel
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#RSTN9874</td>
                                            <td>2025-10-06</td>
                                            <td>Charlie Brown</td>
                                            <td class="text-success fw-semibold">38.999 VND</td>
                                            <td>
                                                <span class="badge bg-danger"
                                                      ><i class="fas fa-ban me-1"></i>Cancelled</span
                                                >
                                            </td>
                                            <td><span class="badge bg-secondary">Refunded</span></td>
                                            <td>789 Pine Ln, Village</td>
                                            <td>-</td>
                                            <td>
                                                <div class="btn-group btn-group-sm gap-3">
                                                    <button class="btn btn-outline-primary gap-1">
                                                        <i class="fas fa-eye me-1"></i>View
                                                    </button>
                                                    <button class="btn btn-outline-warning gap-5 disabled">
                                                        <i class="fas fa-edit me-1"></i>Edit
                                                    </button>
                                                    <button class="btn btn-outline-danger gap-5 disabled">
                                                        <i class="fas fa-times me-1"></i>Cancel
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Pagination -->
                        <div
                            class="card-footer bg-white border-0 d-flex justify-content-start align-items-center"
                            >
                            <nav>
                                <ul class="pagination pagination-sm mb-0">
                                    <li class="page-item disabled">
                                        <a class="page-link" href="#"
                                           ><i class="fas fa-chevron-left"></i> Prev</a
                                        >
                                    </li>
                                    <li class="page-item active">
                                        <a class="page-link" href="#">1</a>
                                    </li>
                                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                                    <li class="page-item"><a class="page-link" href="#">...</a></li>
                                    <li class="page-item"><a class="page-link" href="#">4</a></li>
                                    <li class="page-item">
                                        <a class="page-link" href="#"
                                           >Next <i class="fas fa-chevron-right"></i
                                            ></a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </main>
        </div>

<!--         JS 
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        ></script>-->
    </body>
</html>
