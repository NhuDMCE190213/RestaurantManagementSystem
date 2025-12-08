<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Verify OTP - Yummy</title>
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
                    <p class="tagline">Check your email for the verification code.</p>
                </div>

                <form class="login-form" action="verification" method="POST">
                    <h2>Email Verification</h2>

                    <c:if test="${not empty requestScope.error}">
                        <div class="message-box error-message">
                            ❌ ${requestScope.error}
                        </div>
                    </c:if>
                    <div class="message-box note-message">
                        <p>
                            A 6-digit verification code has been sent to your email.
                        </p>
                    </div>
                    <div class="form-group">
                        <label for="otp">Enter OTP Code (*)</label>
                        <input type="text" id="otp" name="otp" 
                               placeholder="Enter 6-digit code" maxlength="6" required>
                    </div>

                    <button type="submit" class="btn-login">Verify</button>
                </form>
                <p class="signup-link">
                    <a href="register">Go back to Register</a>
                </p>
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