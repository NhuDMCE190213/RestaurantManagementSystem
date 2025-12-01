<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <title>My Profile - Yummy</title>
<c:set var="title" value="Change Password - Yummy Restaurant"/>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">
    <section id="change-password" class="profile-section pt-5 section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card p-4 p-md-5">

                        <h3 class="mb-4 text-center">Change Password</h3>
                        <p class="subtitle text-center text-muted mb-4">Update your customer account password securely</p>

                        <form action="customer-profile" method="post">
                            <input type="hidden" name="action" value="change-password"/>

                            <div class="mb-3">
                                <label for="oldPassword" class="form-label info-label">Enter Old Password *</label>
                                <input type="password" id="oldPassword" class="form-control" name="oldPassword" required/>
                            </div>

                            <div class="mb-3">
                                <label for="newPassword" class="form-label info-label">Enter New Password *</label>
                                <input type="password" id="newPassword" class="form-control" name="newPassword" required/>
                            </div>

                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label info-label">Confirm New Password *</label>
                                <input type="password" id="confirmPassword" class="form-control" name="confirmPassword" required/>
                            </div>

                            <div class="text-center mt-4 pt-4 border-top">
                                <button type="submit" class="btn btn-danger px-5 py-2 me-3">
                                    <i class="bi bi-key me-1"></i> Update Password
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