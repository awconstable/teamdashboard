package team.dashboard.web.collection;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.BarData;
import be.ceau.chart.dataset.BarDataset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dashboard.web.hierarchy.HierarchyRestRepository;
import team.dashboard.web.hierarchy.Relation;
import team.dashboard.web.metrics.controllers.TeamMetricsController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping(value = "/collection-stats", produces = "application/json")
public class CollectionStatsController
    {

    private final HierarchyRestRepository hierarchyRestRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    private final TeamCollectionReportService teamCollectionReportService;

    @Autowired
    public CollectionStatsController(HierarchyRestRepository hierarchyRestRepository, TeamCollectionReportRepository teamCollectionReportRepository, TeamCollectionReportService teamCollectionReportService)
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
        LinkedHashMap<String, Double> teamCollectionStats = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> teamCountStats = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> teamCollectionCountStats = new LinkedHashMap<>();
        ArrayList<BarDataset> datasets = new ArrayList<>();

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
            String label = TeamMetricsController.createDataPointLabel(report.getReportingDate().getYear(), report.getReportingDate().getMonth().getValue());
            labels.add(label);
            teamCollectionStats.put(report.getTeamId() + label, report.getChildPercentageTeamsCollectingMetrics());
            teamCountStats.put(report.getTeamId() + label, report.getChildTeamCount());
            teamCollectionCountStats.put(report.getTeamId() + label, report.getChildTeamsCollectingMetrics());
            }
        int i = 0;
        for (Relation teamId2 : teams)
            {
            Color barColor = ColorHelper.generateSteppedColor(i, new Color(71, 143, 255));
            BarDataset dataset = new BarDataset().setLabel(teamId2.getName());
            dataset.setBackgroundColor(barColor);
            dataset.setBorderColor(Color.LIGHT_CYAN);
            dataset.setBorderWidth(1);
            dataset.setYAxisID("y-axis-1");


            for (String label : labels)
                {
                dataset.addData(teamCollectionStats.getOrDefault(teamId2.getSlug() + label, 0.0));
                }
            datasets.add(dataset);
            i++;
            }

        Color lineColour = Color.BLACK;
        BarDataset teamCountDataSet = new BarDataset().setLabel("Total Team Count");
        teamCountDataSet.setBackgroundColor(lineColour);
        teamCountDataSet.setBackgroundColor(Color.TRANSPARENT);
        teamCountDataSet.setBorderColor(lineColour);
        teamCountDataSet.setBorderWidth(1);
        teamCountDataSet.setYAxisID("y-axis-2");
        for (String label : labels)
            {
            teamCountDataSet.addData(teamCountStats.getOrDefault(teamId + label, 0));
            }
        datasets.add(teamCountDataSet);

        lineColour = Color.DARK_ORANGE;
        BarDataset teamCollectionCountDataSet = new BarDataset().setLabel("Team Collection Count");
        teamCollectionCountDataSet.setBackgroundColor(lineColour);
        teamCollectionCountDataSet.setBackgroundColor(Color.TRANSPARENT);
        teamCollectionCountDataSet.setBorderColor(lineColour);
        teamCollectionCountDataSet.setBorderWidth(2);
        teamCollectionCountDataSet.setYAxisID("y-axis-2");
        for (String label : labels)
            {
            teamCollectionCountDataSet.addData(teamCollectionCountStats.getOrDefault(teamId + label, 0));
            }
        datasets.add(teamCollectionCountDataSet);

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
