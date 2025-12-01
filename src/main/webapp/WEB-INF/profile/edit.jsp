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

                        <form action="customer-profile" method="post">
                            <input type="hidden" name="action" value="edit"/>

                            <div class="mb-3">
                                <label for="customer_name" class="form-label info-label">Full Name *</label>
                                <input type="text" id="customer_name" class="form-control" name="customer_name"
                                       value="${customer.customerName}" required/>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label info-label">Email Address *</label>
                                <input type="email" id="email" class="form-control" name="email"
                                       value="${customer.email}" required/>
                            </div>

                            <div class="mb-3">
                                <label for="phone_number" class="form-label info-label">Phone Number *</label>
                                <input type="tel" id="phone_number" class="form-control" name="phone_number"
                                       value="${customer.phoneNumber}" required/>
                            </div>

                            <div class="mb-3">
                                <label for="dob" class="form-label info-label">Date of Birth</label>
                                <input type="date" id="dob" class="form-control" name="dob"
                                       value="${customer.dob}" placeholder="YYYY-MM-DD"/>
                            </div>

                            <div class="mb-3">
                                <label class="form-label info-label">Gender *</label>
                                <div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender" id="genderMale"
                                               value="Male" ${customer.gender == 'Male' ? 'checked' : ''} required>
                                        <label class="form-check-label" for="genderMale">Male</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender" id="genderFemale"
                                               value="Female" ${customer.gender == 'Female' ? 'checked' : ''} required>
                                        <label class="form-check-label" for="genderFemale">Female</label>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="address" class="form-label info-label">Address</label>
                                <textarea id="address" class="form-control" name="address" rows="3">${customer.address}</textarea>
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
    <div class="modal" id="exampleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Action Fail</h5>
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
        window.onload = function () {
            var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
            myModal.show();
        };
    </script>
</c:if>

<%@include file="/WEB-INF/include/footerCustomer.jsp" %>