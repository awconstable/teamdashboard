package team.dashboard.web.collection;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.options.elements.Fill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamMetricRepository;
import team.dashboard.web.metrics.TeamMetricsController;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/collection-stats")
public class CollectionStatsController
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamRestRepository teamRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    @Autowired
    public CollectionStatsController(TeamMetricRepository teamMetricRepository, TeamRestRepository teamRepository, TeamCollectionReportRepository teamCollectionReportRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.teamRepository = teamRepository;
        this.teamCollectionReportRepository = teamCollectionReportRepository;
        }

    private double calculatePercentage(int totalTeams, int collectingTeams)
        {
        if (totalTeams == 0 || collectingTeams == 0)
            {
            return 0;
            } else
            {
            return ((double) collectingTeams * 100 / (double) totalTeams);
            }
        }

    private void createCollectionStats(String teamId, Integer year, Integer month)
        {

        int teamCount;
        int collectingTeamCount;
        double percentageTeamsCollectingMetrics;

        Team team = teamRepository.findByTeamSlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        teamCount = teams.size();

        Set<TeamCollectionStat> stats = teamMetricRepository.getCollectionStats(teams.toArray(new String[]{}), year, month);

        collectingTeamCount = stats.size();

        percentageTeamsCollectingMetrics = calculatePercentage(teams.size(), stats.size());

        for (String teamStat : teams)
            {
            TeamCollectionStat stat = new TeamCollectionStat(teamStat, 0, year, month);
            stats.add(stat);
            }

        LocalDate reportingDate = LocalDate.of(year, month, 1);
        TeamCollectionId id = new TeamCollectionId(teamId, reportingDate, ReportingPeriod.MONTH);

        Optional<TeamCollectionReport> existingReport = teamCollectionReportRepository.findById(id);

        if (existingReport.isPresent())
            {
            teamCollectionReportRepository.deleteById(id);
            }

        TeamCollectionReport report = new TeamCollectionReport(id, teamId, reportingDate, ReportingPeriod.MONTH, teamCount, collectingTeamCount, percentageTeamsCollectingMetrics, stats);

        teamCollectionReportRepository.save(report);

        }

    @GetMapping("/generate")
    @ResponseBody
    public String generateLast12MonthsCollectionReporting()
        {

        List<Team> teams = teamRepository.findAllTeams();

        for (Team team : teams)
            {
            int m = 0;
            while (m < 12)
                {
                LocalDate date = LocalDate.now().minusMonths(m);
                createCollectionStats(team.getSlug(), date.getYear(), date.getMonth().getValue());
                m++;
                }
            }
        return "OK";
        }


    @GetMapping("/{teamId}/")
    @ResponseBody
    public String chartCollectionStats(@PathVariable String teamId)
        {
        ArrayList<String> labels = new ArrayList<>();
        LinkedHashMap<String, Integer> teamCollectionStats = new LinkedHashMap<>();
        ArrayList<LineDataset> datasets = new ArrayList<>();

        Team team = teamRepository.findByTeamSlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        List<TeamCollectionStat> stats = teamMetricRepository.getMonthlyCollectionStats(teams.toArray(new String[]{}));

        for (TeamCollectionStat stat : stats)
            {
            String label = TeamMetricsController.createDataPointLabel(stat.getYear(), stat.getMonth());
            if (!labels.contains(label))
                {
                labels.add(label);
                }
            teamCollectionStats.put(stat.getTeamId() + label, stat.getCount());
            }

        for (String teamId2 : teams)
            {
            Color lineColour = Color.random();
            LineDataset dataset = new LineDataset().setLabel(teamId2);
            dataset.setFill(new Fill(false));
            dataset.setBackgroundColor(Color.TRANSPARENT);
            dataset.setBorderColor(lineColour);
            dataset.setBorderWidth(4);
            ArrayList<Color> pointsColors = new ArrayList<>();
            pointsColors.add(lineColour);
            dataset.setPointBackgroundColor(pointsColors);
            dataset.setYAxisID("y-axis-1");

            for (String label : labels)
                {
                dataset.addData(teamCollectionStats.getOrDefault(teamId2 + label, 0));
                }
            datasets.add(dataset);
            }

        for (TeamCollectionStat stat : stats)
            {
            String label = TeamMetricsController.createDataPointLabel(stat.getYear(), stat.getMonth());
            teamCollectionStats.put(stat.getTeamId() + label, stat.getCount());
            }

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
