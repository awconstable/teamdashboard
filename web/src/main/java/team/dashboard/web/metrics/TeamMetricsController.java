package team.dashboard.web.metrics;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/metrics")
public class TeamMetricsController
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamRestRepository teamRepository;

    @Autowired
    public TeamMetricsController(TeamMetricRepository teamMetricRepository, TeamRestRepository teamRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.teamRepository = teamRepository;
        }

    @GetMapping("/{metricType}/{teamId}/{date}/{value}")
    @ResponseBody
    public TeamMetric metricingest(@PathVariable String metricType, @PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable Double value, HttpServletResponse response)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, date);

        if (metric.isPresent())
            {
            teamMetricRepository.deleteById(metric.get().getId());
            }

        TeamMetric newMetric = new TeamMetric(teamId, type, value, date);
        teamMetricRepository.save(newMetric);

        return newMetric;

        }

    @GetMapping("/{metricType}/{teamId}/{date}")
    @ResponseBody
    public TeamMetric getmetric(@PathVariable String metricType, @PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpServletResponse response)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, date);

        if (metric.isPresent())
            {
            return metric.get();
            }

        return null;
        }


    @GetMapping("/{metricType}/{teamId}/")
    @ResponseBody
    public String chartMetricTrend(Model model, @PathVariable String metricType, @PathVariable String teamId) throws Exception
        {

        TeamMetricType type = TeamMetricType.get(metricType);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Double> metricData = new ArrayList<>();
        ArrayList<Integer> metricCount = new ArrayList<>();

        Team team = teamRepository.findByTeamSlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        List<TeamMetricTrend> metrics = teamMetricRepository.getMonthlyChildMetrics(teams.toArray(new String[]{}), type);

        Color lineColour = Color.random();

        for (TeamMetricTrend metric : metrics)
            {

            lineColour = metric.getTeamMetricType().getGraphColour();

            metricCount.add(metric.getCount());

            Double value;

            if (TeamMetricType.AggMethod.AVG.equals(metric.getTeamMetricType().getMethod()))
                {
                value = metric.getAvg();
                } else if (TeamMetricType.AggMethod.SUM.equals(metric.getTeamMetricType().getMethod()))
                {
                value = metric.getSum();
                } else
                {
                throw new Exception("Unknown AggMethod type");
                }

            metricData.add(value);
            labels.add(createDataPointLabel(metric.getYear(), metric.getMonth()));
            }

        //Metric data
        LineDataset dataset = new LineDataset().setLabel(type.getName());
        metricData.forEach(dataset::addData);
        dataset.setFill(false);
        dataset.setBackgroundColor(Color.TRANSPARENT);
        dataset.setBorderColor(lineColour);
        dataset.setBorderWidth(4);
        ArrayList<Color> pointsColors = new ArrayList<>();
        pointsColors.add(lineColour);
        dataset.setPointBackgroundColor(pointsColors);
        dataset.setYAxisID("y-axis-1");

        //Count data
        LineDataset countDataset = new LineDataset().setLabel(type.getName() + " Count");
        metricCount.forEach(countDataset::addData);
        countDataset.setFill(false);
        countDataset.setBackgroundColor(Color.TRANSPARENT);
        countDataset.setBorderColor(Color.GRAY);
        countDataset.setBorderWidth(1);
        ArrayList<Color> countPointsColors = new ArrayList<>();
        pointsColors.add(Color.GRAY);
        countDataset.setPointBackgroundColor(countPointsColors);
        countDataset.setBorderDash(new ArrayList<>(Arrays.asList(5, 5)));
        countDataset.setYAxisID("y-axis-2");

        LineData data = new LineData()
                .addLabels(labels.toArray(new String[]{}))
                .addDataset(dataset)
                .addDataset(countDataset);

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

    public static String createDataPointLabel(int year, int month)
        {

        LocalDateTime date = LocalDateTime.of(year, month, 1, 0, 0);

        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.of("Europe/London"));

        return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
        }

    }