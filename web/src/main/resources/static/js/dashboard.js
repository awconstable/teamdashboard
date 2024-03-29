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
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
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
    } else if (mode && mode === 'deployments') {
        selectDeployments(false, team);
    } else if (mode && mode === 'incidents') {
        selectIncidents(false, team);
    } else if (mode && mode === 'changerequests') {
        selectChangeRequests(false, team);
    } else {
        selectTeamExplorer(false);
    }
}

function loadTeams() {
    loadTeamHierarchy().done(layoutTeamHierarchy);
}

var collectionChart;

function loadCollectionGraphs() {
    loadCollectionData(team)
        .done(function (data) {
            clearDownChart(collectionChart);
            collectionChart = drawBarChart(data, "#collection-chart1", "Collection Report", "Node Count", {beginAtZero: true, precision: 0});
        });
}

function loadDeployments(){
    loadDeploymentData(team)
        .done(function (data){
            drawDeploymentTable(data);
        });
}

function loadIncidents(){
    loadIncidentData(team)
        .done(function (data){
            drawIncidentTable(data);
        });
}

function loadChangeRequests(){
    loadChangeRequestData(team)
        .done(function (data){
            drawChangeRequestTable(data);
        });
}

var chart0;
var chart1;
var chart2;
var chart3;
var chart4;
var chart8;
var chart9;
var chart10;
var chart11;
var chart12;
var chart13;

function clearDownChart(chart){
    if(chart) {
        console.log("destroy chart id: " + chart.id);
        chart.destroy();
    }  
}

function getRGBColour(colourHex){
    var color = Chart.helpers.color;
    return color(colourHex).alpha(0.5).rgbString();
}

function getRGBColourForPerfLevel(perfLevel){
    switch(perfLevel){
        case 0:
            return getRGBColour('#9d9d9d');
        case 1:
            return getRGBColour('#FF5733');
        case 2:
            return getRGBColour('#ff9233');
        case 3:
            return getRGBColour('#ffcc33');
        case 4:
            return getRGBColour('#70ff33');
    } 
}

function getClassForPerfLevel(perfLevel){
    switch(perfLevel){
        case 0:
            return "dora-unknown-perf";
        case 1:
            return "dora-low-perf";
        case 2:
            return "dora-med-perf";
        case 3:
            return "dora-high-perf";
        case 4:
            return "dora-elite-perf";
    }
}

function getLevelValue(doraLevel){
    switch(doraLevel){
        case "LOW":
            return 1;
        case "MEDIUM":
            return 2;
        case "HIGH":
            return 3;
        case "ELITE":
            return 4;
        default:
            return 0;
    }
}

function getLeadTimeValue(leadTimeSeconds) {
    if(leadTimeSeconds === null){
        return "unknown";
    }
    return moment.duration(leadTimeSeconds, 'seconds').humanize();
}

function getLeadTimeTxtValue(doraLevel, leadTimeSeconds) {
    let msg = "On average changes are deployed within ";
    let leadTime;
    switch(doraLevel){
        case "LOW":
            leadTime = Math.round(moment.duration(leadTimeSeconds, 'seconds').asMonths());
            return msg + leadTime + " month" + (leadTime > 1 ? "s" : "");
        case "MEDIUM":
            leadTime = Math.round(moment.duration(leadTimeSeconds, 'seconds').asWeeks());
            return msg + leadTime + " week" + (leadTime > 1 ? "s" : "");
        case "HIGH":
        case "ELITE":
            leadTime = Math.round(moment.duration(leadTimeSeconds, 'seconds').asHours());
            return msg + leadTime + " hour" + (leadTime > 1 ? "s" : "");
        default:
            return "No lead time data";
    }
}

function getTimePeriodValue(timePeriod){
    switch(timePeriod){
        case "YEAR":
            return "yearly";
        case "MONTH":
            return "monthly";
        case "WEEK":
            return "weekly";
        case "DAY":
            return "daily";
        default:
            return "unknown";
    }
}

