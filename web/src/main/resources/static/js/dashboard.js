$(document).ready(function () {
    feather.replace();
    google.charts.load('current', {'packages': ['line']});
    google.charts.setOnLoadCallback(loadCharts);
});

function loadTrendData(url, slug) {
    return $.ajax({
        url: "/metrics" + url + slug + "/",
        dataType: "json"
    });
}

function loadCharts() {

    loadTrendData("/cycletime/", team).done(drawCycleTimeChart);
    loadTrendData("/deployment_frequency/", team).done(drawDeploymentFrequencyChart);
    loadTrendData("/incidents_due_to_change/", team).done(drawIncidentsDueToChangeChart);
    loadTrendData("/production_defects/", team).done(drawProductionDefectsChart);
    loadTrendData("/team_happiness/", team).done(drawTeamHappinessChart);
    loadTrendData("/customer_satisfaction/", team).done(drawCustomerSatisfactionChart);
}

function drawCycleTimeChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Average Cycle Time'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Average Cycle Time (days)', viewWindow: {min: 0}}
        },
        colors: ['orange']
    };

    var chart = new google.charts.Line(document.getElementById('linechart_cycletime'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawDeploymentFrequencyChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Deployment Frequency'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Deployment Frequency (days)', viewWindow: {min: 0}}
        },
        colors: ['green']
    };

    var chart = new google.charts.Line(document.getElementById('linechart_deployment_frequency'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawIncidentsDueToChangeChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Incidents due to change'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Incidents due to change', viewWindow: {min: 0}}
        },
        colors: ['red']
    };

    var chart = new google.charts.Line(document.getElementById('linechart_incidents_due_to_change'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawProductionDefectsChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Production Defects'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Production Defects', viewWindow: {min: 0}}
        }
    };

    var chart = new google.charts.Line(document.getElementById('linechart_production_defects'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawTeamHappinessChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Team Happiness'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Team Happiness', viewWindow: {min: 0}}
        }
    };

    var chart = new google.charts.Line(document.getElementById('linechart_team_happiness'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawCustomerSatisfactionChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Customer Satisfaction'
        },
        legend: {
            position: 'none'
        },
        width: 400,
        height: 300,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Customer Satisfaction', viewWindow: {min: 0}}
        }
    };

    var chart = new google.charts.Line(document.getElementById('linechart_customer_satisfaction'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}