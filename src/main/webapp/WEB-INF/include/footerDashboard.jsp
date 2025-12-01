<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</div>
</div>
</main>



<!-- Vendor JS Files -->
<script src="<%=request.getContextPath()%>/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/aos/aos.js"></script>

<!-- Main JS File -->
<script src="<%=request.getContextPath()%>/assets/js/main.js"></script>

<c:if test="${not empty dashboard_cssjs}">
    <script src="https://cdn.jsdelivr.net/npm/feather-icons@4.28.0/dist/feather.min.js" integrity="sha384-uO3SXW5IuS1ZpFPKugNNWqTZRRglnUJK6UAZ/gxOX80nxEkN9NcGZTftn6RzhGWE" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@2.9.4/dist/Chart.min.js" integrity="sha384-zNy6FEbO50N+Cg5wap8IKA4M/ZnLJgzc6w2NqACZaK0u0FXfOWRRJOnQtpZun8ha" crossorigin="anonymous"></script>
    <script> // src="/assets/js/dashboard.js"
        /* globals Chart:false, feather:false */

        (function () {
            'use strict'

            feather.replace({'aria-hidden': 'true'})

            var labels = [
        <c:forEach var="d" items="${datesList}" varStatus="loop">
            '${d}'<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
            ];

            var dataPoints = [
        <c:forEach var="i" items="${incomeList}" varStatus="loop">
            ${i}<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
            ];

            // Graphs
            var ctx = document.getElementById('myChart');
            // eslint-disable-next-line no-unused-vars
            var myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                            data: dataPoints,
                            lineTension: 0,
                            backgroundColor: 'transparent',
                            borderColor: '#007bff',
                            borderWidth: 4,
                            pointBackgroundColor: '#007bff'
                        }]
                },
                options: {
                    scales: {
                        yAxes: [{
                                ticks: {
                                    beginAtZero: false
                                }
                            }]
                    },
                    legend: {
                        display: false
                    }
                }
            })
        })()
    </script>
</c:if>

</body>

<c:if  test="${not empty sessionScope.popupStatus}">
    <div class="modal" id="exampleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Action ${sessionScope.popupStatus eq true?"Successful":"Fail"}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <c:choose>
                        <c:when test="${sessionScope.popupStatus eq true}">
                            <p style="color: green"><c:out value="${sessionScope.popupMessage}"/></p>
                        </c:when>
                        <c:otherwise>
                            <p style="color: red"><c:out value="${sessionScope.popupMessage}"/></p>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <!--<button type="button" class="btn btn-primary">Save changes</button>-->
                </div>
            </div>
        </div>
    </div>

    <script>
        var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
        myModal.show();
    </script>
</c:if>

<script>
    function showDeletePopup(id) {
        document.getElementById("hiddenInputIdDelete").value = id;
        document.getElementById("idForDeletePopup").textContent = "Are you sure you want to delete the object with id = " + id + "?";
        var myModal = new bootstrap.Modal(document.getElementById('deletePopup'));
        myModal.show();
    }
</script>

</html>