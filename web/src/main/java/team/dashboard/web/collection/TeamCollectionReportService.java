package team.dashboard.web.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.hierarchy.HierarchyClient;
import team.dashboard.web.hierarchy.HierarchyEntity;
import team.dashboard.web.hierarchy.Relation;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamCollectionStatId;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamCollectionReportService
    {

    private final TeamMetricRepository teamMetricRepository;

    private final HierarchyClient hierarchyRestRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    @Autowired
    public TeamCollectionReportService(TeamMetricRepository teamMetricRepository, HierarchyClient hierarchyRestRepository, TeamCollectionReportRepository teamCollectionReportRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.hierarchyRestRepository = hierarchyRestRepository;
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

    public void updateCollectionStats(String teamId, Integer year, Integer month)
        {
        Set<String> teamids = new HashSet<>();
        HierarchyEntity team = hierarchyRestRepository.findEntityBySlug(teamId);

        teamids.add(teamId);

        for (Relation child : team.getChildren())
            {
            teamids.add(child.getSlug());
            }
        for (Relation ancestor : team.getAncestors())
            {
            teamids.add(ancestor.getSlug());
            }

        for (String teamid : teamids)
            {
            System.out.println(teamId);
            createCollectionStats(teamid, year, month);
            }
        }

    void generateLast12MonthsCollectionReporting()
        {

        List<HierarchyEntity> teams = hierarchyRestRepository.findAll();

        for (HierarchyEntity team : teams)
            {
            int m = 0;
            while (m < 12)
                {
                LocalDate date = LocalDate.now().minusMonths(m);
                createCollectionStats(team.getSlug(), date.getYear(), date.getMonth().getValue());
                m++;
                }
            }
        }

    private void createCollectionStats(String teamId, Integer year, Integer month)
        {

        int teamCount;
        int collectingTeamCount;
        double percentageTeamsCollectingMetrics;

        HierarchyEntity team = hierarchyRestRepository.findEntityBySlug(teamId);

        Set<String> teams = new HashSet<>();
        teams.add(teamId);

        for (Relation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        teamCount = teams.size();
        
        System.out.println(teams);

        Set<TeamCollectionStat> stats = teamMetricRepository.getCollectionStats(teams.toArray(new String[]{}), year, month);

        collectingTeamCount = stats.size();

        percentageTeamsCollectingMetrics = calculatePercentage(teams.size(), stats.size());

        for (String teamStat : teams)
            {
            TeamCollectionStat stat = new TeamCollectionStat(new TeamCollectionStatId(teamStat, year, month), 0);
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
    }
