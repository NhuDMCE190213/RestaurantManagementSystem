<%-- 
    Document   : changepassword-emp
    Created on : Oct 28, 2025, 9:41:39 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Change Password - Yummy Dashboard"/>

<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10" aria-label="Change Password section">
    <div class="content-card shadow-sm p-4 p-md-5 mt-3">

        <h3 class="mb-4 text-center">Change Password</h3>
        <p class="text-center text-secondary mb-4">Update your employee account password securely</p>

        <!-- Success Message -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success d-flex align-items-center" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <div>${successMessage}</div>
            </div>
        </c:if>

        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger d-flex align-items-center" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div>${errorMessage}</div>
            </div>
        </c:if>

        <div class="card p-4 p-md-5 mt-4">
            <form action="employee-profile" method="post">
                <input type="hidden" name="action" value="change-password"/>

                <div class="mb-3">
                    <label for="oldPassword" class="form-label fw-semibold">Enter Old Password *</label>
                    <input type="password" id="oldPassword" name="oldPassword" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="newPassword" class="form-label fw-semibold">Enter New Password *</label>
                    <input type="password" id="newPassword" name="newPassword" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="confirmPassword" class="form-label fw-semibold">Confirm New Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                </div>

                <div class="text-center mt-4 pt-4 border-top">
                    <button type="submit" class="btn btn-danger px-5 py-2 me-3">
                        <i class="bi bi-key me-1"></i> Update Password
                    </button>
                    <a href="employee-profile?action=view" class="btn btn-outline-danger px-5 py-2">
                        <i class="bi bi-x-circle me-1"></i> Cancel
                    </a>
                </div>
            </form>
        </div>

    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>