function getDeployFreqTxtValue(timePeriod, deploymentCount){
    switch(timePeriod){
        case "YEAR":
            return deploymentCount + " deployment" + (deploymentCount > 1 ? "s" : "") + " in the last year";
        case "MONTH":
            return deploymentCount + " deployment" + (deploymentCount > 1 ? "s" : "") + " in the last month";
        case "WEEK":
            return deploymentCount + " deployment" + (deploymentCount > 1 ? "s" : "") + " in the last week";
        case "DAY":
            return deploymentCount + " deployment" + (deploymentCount > 1 ? "s" : "") + " in the last day";
        default:
            return "No deployment data";
    }
}

function getChangeFailureRateValue(failureRate) {
    if(failureRate === null){
        return "unknown";
    }
    return Math.round(failureRate * 100) + "%";
}

function getChangeFailureRateTxtValue(failureRate, changeCount) {
    if(failureRate === null){
        return "No change request data";
    }
    return getChangeFailureRateValue(failureRate) + " of the last " + changeCount + " change" + (changeCount > 1 ? "s" : "") + " failed";
}

function getMTTRValue(mttr) {
    if(mttr === null){
        return "unknown";
    }
    return moment.duration(mttr, 'seconds').humanize();
}

function getMTTRTxtValue(mttrSeconds, doraLevel) {
    let msg = "On average incidents are recovered within ";
    let mttr;
    switch(doraLevel){
        case "LOW":
            mttr = Math.round(moment.duration(mttrSeconds, 'seconds').asWeeks());
            return msg + mttr + " week" + (mttr > 1 ? "s" : "");
        case "MEDIUM":
        case "HIGH":
            mttr = Math.round(moment.duration(mttrSeconds, 'seconds').asHours());
            return msg + mttr + " hour" + (mttr > 1 ? "s" : "");
        case "ELITE":
            mttr = Math.round(moment.duration(mttrSeconds, 'seconds').asMinutes());
            return msg + mttr + " minute" + (mttr > 1 ? "s" : "");
        default:
            return "No incident data";
    }
}

function processTeamPerfData (data) {
    console.log(data);
    var dataOut = [];
    dataOut[0] = getLevelValue(data.deploymentFrequency ? data.deploymentFrequency.deployFreqLevel : null);  // Deployment frequency
    dataOut[1] = getLevelValue(data.leadTime ? data.leadTime.leadTimePerfLevel : null);  // Lead Time for changes
    dataOut[2] = getLevelValue(data.mttr ? data.mttr.doraLevel : null);  // Time to restore service
    dataOut[3] = getLevelValue(data.changeFailureRate ? data.changeFailureRate.doraLevel : null);  // Change Failure Rate
    var backColour = [];
    backColour[0] = getRGBColourForPerfLevel(dataOut[0]);
    backColour[1] = getRGBColourForPerfLevel(dataOut[1]);
    backColour[2] = getRGBColourForPerfLevel(dataOut[2]);
    backColour[3] = getRGBColourForPerfLevel(dataOut[3]);
    return {
        datasets: [{
            data: dataOut,
            backgroundColor: backColour,
            hoverBorderColor: getRGBColour('#6c6d6e'),
            borderAlign: "inner"
        }],
        labels: [
            'Deployment frequency',
            'Lead Time for changes',
            'Time to restore service',
            'Change Failure Rate'
        ]
    };
}

function removeDoraPerfClass(elem){
    elem.removeClass("dora-unknown-perf")
        .removeClass("dora-low-perf")
        .removeClass("dora-med-perf")
        .removeClass("dora-high-perf")
        .removeClass("dora-elite-perf"); 
}

