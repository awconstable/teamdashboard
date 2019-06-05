package team.dashboard.web.collection;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.BarData;
import be.ceau.chart.dataset.BarDataset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.metrics.TeamMetricsController;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/collection-stats")
public class CollectionStatsController
    {

    private final TeamRestRepository teamRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    private final TeamCollectionReportService teamCollectionReportService;

    @Autowired
    public CollectionStatsController(TeamRestRepository teamRepository, TeamCollectionReportRepository teamCollectionReportRepository, TeamCollectionReportService teamCollectionReportService)
        {
        this.teamRepository = teamRepository;
        this.teamCollectionReportRepository = teamCollectionReportRepository;
        this.teamCollectionReportService = teamCollectionReportService;
        }

    @GetMapping("/generate")
    @ResponseBody
    public String generateLast12MonthsCollectionReporting()
        {

        teamCollectionReportService.generateLast12MonthsCollectionReporting();

        return "OK";
        }


    @GetMapping("/{teamId}/")
    @ResponseBody
    public String chartCollectionStats(@PathVariable String teamId)
        {
        LinkedHashSet<String> labels = new LinkedHashSet<>();
        LinkedHashMap<String, Double> teamCollectionStats = new LinkedHashMap<>();
        ArrayList<BarDataset> datasets = new ArrayList<>();

        TeamRelation team = teamRepository.findTeamHierarchyBySlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        List<TeamCollectionReport> reports = teamCollectionReportRepository.findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(teams, ReportingPeriod.MONTH, LocalDate.now().minusMonths(12).atStartOfDay(), LocalDate.now().atStartOfDay());

        for (TeamCollectionReport report : reports)
            {
            String label = TeamMetricsController.createDataPointLabel(report.getReportingDate().getYear(), report.getReportingDate().getMonth().getValue());
            labels.add(label);
            teamCollectionStats.put(report.getTeamId() + label, report.getChildPercentageTeamsCollectingMetrics());
            }

        for (String teamId2 : teams)
            {
            Color lineColour = Color.random();
            BarDataset dataset = new BarDataset().setLabel(teamId2);
            dataset.setBackgroundColor(lineColour);
            dataset.setBorderColor(lineColour);
            dataset.setBorderWidth(1);
            dataset.setYAxisID("y-axis-1");

            for (String label : labels)
                {
                dataset.addData(teamCollectionStats.getOrDefault(teamId2 + label, 0.0));
                }
            datasets.add(dataset);
            }

        BarData data = new BarData()
                .addLabels(labels.toArray(new String[]{}));
        datasets.forEach(data::addDataset);

        ObjectWriter writer = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .forType(BarData.class);

        try
            {
            return writer.writeValueAsString(data);
            } catch (JsonProcessingException e)
            {
            throw new RuntimeException(e);
            }
        }

    }
