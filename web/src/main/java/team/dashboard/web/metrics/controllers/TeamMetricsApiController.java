package team.dashboard.web.metrics.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import team.dashboard.web.metrics.Metric;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/metrics", produces = "application/json")
@Api(value = "teammetrics", description = "Operations to manage team metrics")
public class TeamMetricsApiController
    {

    private final TeamMetricServiceImpl teamMetricServiceImpl;

    @Autowired
    public TeamMetricsApiController(TeamMetricServiceImpl teamMetricServiceImpl)
        {
        this.teamMetricServiceImpl = teamMetricServiceImpl;
        }

    @PostMapping("/{teamId}/{reportingDate}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Submit a list of team metrics", notes = "Multiple team metrics can be submitted for a single team and reporting date", response = TeamMetric.class, responseContainer = "List")
    public List<TeamMetric> metricsingest(@ApiParam(value = "The team ID", required = true) @PathVariable String teamId, @ApiParam(value = "The reporting date in ISO Date format YYYY-MM-dd", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate, @ApiParam(value = "A list of metrics to submit. {teamMetricType = '<key>', value = '<value>'}", required = true) @RequestBody ArrayList<Metric> metrics)
        {
        ArrayList<TeamMetric> output = new ArrayList<>();
        for (Metric metric : metrics)
            {
            TeamMetric persistedMetric = teamMetricServiceImpl.save(metric.getTeamMetricType(), teamId, reportingDate, metric.getValue());
            output.add(persistedMetric);
            }
        return output;
        }

    @GetMapping("/{teamId}/{reportingDate}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "List team metrics for team id and reporting date combination", notes = "Get a list of team metrics for team id and reporting date combination", response = TeamMetric.class, responseContainer = "List")
    public List<TeamMetric> listMetrics(@ApiParam(value = "The team ID", required = true) @PathVariable String teamId, @ApiParam(value = "The reporting date in ISO Date format YYYY-MM-dd", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate)
        {
        return teamMetricServiceImpl.list(teamId, reportingDate);
        }

    @DeleteMapping("/{teamId}/{metricType}/{reportingDate}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a team metric for a metric type key and reporting date combination", notes = "Delete a single team metric for a metric type key and reporting date combination", response = TeamMetric.class)
    public Map<String, String> deleteMetric(@ApiParam(value = "The team ID", required = true) @PathVariable String teamId, @ApiParam(value = "The metric type key as described /metrics/metric_types", required = true) @PathVariable String metricType, @ApiParam(value = "The reporting date in ISO Date format YYYY-MM-dd", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate)
        {

        Map<String, String> responseBody = new HashMap<>();

        teamMetricServiceImpl.delete(metricType, teamId, reportingDate);

        responseBody.put("message", "Metric deleted");

        return responseBody;
        }

    @GetMapping("/metric_types")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "List all available metric types", notes = "List all available metric types", response = TeamMetricType.class, responseContainer = "List")
    public TeamMetricType[] getMetricTypes()
        {
        return teamMetricServiceImpl.listTypes();
        }

    }