function updateCurrentLeadTimeValues(data){
    let leadTimeElem = $("#current_lead_time");
    leadTimeElem.html(getLeadTimeValue(data.leadTime ? data.leadTime.leadTimeSeconds : null));
    removeDoraPerfClass(leadTimeElem);
    leadTimeElem.addClass(getClassForPerfLevel(getLevelValue(data.leadTime ? data.leadTime.leadTimePerfLevel : null)));
    $("#current_lead_time_txt").html(getLeadTimeTxtValue(data.leadTime ? data.leadTime.leadTimePerfLevel : null, data.leadTime ? data.leadTime.leadTimeSeconds : null));
}

function updateCurrentDeployFreqValues(data){
    let deployFreElem = $("#current_deploy_freq");
    deployFreElem.html(getTimePeriodValue(data.deploymentFrequency ? data.deploymentFrequency.timePeriod : null));
    removeDoraPerfClass(deployFreElem);
    deployFreElem.addClass(getClassForPerfLevel(getLevelValue(data.deploymentFrequency ? data.deploymentFrequency.deployFreqLevel : null)));
    $("#current_deploy_freq_txt").html(getDeployFreqTxtValue(data.deploymentFrequency ? data.deploymentFrequency.timePeriod : null, data.deploymentFrequency ? data.deploymentFrequency.deploymentCount : null));
}

function updateCurrentChangeFailureRateValues(data){
    let elem = $("#current_change_failure_rate");
    elem.html(getChangeFailureRateValue(data.changeFailureRate ? data.changeFailureRate.changeFailureRatePercent : null));
    removeDoraPerfClass(elem);
    elem.addClass(getClassForPerfLevel(getLevelValue(data.changeFailureRate ? data.changeFailureRate.doraLevel : null)));
    $("#current_change_failure_rate_txt").html(getChangeFailureRateTxtValue(data.changeFailureRate ? data.changeFailureRate.changeFailureRatePercent : null, data.changeFailureRate ? data.changeFailureRate.changeRequestCount : null));
}

function updateCurrentMTTRValues(data){
    let elem = $("#current_mttr");
    elem.html(getMTTRValue(data.mttr ? data.mttr.meanTimeToRecoverSeconds : null));
    removeDoraPerfClass(elem);
    elem.addClass(getClassForPerfLevel(getLevelValue(data.mttr ? data.mttr.doraLevel : null)));
    $("#current_mttr_txt").html(getMTTRTxtValue(data.mttr ? data.mttr.meanTimeToRecoverSeconds : null, data.mttr ? data.mttr.doraLevel : null));
}

function loadGraphs() {
    //team performance chart
    loadTeamPerformanceData(team)
        .done(function (data) {
            clearDownChart(chart0);
            chart0 = drawPolarChart(processTeamPerfData(data), "#chart0", "Team Performance");
            updateCurrentDeployFreqValues(data);
            updateCurrentLeadTimeValues(data);
            updateCurrentChangeFailureRateValues(data);
            updateCurrentMTTRValues(data);
        });
    //throughput metrics
    loadTrendData("/lead_time/", team)
        .done(function (data) {
            clearDownChart(chart1);
            chart1 = drawChart('line', data, "#chart1", "Average Lead Time", "Hours", {beginAtZero: true});
        });
    loadTrendData("/deployment_count/", team)
        .done(function (data) {
            clearDownChart(chart2);
            
            data.datasets[0].maxBarThickness = 4;
            data.datasets[0].minBarLength = 2;
            
            chart2 = drawChart( 'bar', data, "#chart2", "Deployment Count", "Deployments", {beginAtZero: true});
        });
    //stability metrics
    loadTrendData("/change_failure_rate/", team)
        .done(function (data) {
            clearDownChart(chart3);
            chart3 = drawChart('line', data, "#chart3", "Change Failure Rate", "%",
                {
                    beginAtZero: true,
                    max: 100,
                    stepSize: 10
            });
        });
    loadTrendData("/mttr/", team)
        .done(function (data) {
            clearDownChart(chart4);
            chart4 = drawChart('line', data, "#chart4", "Mean Time to Recovery", "Minutes", {beginAtZero: true});
        });
    loadTrendData("/team_happiness/", team)
        .done(function (data) {
            clearDownChart(chart8);
            chart8 = drawChart('line', data, "#chart8", "Team Happiness", "Happiness Index",
                {
                    beginAtZero: true,
                    max: 5,
                    stepSize: 1
                });
        });
    loadTrendData("/customer_satisfaction/", team)
        .done(function (data) {
            clearDownChart(chart9);
            chart9 = drawChart('line', data, "#chart9", "Customer Satisfaction", "CSAT Index",
                {
                    beginAtZero: true,
                    max: 5,
                    stepSize: 1
                });
        });
    loadTrendData("/batch_size/", team)
        .done(function (data) {
            clearDownChart(chart10);
            chart10 = drawChart('line', data, "#chart10", "Batch Size", "Batch Size", {beginAtZero: true, precision: 0});
        });
    loadTrendData("/test_automation_coverage/", team)
        .done(function (data) {
            clearDownChart(chart11);
            chart11 = drawChart('line', data, "#chart11", "Test Automation Coverage", "%",
                {
                    beginAtZero: true,
                    max: 100,
                    stepSize: 10
                });
        });
    loadTrendData("/total_test_execution_count/", team)
        .done(function (data) {
            clearDownChart(chart12);
            chart12 = drawChart('line', data, "#chart12", "Total Test Execution Count", "#", {beginAtZero: true, precision: 0});
        });
    loadTrendData("/test_automation_execution_count/", team)
        .done(function (data) {
            clearDownChart(chart13);
            chart13 = drawChart('line', data, "#chart13", "Test Automation Execution Count", "#", {beginAtZero: true, precision: 0});
        });
}

