package team.dashboard.web.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamMetricRepository;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamCollectionReportService
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamRestRepository teamRepository;

    private final TeamCollectionReportRepository teamCollectionReportRepository;

    @Autowired
    public TeamCollectionReportService(TeamMetricRepository teamMetricRepository, TeamRestRepository teamRepository, TeamCollectionReportRepository teamCollectionReportRepository)
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

    public void updateCollectionStats(String teamId, Integer year, Integer month)
        {
        Set<String> teamids = new HashSet<String>();
        Team team = teamRepository.findByTeamSlug(teamId);

        teamids.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teamids.add(child.getSlug());
            }
        for (TeamRelation ancestor : team.getAncestors())
            {
            teamids.add(ancestor.getSlug());
            }

        for (String teamid : teamids)
            {
            createCollectionStats(teamid, year, month);
            }
        }

    public void generateLast12MonthsCollectionReporting()
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
        }

    private void createCollectionStats(String teamId, Integer year, Integer month)
        {

        int teamCount;
        int collectingTeamCount;
        double percentageTeamsCollectingMetrics;

        Team team = teamRepository.findByTeamSlug(teamId);

        Set<String> teams = new HashSet<>();
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
    }
