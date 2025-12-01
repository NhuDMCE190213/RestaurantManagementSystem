<%-- 
    Document   : login
    Created on : Oct 23, 2025
    Author     : [Tên của bạn]
    Description: Login form using EL and JSTL to display error messages.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Yummy</title>
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
           
        </style>
    </head>
    <body>
        <div class="container">
            <div class="login-box">
                <div class="brand-section">
                    <h1 class="brand-name">Yummy.</h1>
                    <p class="tagline">Welcome back to delicious!</p>
                </div>


                <form class="login-form" action="login" method="POST">
                    <h2>Sign In</h2>

                    <c:if test="${not empty sessionScope.popupMessage}">
                        <div class="message-box success-message">
                            ✅ ${sessionScope.popupMessage}
                        </div>

                        <c:remove var="popupMessage" scope="session"/>
                    </c:if>

                    <c:if test="${not empty requestScope.error}">
                        <div class="message-box error-message">
                            ❌ ${requestScope.error}
                        </div>
                    </c:if>

                    <div class="form-group">
                        <label for="account">Username</label>

                        <input type="text" id="account" name="account" placeholder="Enter your username" 
                               value="${requestScope.account}" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    </div>



                    <button type="submit" class="btn-login">Log In</button>

                    <div class="divider">
                        <span>OR</span>
                    </div>

                    <p class="signup-link">
                        Don't have an account? <a href="register">Register</a>
                    </p>
                    <p class="signup-link">
                        Or if you are a member of restaurant. <a href="login_employee">Login as Employee</a>
                    </p>
                </form>
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