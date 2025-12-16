<%-- 
    Document   : listByReservation
    Created on : 3 Dec 2025, 7:33:33 AM
    Author     : Dai Minh Nhu - CE190213
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Result Payment - Yummy"/>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section style="margin-top: 50px; text-align: center;" class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div>
        <img src="https://cdn2.cellphones.com.vn/insecure/rs:fill:150:0/q:90/plain/https://cellphones.com.vn/media/wysiwyg/Review-empty.png" 
             alt="Transaction Status" 
             style="width: 120px; height: 120px; margin-bottom: 20px;">
    </div>

    <!-- Giao d?ch thành công -->
    <c:if test="${transResult}">
        <div>
            <h3 style="font-weight: bold; color: #28a745;">
                Successfull Payment!
                <i class="fas fa-check-circle"></i>
            </h3>
        </div>
    </c:if>

    <!-- Giao d?ch th?t b?i -->
    <c:if test="${transResult == false}">
        <div>
            <h3 style="font-weight: bold; color: #dc3545;">
                Failed Payment!
            </h3>
        </div>
    </c:if>

    <!-- ?ang x? lý giao d?ch -->
    <c:if test="${transResult == null}">
        <div>
            <h3 style="font-weight: bold; color: #ffc107;">
                We have received your order, please wait for processing!
            </h3>
            <a></a>
        </div>
    </c:if>
</section>
<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
