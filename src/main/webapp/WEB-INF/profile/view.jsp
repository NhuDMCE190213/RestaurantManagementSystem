 <title>My Profile - Yummy</title>
        <%@include file="/WEB-INF/include/headerCustomer.jsp" %>

        <main class="main">



            <section id="profile" class="profile-section pt-5 section">
                <div class="container" data-aos="fade-up" data-aos-delay="100">

                    <div class="row justify-content-center">
                        <div class="col-lg-8">
                            <div class="card p-4 p-md-5">

                                <%-- Display Messages --%>
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success d-flex align-items-center" role="alert">
                                        <i class="bi bi-check-circle-fill me-2"></i>
                                        <div>${successMessage}</div>
                                    </div>
                                </c:if>

                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger d-flex align-items-center" role="alert">
                                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                        <div>${errorMessage}</div>
                                    </div>
                                </c:if>

                                <h3 class="mb-4 text-center">Profile Information</h3>

                                <div class="profile-info">
                                    <%-- Customer Info Rows --%>
                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Username</div>
                                        <div class="col-sm-8 info-value">
                                            <strong>${customer.customerAccount}</strong>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Full Name</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.customerName}">
                                                    ${customer.customerName}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Gender</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.gender}">
                                                    ${customer.gender}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Phone Number</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.phoneNumber}">
                                                    ${customer.phoneNumber}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Email Address</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.email}">
                                                    ${customer.email}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Address</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.address}">
                                                    ${customer.address}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3">
                                        <div class="col-sm-4 info-label">Date of Birth</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty customer.dob}">
                                                    ${customer.dob}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                                <%-- Action Buttons --%>
                                <div class="text-center mt-4 pt-4 border-top">
                                    <a href="customer-profile?action=edit" class="btn btn-danger px-5 py-2 me-3">
                                        <i class="bi bi-pencil-square me-1"></i> Edit Profile
                                    </a>
                                    <a href="customer-profile?action=change-password" class="btn btn-outline-danger px-5 py-2">
                                        <i class="bi bi-key me-1"></i> Change Password
                                    </a>
                                </div>

                            </div>
                        </div>
                    </div>

                </div>
            </section>
        </main>

        <%-- Include Footer --%>
        <%@include file="/WEB-INF/include/footerCustomer.jsp" %>
    </body>
</html>