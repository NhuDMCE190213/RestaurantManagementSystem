<%-- 
    Document   : bookatable
    Created on : Dec 9, 2025, 3:04:43 PM
    Author     : Tiêu Gia Huy - CE191594
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Select Table</title>

        <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .table-grid {
                max-width: 1100px;
                margin: 60px auto;
            }
            .table-card {
                border-radius: 16px;
                border: 1px solid #d1e7dd;
                background-color: #ffffff;
                box-shadow: 0 4px 12px rgba(0,0,0,0.04);
                padding: 16px;
                text-align: center;
                cursor: pointer;
                transition: transform .15s ease, box-shadow .15s ease;
            }
            .table-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 10px 20px rgba(0,0,0,0.08);
            }
            .table-name {
                font-weight: 600;
                margin-bottom: 4px;
            }
            .table-capacity {
                font-size: 14px;
                color: #6c757d;
                margin-bottom: 8px;
            }
            .badge-status {
                display: inline-block;
                padding: 4px 12px;
                border-radius: 999px;
                font-size: 13px;
            }
            .badge-available {
                background-color: #d1e7dd;
                color: #0f5132;
            }
            .badge-occupied {
                background-color: #f8d7da;
                color: #842029;
            }
        </style>
    </head>
    <body>

        <div class="table-grid">
            <h3 class="mb-4 text-center">Select a table to create reservation</h3>

            <div class="row g-3">
                <c:forEach var="t" items="${listTable}">
                    <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                        <a href="${pageContext.request.contextPath}/reservation?view=add&tableId=${t.id}" style="text-decoration:none;">
                            <div class="table-card">
                                <div class="table-name">Table ${t.number}</div>
                                <div class="table-capacity">${t.capacity} Guests</div>
                                <span class="badge-status
                                      <c:if test='${fn:toLowerCase(t.status) == "available"}'>badge-available</c:if>
                                      <c:if test='${fn:toLowerCase(t.status) != "available"}'>badge-occupied</c:if>">
                                    ${t.status}
                                </span>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

