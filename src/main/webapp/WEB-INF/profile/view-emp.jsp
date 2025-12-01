<%-- 
    Document   : view-emp
    Created on : Oct 28, 2025, 9:38:16 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" value="My Profile - Yummy Restaurant"/>

<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10" aria-label="Profile section">
    <div class="content-card shadow-sm p-4 p-md-5 mt-3">

        <%-- Messages --%>
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
            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Username</div>
                <div class="col-sm-8"><strong>${employee.empAccount}</strong></div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Role</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.role.name}">
                            ${employee.role.name}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Full Name</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.empName}">
                            ${employee.empName}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Gender</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.gender}">
                            ${employee.gender}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Phone Number</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.phoneNumber}">
                            ${employee.phoneNumber}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Email Address</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.email}">
                            ${employee.email}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3 border-bottom">
                <div class="col-sm-4 fw-semibold text-secondary">Address</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.address}">
                            ${employee.address}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="row py-3">
                <div class="col-sm-4 fw-semibold text-secondary">Date of Birth</div>
                <div class="col-sm-8">
                    <c:choose>
                        <c:when test="${not empty employee.dob}">
                            ${employee.dob}
                        </c:when>
                        <c:otherwise><span class="text-muted fst-italic">Not provided</span></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="text-center mt-4 pt-4 border-top">
            <a href="employee-profile?action=edit" class="btn btn-danger px-5 py-2 me-3">
                <i class="bi bi-pencil-square me-1"></i> Edit Profile
            </a>
            <a href="employee-profile?action=change-password" class="btn btn-outline-danger px-5 py-2">
                <i class="bi bi-key me-1"></i> Change Password
            </a>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>