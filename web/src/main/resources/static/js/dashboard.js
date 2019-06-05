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

    var forms = $('.needs-validation');

    Array.prototype.filter.call(forms, function (form) {
        form.addEventListener('submit', function (event) {
            if (form.checkValidity() === false) {
                event.preventDefault();
                event.stopPropagation()
            }
            form.classList.add('was-validated')
        }, false)
    });

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
    } else if (mode && mode === 'collection') {
        selectCollection(false, team);
    } else if (mode && mode === 'capture') {
        selectCapture(false, team);
    } else {
        selectTeamExplorer(false);
    }
}

function loadTeams() {
    loadTeamHierarchy().done(layoutTeamHierarchy);
}

function loadCollectionGraphs() {
    loadCollectionData(team)
        .done(function (data) {
            drawBarChart(data, "#collection-chart1", "Collection Report", "% of teams collecting data")
        });
}

function loadGraphs() {
    //throughput metrics
    loadTrendData("/lead_time/", team)
        .done(function (data) {
            drawChart(data, "#chart1", "Average Lead Time", "Days", "Metric Count")
        });
    loadTrendData("/deployment_frequency/", team)
        .done(function (data) {
            drawChart(data, "#chart2", "Deployment Frequency", "Days", "Metric Count")
        });
    //stability metrics
    loadTrendData("/change_failure_rate/", team)
        .done(function (data) {
            drawChart(data, "#chart3", "Change Failure Rate", "%", "Metric Count")
        });
    loadTrendData("/mttr/", team)
        .done(function (data) {
            drawChart(data, "#chart4", "Mean Time to Recovery", "Minutes", "Metric Count")
        });
    loadTrendData("/cycletime/", team)
        .done(function (data) {
            drawChart(data, "#chart5", "Average Cycle Time", "Days", "Metric Count")
        });
    loadTrendData("/incidents_due_to_change/", team)
        .done(function (data) {
            drawChart(data, "#chart6", "Incidents Due To Change", "Incidents", "Metric Count")
        });
    loadTrendData("/production_defects/", team)
        .done(function (data) {
            drawChart(data, "#chart7", "Production Defects", "Defects", "Metric Count")
        });
    //culture
    loadTrendData("/team_happiness/", team)
        .done(function (data) {
            drawChart(data, "#chart8", "Team Happiness", "Happiness Index", "Metric Count")
        });
    loadTrendData("/customer_satisfaction/", team)
        .done(function (data) {
            drawChart(data, "#chart9", "Customer Satisfaction", "CSAT Index", "Metric Count")
        });
    loadTrendData("/batch_size/", team)
        .done(function (data) {
            drawChart(data, "#chart10", "Batch Size", "Batch Size", "Metric Count")
        });
    loadTrendData("/test_automation_coverage/", team)
        .done(function (data) {
            drawChart(data, "#chart11", "Test Automation Coverage", "%", "Metric Count")
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
    submissionMessage();
    return false;

});

function submissionMessage() {
    $("<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
        "                                  Metrics Successfully Submitted\n" +
        "                                  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "                                      <span aria-hidden=\"true\">&times;</span>\n" +
        "                                  </button>\n" +
        "                              </div>").insertAfter("#alert");
}

var teamExplorerLinkElem = $("#teamexplorer-link");
var dashboardLinkElem = $("#dashboard-link");
var collectionLinkElem = $("#collection-link");
var captureLinkElem = $("#capture-link");

var teamExplorerContentElem = $("#teamexplorer-content");
var dashboardContentElem = $("#dashboard-content");
var collectionContentElem = $("#collection-content");
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

collectionLinkElem.click(function () {
    if (team) {
        selectCollection(true, team);
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
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-none").addClass("d-block");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
}

function selectDashboard(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Team Dashboard - ' + slug}, null, '/dashboard/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Team Dashboard - ' + slug}, null, '/dashboard/' + slug);
    }

    loadTeam(slug).done(updateTeamName);
    loadGraphs();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.addClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-none").addClass("d-block");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
}

function selectCollection(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Collection Dashboard - ' + slug}, null, '/collection/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Collection Dashboard - ' + slug}, null, '/collection/' + slug);
    }

    loadTeam(slug).done(updateTeamName);
    loadCollectionGraphs();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.addClass("active");
    captureLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-none").addClass("d-block");
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
    collectionLinkElem.removeClass("active");
    captureLinkElem.addClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
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

$("#collection-refresh-button").click(function () {
    loadCollectionGraphs();
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

function sortTeams(teams) {
    teams.sort(function (a, b) {
        return a.name.localeCompare(b.name);
    });
}

function layoutTeamHierarchy(data) {

    var teamHeirarchyElem = $('#team-heirarchy-list');
    teamHeirarchyElem.empty();
    var htmlToAppend = "";

    sortTeams(data);
    
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

    sortTeams(children);

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

function loadCollectionData(slug) {
    return $.ajax({
        url: "/collection-stats/" + slug + "/",
        dataType: "json"
    });
}

function getBarChartConfig(data, title, yAxisLabel1) {
    return {
        type: 'bar',
        data: data,
        options: {
            title: {
                display: true,
                text: title
            },
            legend: {
                display: true
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                intersect: false
            },
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel1
                    },
                    ticks: {
                        beginAtZero: true,
                        max: 100
                    },
                    id: "y-axis-1"
                }],
                xAxes: [{
                    type: 'time',
                    time: {
                        unit: 'month',
                        tooltipFormat: 'MMM YYYY'
                    },
                    bounds: "data",
                    ticks: {
                        source: 'labels'
                    },
                    gridLines: {
                        offsetGridLines: true
                    },
                    offset: true
                }]
            }
        }
    }
}

function getChartConfig(data, title, yAxisLabel1, yAxisLabel2) {
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
                        labelString: yAxisLabel1
                    },
                    type: "linear",
                    ticks: {
                        beginAtZero: true
                    },
                    position: "right",
                    id: "y-axis-1"
                }, {
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel2
                    },
                    type: "linear",
                    ticks: {
                        beginAtZero: true,
                        precision: 0
                    },
                    position: "left",
                    id: "y-axis-2",
                    gridLines: {
                        drawOnChartArea: false
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

function drawBarChart(data, chartElemId, title, yAxisLabel1) {
    var ctx = $(chartElemId);

    new Chart(ctx, getBarChartConfig(data, title, yAxisLabel1));
}

function drawChart(data, chartElemId, title, yAxisLabel1, yAxisLabel2) {
    var ctx = $(chartElemId);

    new Chart(ctx, getChartConfig(data, title, yAxisLabel1, yAxisLabel2));
}