var frm = $('#metric_capture');

frm.submit(function (e) {

    e.preventDefault();

    var requestBody = [];

    $("#metric_capture input[type=text]").each(function () {
        if (this.name === 'date' || this.value === "") {
            console.log(this.name + ': skip field');
            return;
        }
        requestBody.push({
            "teamMetricType": this.name,
            "value": this.value
        });
    });

    console.log(requestBody);

    $.ajax({
        method: "POST",
        url: "/api/metrics/" + team + "/" + $("#date").val() + "/",
        data: JSON.stringify(requestBody),
        processData: false,
        contentType: "application/json",
        success: function () {
            console.log('Submission was successful');
        },
        error: function () {
            console.log('An error occurred');
        }
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
var deploymentLinkElem = $("#deployments-link");
var incidentLinkElem = $("#incidents-link");
var changeRequestLinkElem = $("#change-requests-link");

var teamExplorerContentElem = $("#teamexplorer-content");
var dashboardContentElem = $("#dashboard-content");
var doraPerformanceContentElem = $("#dora-performance-content");
var doraPerformanceContentHrElem = $("#dora-performance-content-hr");
var collectionContentElem = $("#collection-content");
var captureContentElem = $("#capture-content");
var deploymentContentElem = $("#deployments-content");
var incidentContentElem = $("#incidents-content");
var changeRequestContentElem = $("#change-requests-content");

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

deploymentLinkElem.click(function () {
    if (team) {
        selectDeployments(true, team);
    }
});

incidentLinkElem.click(function () {
    if (team) {
        selectIncidents(true, team);
    }
});

changeRequestLinkElem.click(function () {
    if (team) {
        selectChangeRequests(true, team);
    }
});

function selectTeamExplorer(updateHistory) {
    if (updateHistory) {
        history.pushState({"pageTitle": 'Team Explorer'}, null, '/teamexplorer/');
    } else {
        history.replaceState({"pageTitle": 'Team Explorer'}, null, '/teamexplorer/');
    }

    loadTeams();

    loadTeam(team).done(updateTeam);

    teamExplorerLinkElem.addClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-none").addClass("d-block");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectDashboard(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Team Dashboard - ' + slug}, null, '/dashboard/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Team Dashboard - ' + slug}, null, '/dashboard/' + slug);
    }

    loadTeam(slug).done(updateTeam);
    loadGraphs();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.addClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-none").addClass("d-block");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectCollection(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Collection Dashboard - ' + slug}, null, '/collection/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Collection Dashboard - ' + slug}, null, '/collection/' + slug);
    }

    loadTeam(slug).done(updateTeam);
    loadCollectionGraphs();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.addClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-none").addClass("d-block");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectCapture(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Capture - ' + slug}, null, '/capture/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Capture - ' + slug}, null, '/capture/' + slug);
    }

    loadTeam(slug).done(updateTeam);

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.addClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-none").addClass("d-block");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectDeployments(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Deployments - ' + slug}, null, '/deployments/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Deployments - ' + slug}, null, '/deployments/' + slug);
    }

    loadTeam(slug).done(updateTeam);
    loadDeployments();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.addClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-none").addClass("d-block");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectIncidents(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Incidents - ' + slug}, null, '/incidents/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Incidents - ' + slug}, null, '/incidents/' + slug);
    }

    loadTeam(slug).done(updateTeam);
    loadIncidents();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.addClass("active");
    changeRequestLinkElem.removeClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-none").addClass("d-block");
    changeRequestContentElem.removeClass("d-block").addClass("d-none");
}

