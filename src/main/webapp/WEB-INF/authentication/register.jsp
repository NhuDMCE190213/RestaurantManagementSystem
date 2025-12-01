<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register - Yummy</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/uistyle.css">

        <style>

            .login-box {

                min-height: 600px;
            }
            .error-message {
                color: #dc3545;
                font-weight: 600;
                margin-bottom: 10px;
                padding: 10px;
                border: 1px solid #f5c6cb;
                background-color: #f8d7da;
                border-radius: 4px;
            }
            .signup-link {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="login-box">
                <div class="brand-section">
                    <h1 class="brand-name">Yummy.</h1>
                    <p class="tagline">Join us and discover a world of flavors!</p>
                </div>

                <form class="login-form" action="register" method="POST">
                    <h2>Register</h2>


                    <c:if test="${not empty requestScope.error}">
                        <div class="error-message">
                            ❌ ${requestScope.error}
                        </div>
                    </c:if>


                    <div class="form-group">
                        <label for="account">Username (*)</label>
                        <input type="text" id="account" name="account" placeholder="Enter username" 
                               value="${requestScope.account}" required>
                    </div>


                    <div class="form-group">
                        <label for="password">Password (*)</label>
                        <input
                              type="password"
                              id="password"
                              name="password"
                              placeholder="Enter password"
                              required
                                                      >
                    </div>


                    <div class="form-group">
                        <label for="confirm_password">Confirm password (*)</label>
                        <input type="password" id="confirm_password" name="confirm_password" required>
                    </div>

                    <div class="form-group">
                        <label for="name">Full Name (*)</label>
                        <input type="text" id="name" name="name"
                               value="${requestScope.name}" required
                              >
                    </div>


                    <div class="form-group">
                        <label for="email">Email (*)</label>
                        <input type="email" id="email" name="email"
                               value="${requestScope.email}" required>
                    </div>


                    <div class="form-group">
                        <label for="phone">Phone number (*)</label>
                        <input type="tel" id="phone" name="phone"
                               value="${requestScope.phone}" required
                              >
                    </div>


                    <button type="submit" class="btn-login">Register</button>

                    <div class="divider">
                        <span>Already have an account?</span>
                    </div>


                    <p class="signup-link">
                        <a href="login">Login</a>
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