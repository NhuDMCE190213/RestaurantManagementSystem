<%-- 
    Document   : edit-emp
    Created on : Oct 28, 2025, 9:40:29 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Edit Profile - Yummy Dashboard"/>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10" aria-label="Edit Profile section">
    <div class="content-card shadow-sm p-4 p-md-5 mt-3">

        <h3 class="mb-4 text-center">Edit My Profile</h3>
        <p class="text-center text-secondary mb-4">Update your account information below</p>

        <!-- Popup giống customer -->
        <c:if test="${requestScope.popupStatus != null and requestScope.popupStatus == false}">
            <div class="alert alert-danger d-flex align-items-center" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <span>${requestScope.popupMessage}</span>
            </div>
        </c:if>

        <form action="employee-profile" method="post">
            <input type="hidden" name="action" value="edit"/>

            <!-- Username -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Username</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control bg-light" 
                           value="${employee.empAccount}" readonly />
                </div>
            </div>

            <!-- Full Name -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Full Name *</label>
                <div class="col-sm-9">
                    <input type="text" name="emp_name" class="form-control"
                           value="${employee.empName}" required/>
                </div>
            </div>

            <!-- Gender -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Gender *</label>
                <div class="col-sm-9">
                    <select name="gender" class="form-select" required>
                        <option value="Male"   ${employee.gender eq 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${employee.gender eq 'Female' ? 'selected' : ''}>Female</option>
                    </select>
                </div>
            </div>

            <!-- Phone -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Phone Number *</label>
                <div class="col-sm-9">
                    <input type="text" name="phone_number" class="form-control" 
                           value="${employee.phoneNumber}" required/>
                </div>
            </div>

            <!-- Email -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Email *</label>
                <div class="col-sm-9">
                    <input type="email" name="email" class="form-control"
                           value="${employee.email}" required/>
                </div>
            </div>

            <!-- Address -->
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label fw-semibold">Address</label>
                <div class="col-sm-9">
                    <input type="text" name="address" class="form-control"
                           value="${employee.address}"/>
                </div>
            </div>

            <!-- DOB -->
            <div class="row mb-4">
                <label class="col-sm-3 col-form-label fw-semibold">Date of Birth</label>
                <div class="col-sm-9">
                    <input type="date" name="dob" class="form-control"
                           value="${employee.dob}"/>
                </div>
            </div>

            <div class="text-center mt-4 pt-4 border-top">
                <button type="submit" class="btn btn-danger px-5 py-2 me-3">
                    <i class="bi bi-save me-1"></i> Save Changes
                </button>
                <a href="employee-profile?action=view" 
                   class="btn btn-outline-danger px-5 py-2">
                    <i class="bi bi-x-circle me-1"></i> Cancel
                </a>
            </div>

        </form>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>