function selectChangeRequests(updateHistory, slug) {

    if (updateHistory) {
        history.pushState({"pageTitle": 'Change Requests - ' + slug}, null, '/changerequests/' + slug);
    } else {
        history.replaceState({"pageTitle": 'Change Requests - ' + slug}, null, '/changerequests/' + slug);
    }

    loadTeam(slug).done(updateTeam);
    loadChangeRequests();

    teamExplorerLinkElem.removeClass("active");
    dashboardLinkElem.removeClass("active");
    collectionLinkElem.removeClass("active");
    captureLinkElem.removeClass("active");
    deploymentLinkElem.removeClass("active");
    incidentLinkElem.removeClass("active");
    changeRequestLinkElem.addClass("active");

    teamExplorerContentElem.removeClass("d-block").addClass("d-none");
    dashboardContentElem.removeClass("d-block").addClass("d-none");
    collectionContentElem.removeClass("d-block").addClass("d-none");
    captureContentElem.removeClass("d-block").addClass("d-none");
    deploymentContentElem.removeClass("d-block").addClass("d-none");
    incidentContentElem.removeClass("d-block").addClass("d-none");
    changeRequestContentElem.removeClass("d-none").addClass("d-block");
}

function updateTeam(data) {
    console.log(data);
    if (!data) {
        teamNameElem.text("");
    }
    teamNameElem.text(data.name);
    if(!data || (data.entityType.key !== "APPLICATION" && data.entityType.key !== "COMPONENT")){
        console.log("hide perf");
        doraPerformanceContentElem.removeClass("d-flex").addClass("d-none");
        doraPerformanceContentHrElem.removeClass("d-block").addClass("d-none");
    } else {
        console.log("show perf");
        doraPerformanceContentElem.removeClass("d-none").addClass("d-flex");
        doraPerformanceContentHrElem.removeClass("d-none").addClass("d-block");
    }
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

$("#deployment-refresh-button").click(function () {
    loadDeployments();
});

$("#incident-refresh-button").click(function () {
    loadIncidents();
});

$("#change-request-refresh-button").click(function () {
    loadChangeRequests();
});

function loadTeamHierarchy() {
    return $.ajax({
        url: "/hierarchy",
        dataType: "json"
    });
}

function loadTeam(slug) {
    return $.ajax({
        url: "/hierarchy/" + slug,
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
        htmlToAppend += "<li class=\"list-group-item no-border\"><a href=\"/dashboard/" + x.slug + "\"><span>" + x.name + " (" + x.entityType.name +")</span></a>";
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

            html += "<li class=\"list-group-item no-border\"><a href=\"/dashboard/" + child.slug + "\"><span data-feather=\"corner-down-right\"></span>" + child.name + " (" + child.entityType.name +")</a></li>";
            html += layoutChild(child.slug, child.children);
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

function loadDeploymentData(slug) {
    return $.ajax({
        url: "/deploys/" + slug + "/",
        dataType: "json"
    });
}

function loadIncidentData(slug) {
    return $.ajax({
        url: "/incs/" + slug + "/",
        dataType: "json"
    });
}

function loadChangeRequestData(slug) {
    return $.ajax({
        url: "/crs/" + slug + "/",
        dataType: "json"
    });
}

function loadTeamPerformanceData(slug) {
    return $.ajax({
        url: "/dora/team/" + slug + "/performance",
        dataType: "json"
    });
}

function getChartConfig(type, data, title, yAxisLabel1, ticks) {
    return {
        type: type,
        data: data,
        options: {
            title: {
                display: true,
                text: title
            },
            legend: {
                display: false
            },
            tooltips: {
                callbacks: {
                    label: function (tooltipItem, data) {
                        var label = data.datasets[tooltipItem.datasetIndex].label || '';

                        if (label) {
                            label += ': ';
                        }
                        label += Math.round(tooltipItem.yLabel * 100) / 100;
                        return label;
                    }
                }
            },
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel1
                    },
                    type: "linear",
                    ticks: ticks,
                    position: "left",
                    id: "y-axis-1"
                }],
                xAxes: [{
                    type: 'time',
                    time: {
                        unit: 'day',
                        tooltipFormat: 'DD MMM YYYY'
                    }
                }]
            }
        }
    };
}

