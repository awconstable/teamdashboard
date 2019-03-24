$(document).ready(function () {
    feather.replace();

    loadTrendData("/cycletime/", team).done(drawCycleTimeChart);
    loadTrendData("/deployment_frequency/", team).done(drawDeploymentFrequencyChart);
    loadTrendData("/incidents_due_to_change/", team).done(drawIncidentsDueToChangeChart);
    loadTrendData("/production_defects/", team).done(drawProductionDefectsChart);
    loadTrendData("/team_happiness/", team).done(drawTeamHappinessChart);
    loadTrendData("/customer_satisfaction/", team).done(drawCustomerSatisfactionChart);

});

function loadTrendData(url, slug) {
    return $.ajax({
        url: "/metrics" + url + slug + "/",
        dataType: "json"
    });
}

function getChartConfig(data, title, yAxisLabel) {
    return {
        type: 'line',
        data: data,
        options: {
            title: {
                display: true,
                text: title
            },
            legend: {
                display: false
            },
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel
                    },
                    type: "linear",
                    ticks: {
                        beginAtZero: true
                    }
                }],
                xAxes: [{
                    type: 'time',
                    time: {
                        unit: 'month',
                        tooltipFormat: 'MMM YYYY'
                    }
                }]
            }
        }
    }
}

function drawCycleTimeChart(data) {

    var ctx = $('#chart1');

    new Chart(ctx, getChartConfig(data, "Average Cycle Time", "Days"));
}

function drawDeploymentFrequencyChart(data) {

    var ctx = $('#chart2');

    new Chart(ctx, getChartConfig(data, "Deployment Frequency", "Days"));

}

function drawIncidentsDueToChangeChart(data) {

    var ctx = $('#chart3');

    new Chart(ctx, getChartConfig(data, "Incidents due to change", "Incidents"));

}

function drawProductionDefectsChart(data) {

    var ctx = $('#chart4');

    new Chart(ctx, getChartConfig(data, "Production Defects", "Defects"));

}

function drawTeamHappinessChart(data) {

    var ctx = $('#chart5');

    new Chart(ctx, getChartConfig(data, "Team Happiness", "Happiness Index"));

}

function drawCustomerSatisfactionChart(data) {

    var ctx = $('#chart6');

    new Chart(ctx, getChartConfig(data, "Customer Satisfaction", "CSAT Index"));
    
}