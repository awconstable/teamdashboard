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
    //throughput metrics
    loadTrendData("/lead_time/", team)
        .done(function (data) {
            drawChart(data, "#chart1", "Average Lead Time", "Days")
        });
    loadTrendData("/cycletime/", team)
        .done(function (data) {
            drawChart(data, "#chart2", "Average Cycle Time", "Days")
        });
    loadTrendData("/deployment_frequency/", team)
        .done(function (data) {
            drawChart(data, "#chart3", "Deployment Frequency", "Days")
        });
    //stability metrics
    loadTrendData("/change_failure_rate/", team)
        .done(function (data) {
            drawChart(data, "#chart4", "Change Failure Rate", "%age")
        });
    loadTrendData("/mttr/", team)
        .done(function (data) {
            drawChart(data, "#chart5", "Mean Time to Recovery", "Minutes")
        });
    loadTrendData("/incidents_due_to_change/", team)
        .done(function (data) {
            drawChart(data, "#chart6", "Incidents Due To Change", "Incidents")
        });
    loadTrendData("/production_defects/", team)
        .done(function (data) {
            drawChart(data, "#chart7", "Production Defects", "Defects")
        });
    //culture
    loadTrendData("/team_happiness/", team)
        .done(function (data) {
            drawChart(data, "#chart8", "Team Happiness", "Happiness Index")
        });
    loadTrendData("/customer_satisfaction/", team)
        .done(function (data) {
            drawChart(data, "#chart9", "Customer Satisfaction", "CSAT Index")
        });
    loadTrendData("/batch_size/", team)
        .done(function (data) {
            drawChart(data, "#chart10", "Batch Size", "Batch Size")
        });
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

            html += "<li class=\"list-group-item no-border\"><a href=\"/dashboard/" + child.slug + "\"><span data-feather=\"corner-down-right\"></span>" + child.name + "</a></li>";
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

function drawChart(data, chartElemId, title, yAxisLabel) {
    var ctx = $(chartElemId);

    new Chart(ctx, getChartConfig(data, title, yAxisLabel));
}