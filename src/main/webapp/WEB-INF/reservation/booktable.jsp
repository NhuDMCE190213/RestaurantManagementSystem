<%-- 
    Document   : booktable
    Created on : Oct 29, 2025, 3:47:47 PM
    Author     : TruongBinhTrong
--%>

<%@ page import="model.Table" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@include file="/WEB-INF/include/headerBookTable.jsp" %>

<main class="main">

    <div class="page-title" data-aos="fade">
        <div class="container">
            <h1>Choose Your Table</h1>
        </div>
    </div>

    <section id="tables" class="tables section">
        <div class="container section-title" data-aos="fade-up">
            <p><span>Select the perfect</span> <span class="description-title">Spot for Your Meal</span></p>
        </div>

        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <!-- Legend -->
            <div class="row align-items-center justify-content-between g-4 table-legend">
                <div class="col-md-4">
                    <div class="legend-item d-flex align-items-center">
                        <span class="legend-swatch available"></span>
                        <span>Available Table</span>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="legend-item d-flex align-items-center">
                        <span class="legend-swatch reserved"></span>
                        <span>Reserved</span>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="legend-item d-flex align-items-center">
                        <span class="legend-swatch occupied"></span>
                        <span>Occupied</span>
                    </div>
                </div>
            </div>

            <!-- Table List -->
            <div class="row g-4 mt-1" data-aos="fade-up" data-aos-delay="150">

                <%
                    List<Table> tables = (List<Table>) request.getAttribute("tableList");
                    if (tables != null) {
                        for (Table t : tables) {
                            String status = t.getStatus().toLowerCase();
                            boolean clickable = true;
                %>

                <div class="col-6 col-md-4 col-lg-3">
                    <div class="table-card <%= status%> h-100 text-center p-4"
                         style="<%= clickable ? "cursor:pointer;" : "cursor:not-allowed; opacity:0.6;"%>"
                         <%= clickable
                                 ? "onclick=\"window.location.href='booktable?view=add&tableId=" + t.getId() + "'\""
                                 : ""%>>
                        <h4>Table <%= t.getNumber()%></h4>
                        <p class="capacity"><%= t.getCapacity()%> Guests</p>
                        <span class="status"><%= t.getStatus()%></span>
                    </div>
                </div>

                <%
                    }
                } else {
                %>
                <p>No tables available.</p>
                <% }%>

            </div>
        </div>
    </section>

    <!-- Booking Modal -->
    <div class="modal fade" id="bookingModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <form action="<c:url value='/booktable'/>" method="post">
                    <div class="modal-header">
                        <h5 class="modal-title">Book Table</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <input type="hidden" name="tableId" id="tableId">

                        <label class="form-label">Party Size</label>
                        <input type="number" class="form-control" name="partySize" id="modalPartySize" min="1" required>
                        <small class="text-muted">Number of guests</small>

                        <label class="form-label mt-2">Date</label>
                        <input type="date" class="form-control" name="reservationDate" id="modalReservationDate" required>

                        <label class="form-label mt-2">Time</label>
                        <input type="time" class="form-control" name="reservationTime" id="modalReservationTime" required>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-danger">Confirm</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>

            </div>
        </div>
    </div>

</main>
<%@include file="/WEB-INF/include/footerBookTable.jsp" %>
