<%--
    Document   : forgot
    Created on : Oct 23, 2025
    Author     : Huynh Thai Duy Phuong
    Description: Forgot Password/Reset Password form.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:set var="resetCodeSent" value="${not empty sessionScope.resetCode}" />
        <title>
            <c:choose>
                <c:when test="${resetCodeSent}">Reset Password</c:when>
                <c:otherwise>Forgot Password</c:otherwise>
            </c:choose>
            - Yummy</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/uistyle.css">

        <style>

            .message-box {
                font-weight: 600;
                margin-bottom: 10px;
                padding: 10px;
                border-radius: 4px;
            }
            .error-message {
                color: #dc3545;
                border: 1px solid #f5c6cb;
                background-color: #f8d7da;
            }
            .success-message {
                color: #155724;
                border: 1px solid #c3e6cb;
                background-color: #d4edda;
            }
            .note-message{
                color: #fd7e14;
                border: 1px solid papayawhip;
                background-color: #ffe69c;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="login-box">
                <div class="brand-section">
                    <h1 class="brand-name">Yummy.</h1>
                    <p class="tagline">Recover your account for delicious!</p>
                </div>


                <c:set var="formTitle">
                    <c:choose>
                        <c:when test="${resetCodeSent}">Reset Password</c:when>
                        <c:otherwise>Forgot Password</c:otherwise>
                    </c:choose>
                </c:set>

                <div class="login-form">
                    <h2>${formTitle}</h2>

                    <c:if test="${not empty requestScope.message}">
                        <div class="message-box success-message">
                            ✅ ${requestScope.message}
                        </div>
                    </c:if>

                    <c:if test="${not empty requestScope.error}">
                        <div class="message-box error-message">
                            ❌ ${requestScope.error}
                        </div>
                    </c:if>

<!--reset form-->
                    <c:choose> 
                        <c:when test="${resetCodeSent}">
                            <div class="message-box note-message">
                                <p>A 6-digit code has been sent to **${sessionScope.resetEmail}**. Enter the code and your new password below. (Code expires in 5 minutes)</p>
                            </div>
                            <form action="forgetPassword" method="POST">
                                <input type="hidden" name="action" value="reset_password">

                                <div class="form-group">
                                    <label for="code">Reset Code</label>
                                    <input type="text" id="code" name="code" placeholder="Enter the 6-digit code" required>
                                </div>

                                <div class="form-group">
                                    <label for="newPassword">New Password</label>
                                    <input type="password" id="newPassword" name="newPassword" placeholder="Enter your new password" required>
                                </div>

                                <div class="form-group">
                                    <label for="confirmPassword">Confirm Password</label>
                                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm your new password" required>
                                </div>

                                <button type="submit" class="btn-login">Reset Password</button>
                            </form>

                            <div class="forgot-password-link">
                                <a href="forgetPassword?action=cancel">Go back to Login</a>
                            </div>

                        </c:when>
                        <c:otherwise> <!--email first-->                          
                            <div class="message-box note-message">
                                <p>Please enter your email address to receive a password reset code.</p>
                            </div>
                            <form action="forgetPassword" method="POST">
                                <input type="hidden" name="action" value="send_code">

                                <div class="form-group">
                                    <label for="email">Email Address</label>
                                    <input type="email" id="email" name="email" placeholder="Enter your email"
                                           value="${requestScope.email}" required>
                                </div>

                                <button type="submit" class="btn-login">Send Reset Code</button>
                            </form>

                            <div class="forgot-password-link">
                                <a href="login">Go back to Login</a>
                            </div>

                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="image-section">
                <div class="overlay">
                    <h2>Enjoy Your Healthy Delicious Food</h2>
                    <p>Join us today and discover a world of flavors</p>
                </div>
            </div>
        </div>


    </body>
</html>