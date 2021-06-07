package team.dashboard.web.metrics.controllers;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.options.elements.Fill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.domain.Relation;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetric;
import team.dashboard.web.metrics.domain.TeamMetricTrend;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/metrics", produces = "application/json")
@ApiIgnore
public class TeamMetricsController
    {
    @Value("${display.previous.months:6}")
    private int displayPreviousMonths;
    
    private final TeamMetricRepository teamMetricRepository;

    private final HierarchyClient hierarchyRestRepository;

    @Autowired
    public TeamMetricsController(TeamMetricRepository teamMetricRepository, HierarchyClient hierarchyRestRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.hierarchyRestRepository = hierarchyRestRepository;
        }

    public static String createDataPointLabel(int year, int month, int day)
        {

        LocalDateTime dateTime = LocalDate.of(year, month, day).atStartOfDay();

        return ZonedDateTime.of(dateTime, ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        }

    @GetMapping("/{metricType}/{teamId}/{reportingDate}")
    @ResponseStatus(HttpStatus.OK)
    public TeamMetric getmetric(@PathVariable String metricType, @PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate)
        {
        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, reportingDate);

        return metric.orElse(null);
        }

    @GetMapping("/{metricType}/{teamId}/")
    public String chartMetricTrend(@PathVariable String metricType, @PathVariable String teamId) throws Exception
        {

        TeamMetricType type = TeamMetricType.get(metricType);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Double> metricData = new ArrayList<>();

        HierarchyEntity team = hierarchyRestRepository.findEntityBySlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (Relation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }
        
        List<TeamMetricTrend> metrics = teamMetricRepository.getDailyChildMetrics(teams.toArray(new String[]{}), type, displayPreviousMonths);

        Color lineColour = Color.random();

        for (TeamMetricTrend metric : metrics)
            {
            lineColour = metric.getTeamMetricTrendId().getTeamMetricType().getGraphColour();

            Double value;

            if (TeamMetricType.AggMethod.AVG.equals(metric.getTeamMetricTrendId().getTeamMetricType().getMethod()))
                {
                value = metric.getAvg();
                } else if (TeamMetricType.AggMethod.SUM.equals(metric.getTeamMetricTrendId().getTeamMetricType().getMethod()))
                {
                value = metric.getSum();
                } else
                {
                throw new Exception("Unknown AggMethod type");
                }

            metricData.add(value);
            labels.add(createDataPointLabel(metric.getTeamMetricTrendId().getYear(), metric.getTeamMetricTrendId().getMonth(), metric.getTeamMetricTrendId().getDay()));
            }

        //Metric data
        LineDataset dataset = new LineDataset().setLabel(type != null ? type.getName() : "Metric");
        metricData.forEach(dataset::addData);
        dataset.setFill(new Fill(false));
        dataset.setBackgroundColor(lineColour);
        dataset.setBorderColor(lineColour);
        dataset.setBorderWidth(2);
        ArrayList<Color> pointsColors = new ArrayList<>();
        pointsColors.add(lineColour);
        dataset.setPointBackgroundColor(pointsColors);
        dataset.addPointRadius(2);
        dataset.setYAxisID("y-axis-1");

        LineData data = new LineData()
                .addLabels(labels.toArray(new String[]{}))
                .addDataset(dataset);

        ObjectWriter writer = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .forType(LineData.class);

        try
            {
            return writer.writeValueAsString(data);
            } catch (JsonProcessingException e)
            {
            throw new RuntimeException(e);
            }
        }


    }