$(document).ready(function () {
    feather.replace();

    loadGraphs();

    $("#date").datepicker({
        dateFormat: "yy-mm-dd"
    });
});

function loadGraphs() {
    loadTrendData("/cycletime/", team).done(drawCycleTimeChart);
    loadTrendData("/deployment_frequency/", team).done(drawDeploymentFrequencyChart);
    loadTrendData("/incidents_due_to_change/", team).done(drawIncidentsDueToChangeChart);
    loadTrendData("/production_defects/", team).done(drawProductionDefectsChart);
    loadTrendData("/team_happiness/", team).done(drawTeamHappinessChart);
    loadTrendData("/customer_satisfaction/", team).done(drawCustomerSatisfactionChart);
}

var frm = $('#metric_capture');

frm.submit(function (e) {

    e.preventDefault();

    $("#metric_capture input[type=text]").each(function () {
        if (this.name === 'date' || this.value === "") {
            console.log(this.name + ': skip field');
            return;
        }
        $.ajax({
            method: "GET",
            url: "/metrics/" + this.name + "/" + team + "/" + $("#date").val() + "/" + this.value,
            success: function (url) {
                console.log(url + ': Submission was successful.');
            },
            error: function (url) {
                console.log(url + ': An error occurred.');
            }
        });
    });
    $("#message").text("Metrics Submitted");
    return false;

});

var captureLinkElem = $("#capture-link");
var dashboardLinkElem = $("#dashboard-link");
var captureContentElem = $("#capture-content");
var dashboardContentElem = $("#dashboard-content");

captureLinkElem.click(function () {
    captureLinkElem.addClass("active");
    dashboardLinkElem.removeClass("active");
    captureContentElem.removeClass("d-none").addClass("d-block");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
});

dashboardLinkElem.click(function () {
    loadGraphs();
    captureLinkElem.removeClass("active");
    dashboardLinkElem.addClass("active");
    captureContentElem.addClass("d-none").removeClass("d-block");
    dashboardContentElem.addClass("d-block").removeClass("d-none");
});

$("#refresh-button").click(function () {
    loadGraphs();
});

$("#date").change(function () {
    $("#metric_capture input[type=text]").each(function () {

        var elementid = this.id;

        if (this.name === 'date') {
            console.log(this.name + ': skip field');
            return;
        } else {
            $("#" + elementid).val("");
        }

        function success_cb(data) {
            if (data !== "") {
                console.log(elementid + " : " + data.value);
                $("#" + elementid).val(data.value);
            }
        }

        $.ajax({
            method: "GET",
            url: "/metrics/" + this.name + "/" + team + "/" + $("#date").val(),
            success: function (data) {
                success_cb(data);
            },
            error: function (data) {
                console.log(data);
            }
        });
    });
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

    new Chart(ctx, getChartConfig(data, "Incidents Due To Change", "Incidents"));

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