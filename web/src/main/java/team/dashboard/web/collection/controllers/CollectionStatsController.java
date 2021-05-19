package team.dashboard.web.collection.controllers;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dashboard.web.collection.domain.ReportingPeriod;
import team.dashboard.web.collection.domain.TeamCollectionReport;
import team.dashboard.web.collection.repos.TeamCollectionReportRepository;
import team.dashboard.web.collection.services.TeamCollectionReportService;
import team.dashboard.web.hierarchy.domain.Relation;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.controllers.TeamMetricsController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/collection-stats", produces = "application/json")
public class CollectionStatsController
    {

    private final HierarchyClient hierarchyRestRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    private final TeamCollectionReportService teamCollectionReportService;

    @Autowired
    public CollectionStatsController(HierarchyClient hierarchyRestRepository, TeamCollectionReportRepository teamCollectionReportRepository, TeamCollectionReportService teamCollectionReportService)
        {
        this.hierarchyRestRepository = hierarchyRestRepository;
        this.teamCollectionReportRepository = teamCollectionReportRepository;
        this.teamCollectionReportService = teamCollectionReportService;
        }

    @GetMapping("/generate")
    public String generateLast12MonthsCollectionReporting()
        {

        teamCollectionReportService.generateLast12MonthsCollectionReporting();

        return "OK";
        }

    @GetMapping("/{teamId}")
    public String chartCollectionStats(@PathVariable String teamId)
        {
        LinkedHashSet<String> labels = new LinkedHashSet<>();
        LinkedHashMap<String, Integer> teamCountStats = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> teamCollectionCountStats = new LinkedHashMap<>();
        ArrayList<LineDataset> datasets = new ArrayList<>();

        Relation team = hierarchyRestRepository.findHierarchyBySlug(teamId);

        LinkedHashSet<Relation> teams = new LinkedHashSet<>();
        teams.add(team);

        teams.addAll(team.getChildren());

        ArrayList<String> teamIdList = new ArrayList<>();
        for (Relation teamIdForQuery : teams)
            {
            teamIdList.add(teamIdForQuery.getSlug());
            }

        List<TeamCollectionReport> reports = teamCollectionReportRepository.findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(teamIdList, ReportingPeriod.MONTH, LocalDate.now().minusMonths(12).atStartOfDay(), LocalDate.now().atStartOfDay());

        for (TeamCollectionReport report : reports)
            {
            String label = TeamMetricsController.createDataPointLabel(report.getReportingDate().getYear(), report.getReportingDate().getMonth().getValue(), report.getReportingDate().getDayOfMonth());
            labels.add(label);
            teamCountStats.put(report.getTeamId() + label, report.getChildTeamCount());
            teamCollectionCountStats.put(report.getTeamId() + label, report.getChildTeamsCollectingMetrics());
            }

        Color lineColour = Color.BLACK;
        LineDataset teamCountDataSet = new LineDataset().setLabel("Total Node Count");
        teamCountDataSet.setBackgroundColor(lineColour);
        teamCountDataSet.setBackgroundColor(Color.TRANSPARENT);
        teamCountDataSet.setBorderColor(lineColour);
        teamCountDataSet.setBorderWidth(1);
        teamCountDataSet.setYAxisID("y-axis-1");
        for (String label : labels)
            {
            teamCountDataSet.addData(teamCountStats.getOrDefault(teamId + label, 0));
            }
        datasets.add(teamCountDataSet);

        lineColour = Color.DARK_ORANGE;
        LineDataset teamCollectionCountDataSet = new LineDataset().setLabel("Node's Collecting Data");
        teamCollectionCountDataSet.setBackgroundColor(lineColour);
        teamCollectionCountDataSet.setBackgroundColor(Color.TRANSPARENT);
        teamCollectionCountDataSet.setBorderColor(lineColour);
        teamCollectionCountDataSet.setBorderWidth(2);
        teamCollectionCountDataSet.setYAxisID("y-axis-1");
        for (String label : labels)
            {
            teamCollectionCountDataSet.addData(teamCollectionCountStats.getOrDefault(teamId + label, 0));
            }
        datasets.add(teamCollectionCountDataSet);

        Collections.reverse(datasets);

        LineData data = new LineData()
                .addLabels(labels.toArray(new String[]{}));
        datasets.forEach(data::addDataset);

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
