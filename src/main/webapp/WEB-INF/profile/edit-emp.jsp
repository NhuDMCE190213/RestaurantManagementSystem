<%-- 
    Document   : edit-emp
    Created on : Oct 28, 2025, 9:40:29 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Edit Employee Profile - Yummy Dashboard"/>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10" aria-label="Edit Profile section">
    <div class="content-card shadow-sm p-4 p-md-5 mt-3">

        <h3 class="mb-4 text-center">Edit Employee Profile</h3>
        <p class="text-center text-secondary mb-4">Update your personal information below</p>

        <c:set var="employee" value="${requestScope.employee}"/>
        <c:if test="${employee == null}">
            <c:set var="employee" value="${sessionScope.employeeSession}"/>
        </c:if>

        <c:set var="isOtpPending" value="${not empty sessionScope.otpCode}"/>
        <c:set var="readOnlyAttr" value="${isOtpPending ? 'readonly' : ''}"/>
        <c:set var="disabledAttr" value="${isOtpPending ? 'disabled' : ''}"/>

        <form action="employee-profile" method="post">
            <input type="hidden" name="action" value="edit"/>

            <c:if test="${isOtpPending}">
                <input type="hidden" name="emp_name" value="${employee.empName}"/>
                <input type="hidden" name="phone_number" value="${employee.phoneNumber}"/>
                <input type="hidden" name="dob" value="${employee.dob}"/>
                <input type="hidden" name="gender" value="${employee.gender}"/>
                <input type="hidden" name="address" value="${employee.address}"/>
                <input type="hidden" name="email" value="${sessionScope.newEmailPending}"/>
            </c:if>

            <!-- Full Name -->
            <div class="mb-3">
                <label for="emp_name" class="form-label">Full Name *</label>
                <input type="text" id="emp_name" class="form-control" name="emp_name"
                       value="${employee.empName}" required ${readOnlyAttr}/>
            </div>

            <!-- Email -->
            <div class="mb-3">
                <label for="email" class="form-label">Email Address *</label>
                <input type="email" id="email" class="form-control" name="email"
                       value="${employee.email}" required ${readOnlyAttr}/>
            </div>

            <!-- Phone Number -->
            <div class="mb-3">
                <label for="phone_number" class="form-label">Phone Number *</label>
                <input type="tel" id="phone_number" class="form-control" name="phone_number"
                       value="${employee.phoneNumber}" required ${readOnlyAttr}/>
            </div>

            <!-- DOB -->
            <div class="mb-3">
                <label for="dob" class="form-label">Date of Birth</label>
                <input type="date" id="dob" class="form-control" name="dob"
                       value="${employee.dob}" placeholder="YYYY-MM-DD" ${disabledAttr}/>
            </div>

            <!-- Gender -->
            <div class="mb-3">
                <label class="form-label">Gender *</label>
                <div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender" id="genderMale"
                               value="Male" ${employee.gender == 'Male' ? 'checked' : ''} required ${disabledAttr}>
                        <label class="form-check-label" for="genderMale">Male</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="gender" id="genderFemale"
                               value="Female" ${employee.gender == 'Female' ? 'checked' : ''} required ${disabledAttr}>
                        <label class="form-check-label" for="genderFemale">Female</label>
                    </div>
                </div>
            </div>

            <!-- Address -->
            <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <textarea id="address" class="form-control" name="address" rows="3" ${readOnlyAttr}>${employee.address}</textarea>
            </div>

            <!-- OTP Verification -->
            <c:if test="${isOtpPending}">
                <div class="mb-3 border border-warning p-3 rounded" id="otp-field-container">
                    <div class="alert alert-warning py-2 mb-3 text-center" role="alert">
                        <i class="bi bi-shield-lock-fill me-2"></i> **Verification required:** Enter the 6-digit code sent to **${sessionScope.newEmailPending}**
                    </div>
                    <label for="otp_code" class="form-label">Verification Code (OTP) *</label>
                    <input type="text" id="otp_code" class="form-control" name="otp_code"
                           placeholder="Enter 6-digit code" required maxlength="6"
                           pattern="\d{6}" title="The code must be 6 digits."/>
                </div>
            </c:if>

            <!-- Action Buttons -->
            <div class="text-center">
                <button type="submit" class="btn btn-danger px-5 py-2">
                    <i class="bi bi-save"></i> Save Changes
                </button>
                <a href="employee-profile?action=view" class="btn btn-outline-danger px-5 py-2">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>