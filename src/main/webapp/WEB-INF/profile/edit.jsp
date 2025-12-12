<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>My Profile - Yummy</title>
<c:set var="title" value="Edit Profile - Yummy Restaurant"/>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">
    <section id="edit-profile" class="profile-section pt-5 section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card p-4 p-md-5">

                        <h3 class="mb-4 text-center">Edit Customer Profile</h3>
                        <p class="subtitle text-center text-muted mb-4">Update your personal information below</p>

                        <c:set var="customer" value="${requestScope.customer}"/>
                        <c:if test="${customer == null}">
                            <c:set var="customer" value="${sessionScope.customerSession}"/>
                        </c:if>


                        <c:set var="isOtpPending" value="${not empty sessionScope.otpCode}"/>
                        <c:set var="readOnlyAttr" value="${isOtpPending ? 'readonly' : ''}"/>
                        <c:set var="disabledAttr" value="${isOtpPending ? 'disabled' : ''}"/>


                        <form action="customer-profile" method="post">
                            <input type="hidden" name="action" value="edit"/>

                            <c:if test="${isOtpPending}">

                                <input type="hidden" name="customer_name" value="${customer.customerName}"/>
                                <input type="hidden" name="phone_number" value="${customer.phoneNumber}"/>
                                <input type="hidden" name="dob" value="${customer.dob}"/>
                                <input type="hidden" name="gender" value="${customer.gender}"/>
                                <input type="hidden" name="address" value="${customer.address}"/>
                                <input type="hidden" name="email" value="${sessionScope.newEmailPending}"/>
                            </c:if>

                            <div class="mb-3">
                                <label for="customer_name" class="form-label info-label">Full Name *</label>
                                <input type="text" id="customer_name" class="form-control" name="customer_name"
                                       value="${customer.customerName}" required ${readOnlyAttr}/>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label info-label">Email Address *</label>
                                <input type="email" id="email" class="form-control" name="email"
                                       value="${customer.email}" required ${readOnlyAttr}/>
                            </div>


                            <c:if test="${isOtpPending}">
                                <div class="mb-3 border border-warning p-3 rounded" id="otp-field-container">
                                    <div class="alert alert-warning py-2 mb-3 text-center" role="alert">
                                        <i class="bi bi-shield-lock-fill me-2"></i> **Verification required:** Enter the 6-digit code sent to **${sessionScope.newEmailPending}**
                                    </div>
                                    <label for="otp_code" class="form-label info-label">Verification Code (OTP) *</label>
                                    <input type="text" id="otp_code" class="form-control" name="otp_code"
                                           placeholder="Enter 6-digit code" required maxlength="6"
                                           pattern="\d{6}" title="The code must be 6 digits."/>
                                </div>
                            </c:if>


                            <div class="mb-3">
                                <label for="phone_number" class="form-label info-label">Phone Number *</label>
                                <input type="tel" id="phone_number" class="form-control" name="phone_number"
                                       value="${customer.phoneNumber}" required ${readOnlyAttr}/>
                            </div>

                            <div class="mb-3">
                                <label for="dob" class="form-label info-label">Date of Birth</label>
                                <input type="date" id="dob" class="form-control" name="dob"
                                       value="${customer.dob}" placeholder="YYYY-MM-DD" ${disabledAttr}/>
                            </div>

                            <div class="mb-3">
                                <label class="form-label info-label">Gender *</label>
                                <div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender" id="genderMale"
                                               value="Male" ${customer.gender == 'Male' ? 'checked' : ''} required ${disabledAttr}>
                                        <label class="form-check-label" for="genderMale">Male</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender" id="genderFemale"
                                               value="Female" ${customer.gender == 'Female' ? 'checked' : ''} required ${disabledAttr}>
                                        <label class="form-check-label" for="genderFemale">Female</label>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="address" class="form-label info-label">Address</label>
                                <textarea id="address" class="form-control" name="address" rows="3" ${readOnlyAttr}>${customer.address}</textarea>
                            </div>


                            <div class="text-center mt-4 pt-4 border-top">
                                <button type="submit" class="btn btn-danger px-5 py-2 me-3">
                                    <i class="bi bi-save me-1"></i> Save Changes
                                </button>
                                <a href="customer-profile?action=view" class="btn btn-outline-danger px-5 py-2">
                                    Cancel
                                </a>
                            </div>
                        </form>

                    </div>
                </div>
            </div>

        </div>
    </section>
</main>


<c:if test="${requestScope.popupStatus != null and requestScope.popupStatus == false}">
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Action Fail</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p style="color: red">Error: <c:out value="${requestScope.popupMessage}"/></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var modalElement = document.getElementById('exampleModal');
            
            if (modalElement) {
                var myModal = new bootstrap.Modal(modalElement);
                myModal.show();
                modalElement.addEventListener('hidden.bs.modal', function () {

                    const backdrops = document.querySelectorAll('.modal-backdrop');
                    backdrops.forEach(backdrop => {
                        backdrop.remove();
                    });
                    document.body.classList.remove('modal-open');
                    document.body.style.paddingRight = ''; 
                    document.body.style.overflow = '';
                });
            }
        });
    </script>
</c:if>

<%@include file="/WEB-INF/include/footerCustomer.jsp" %>