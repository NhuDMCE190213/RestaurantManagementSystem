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