function getPolarChartConfig(data, title) {
    return {
        data: data,
        options: {
            responsive: true,
            legend: {
                position: 'right',
            },
            title: {
                display: false,
                text: title
            },
            scale: {
                ticks: {
                    beginAtZero: true,
                    max: 4,
                    stepSize: 1,
                    display:false
                },
                reverse: false
            },
            startAngle: 90,
            animation: {
                animateRotate: false,
                animateScale: true
            },
            tooltips:{
                callbacks: {
                    label: function(tooltipItem, data) {
                        var label = data.labels[tooltipItem.index];
                        var perfLevel;
                        switch(tooltipItem.value){
                            case "1":
                                perfLevel = "Low";
                                break;
                            case "2":
                                perfLevel = "Medium";
                                break;
                            case "3":
                                perfLevel = "High";
                                break;
                            case "4":
                                perfLevel = "Elite";
                                break;
                            default:
                                perfLevel = "Unknown";
                        }
                        return label + " : " + perfLevel;
                    }
                }
            }
        }
    };
}

function drawPolarChart(data, chartElemId, title) {
    var ctx = $(chartElemId);
    return Chart.PolarArea(ctx, getPolarChartConfig(data, title));
}

function drawBarChart(data, chartElemId, title, yAxisLabel1, ticks) {
    var ctx = $(chartElemId);

    return new Chart(ctx, getChartConfig('line', data, title, yAxisLabel1, ticks));
}

function drawChart(type, data, chartElemId, title, yAxisLabel1, ticks) {
    var ctx = $(chartElemId);
    var config = getChartConfig(type, data, title, yAxisLabel1, ticks);

    if(type === 'bar'){
        config.options.scales.xAxes[0].offset = true;
        config.options.scales.yAxes[0].ticks.stepSize = 1;
    }
    return new Chart(ctx, config);
}

