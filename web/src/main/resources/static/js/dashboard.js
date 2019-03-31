function findTeamFromUrl() {
    var parts = window.location.pathname.split('/');

    if (parts) {
        if (parts[2] !== "") {
            return parts[2];
        }
    }

    return null;
}

function findModeFromUrl() {
    var parts = window.location.pathname.split('/');

    if (parts) {
        if (parts[1] !== "") {
            return parts[1];
        }
    }

    return "teamexplorer";
}

var mode = null;
var team = null;

$(document).ready(function () {

    window.onpopstate = function (e) {
        if (e.state) {
            document.title = e.state.pageTitle;
            mode = findModeFromUrl();
            team = findTeamFromUrl();
            processPageEntry();
        }
    };

    feather.replace();

    mode = findModeFromUrl();
    team = findTeamFromUrl();

    processPageEntry();

    $("#date").datepicker({
        format: "yyyy-mm-dd"
    });
});

function processPageEntry() {
    if (mode && mode === 'dashboard') {
        selectDashboard(false, team);
    } else if (mode && mode === 'capture') {
        selectCapture(false, team);
    } else {
        selectTeamExplorer(false);
    }
}

function loadTeams() {
    loadTeamHierarchy().done(layoutTeamHierarchy);
}

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

var teamExplorerLinkElem = $("#teamexplorer-link");
var dashboardLinkElem = $("#dashboard-link");
var captureLinkElem = $("#capture-link");

var teamExplorerContentElem = $("#teamexplorer-content");
var dashboardContentElem = $("#dashboard-content");
var captureContentElem = $("#capture-content");

var teamNameElem = $("#team-name");

teamExplorerLinkElem.click(function () {
    selectTeamExplorer(true);
});

dashboardLinkElem.click(function () {
    if (team) {
        selectDashboard(true, team);
    }
});

captureLinkElem.click(function () {
    if (team) {
        selectCapture(true, team);
    }
});

function selectTeamExplorer(updateHistory) {
    if (updateHistory) {
        history.pushState({"pageTitle": 'Team Explorer'}, null, '/teamexplorer/');
    } else {
        history.replaceState({"pageTitle": 'Team Explorer'}, null, '/teamexplorer/');
    }

    loadTeams();

    loadTeam(team).done(updateTeamName);

    teamExplorerLinkElem.addClass("active");
    dashboardLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-none").addClass("d-block");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
}

function selectDashboard(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Dashboard - ' + slug}, null, '/dashboard/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Dashboard - ' + slug}, null, '/dashboard/' + slug);
    }

    loadTeam(slug).done(updateTeamName);
    loadGraphs();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.addClass("active");
    captureLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-none").addClass("d-block");
    captureContentElem.removeClass("d-block").addClass("d-none");
}

function selectCapture(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Capture - ' + slug}, null, '/capture/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Capture - ' + slug}, null, '/capture/' + slug);
    }

    loadTeam(slug).done(updateTeamName);

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    captureLinkElem.addClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-none").addClass("d-block");
}

function updateTeamName(data) {

    if (!data) {
        teamNameElem.text("");
    }
    teamNameElem.text(data.name);
}

$("#teamexplorer-refresh-button").click(function () {
    loadTeamHierarchy();
});

$("#dashboard-refresh-button").click(function () {
    loadGraphs();
});

function loadTeamHierarchy() {
    return $.ajax({
        url: "/teams",
        dataType: "json"
    });
}

function loadTeam(slug) {
    return $.ajax({
        url: "/teams/" + slug,
        dataType: "json"
    });
}

function layoutTeamHierarchy(data) {

    var teamHeirarchyElem = $('#team-heirarchy-list');
    teamHeirarchyElem.empty();
    var htmlToAppend = "";
    data.forEach(function (x) {
        htmlToAppend += "<li class=\"list-group-item no-border\"><a href=\"/dashboard/" + x.slug + "\"><span>" + x.name + "</span></a>";
        htmlToAppend += layoutChild(x.slug, x.children);
        htmlToAppend += ("</li>");
    });
    teamHeirarchyElem.append(htmlToAppend);
    feather.replace();

}

function layoutChild(slug, children) {
    if (!children) {
        return "";
    }
    var html = "<ul class=\"list-group list-group-hierarchy no-border\">";

    children.forEach(function (child) {
        if (child.slug !== slug) {

            html += "<li class=\"list-group-item no-border\"><a href=\"/dashboard/" + child.slug + "\"><span data-feather=\"corner-down-right\"></span>" + child.slug + "</a></li>";
            html += layoutChild(child.slug, child.children, html);
            html += "</li>";
        }
    });

    html += "</ul>";
    return html;
}

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