function drawDeploymentTable(data){
    var tableBodyElem = $('#deployment-table-body');
    tableBodyElem.empty();
    var html = '<thead>' +
        '<tr>' +
        '<th scope="col">Application Id</th>' +
        '<th scope="col">Deployment ID</th>' +
        '<th scope="col">Description</th>' +
        '<th scope="col">RFC ID</th>' +
        '<th scope="col">Deployment Time</th>' +
        '<th scope="col">Changes</th>' +
        '<th scope="col">Lead Time</th>' +
        '<th scope="col">Performance Level</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';

    jQuery.each(data, function (j, deployment) {
        html += '<tr>'
        + '<td scope="row">' + deployment.applicationId + '</td>'
        + '<td scope="row">' + deployment.deploymentId + '</td>'
        + '<td scope="row">' + deployment.deploymentDesc + '</td>'
        + '<td scope="row">' + deployment.rfcId + '</td>' 
        + '<td scope="row">' + moment(deployment.created).format('YYYY-MM-DD HH:mm') + ' (' + moment(deployment.created).fromNow() + ')</td>'    
        + '<td scope="row">' + deployment.changes.length + '</td>' 
        + '<td scope="row">' + moment.duration(deployment.leadTimeSeconds, 'seconds').humanize() + '</td>'
        + '<td scope="row">' + deployment.leadTimePerfLevel + '</td>';
        jQuery.each(deployment.changes, function (k, change) {
            console.log(change);
        });
        html += '</tr>';
    });

    html += '</tr></tbody>';

    tableBodyElem.append(html);
}

function drawIncidentTable(data){
    var tableBodyElem = $('#incident-table-body');
    tableBodyElem.empty();
    var html = '<thead>' +
        '<tr>' +
        '<th scope="col">Application Id</th>' +
        '<th scope="col">Incident ID</th>' +
        '<th scope="col">Description</th>' +
        '<th scope="col">Creation Time</th>' +
        '<th scope="col">Resolution Time</th>' +
        '<th scope="col">MTTR</th>' +
        '<th scope="col">Performance Level</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';

    jQuery.each(data, function (j, incident) {
        html += '<tr>'
            + '<td scope="row">' + incident.applicationId + '</td>'
            + '<td scope="row">' + incident.incidentId + '</td>'
            + '<td scope="row">' + incident.incidentDesc + '</td>'
            + '<td scope="row">' + moment(incident.created).format('YYYY-MM-DD HH:mm') + ' (' + moment(incident.created).fromNow() + ')</td>'
            + '<td scope="row">' + moment(incident.resolved).format('YYYY-MM-DD HH:mm') + ' (' + moment(incident.resolved).fromNow() + ')</td>'
            + '<td scope="row">' + moment.duration(incident.mttrSeconds, 'seconds').humanize() + '</td>'
            + '<td scope="row">' + incident.mttrPerfLevel + '</td>';
        html += '</tr>';
    });

    html += '</tr></tbody>';

    tableBodyElem.append(html);
}

function drawChangeRequestTable(data){
    var tableBodyElem = $('#change-request-table-body');
    tableBodyElem.empty();
    var html = '<thead>' +
        '<tr>' +
        '<th scope="col">Application Id</th>' +
        '<th scope="col">Change Request ID</th>' +
        '<th scope="col">Description</th>' +
        '<th scope="col">Creation Time</th>' +
        '<th scope="col">Start Time</th>' +
        '<th scope="col">Finish Time</th>' +
        '<th scope="col">Close Time</th>' +
        '<th scope="col">Failed</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';

    jQuery.each(data, function (j, cr) {
        html += '<tr>'
            + '<td scope="row">' + cr.applicationId + '</td>'
            + '<td scope="row">' + cr.changeRequestId + '</td>'
            + '<td scope="row">' + cr.description + '</td>'
            + '<td scope="row">' + moment(cr.created).format('YYYY-MM-DD HH:mm') + ' (' + moment(cr.created).fromNow() + ')</td>'
            + '<td scope="row">' + moment(cr.started).format('YYYY-MM-DD HH:mm') + ' (' + moment(cr.started).fromNow() + ')</td>'
            + '<td scope="row">' + moment(cr.finished).format('YYYY-MM-DD HH:mm') + ' (' + moment(cr.finished).fromNow() + ')</td>'
            + '<td scope="row">' + moment(cr.closed).format('YYYY-MM-DD HH:mm') + ' (' + moment(cr.closed).fromNow() + ')</td>'
            + '<td scope="row">' + cr.failed + '</td>';
        html += '</tr>';
    });

    html += '</tr></tbody>';

    tableBodyElem.append(html);
}