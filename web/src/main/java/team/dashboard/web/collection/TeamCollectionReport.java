package team.dashboard.web.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import team.dashboard.web.metrics.TeamCollectionStat;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Document
public class TeamCollectionReport
    {
    @Id
    private final TeamCollectionId id;
    private final String teamId;

    private final LocalDate reportingDate;
    private final ReportingPeriod reportingPeriod;

    private final Integer childTeamCount;
    private final Integer childTeamsCollectingMetrics;
    private final Double childPercentageTeamsCollectingMetrics;
    private final Set<TeamCollectionStat> teamStats;

    public TeamCollectionReport(TeamCollectionId id, String teamId, LocalDate reportingDate, ReportingPeriod reportingPeriod, Integer childTeamCount, Integer childTeamsCollectingMetrics, Double childPercentageTeamsCollectingMetrics, Set<TeamCollectionStat> teamStats)
        {
        this.id = id;
        this.teamId = teamId;
        this.reportingDate = reportingDate;
        this.reportingPeriod = reportingPeriod;
        this.childTeamCount = childTeamCount;
        this.childTeamsCollectingMetrics = childTeamsCollectingMetrics;
        this.childPercentageTeamsCollectingMetrics = childPercentageTeamsCollectingMetrics;
        this.teamStats = teamStats;
        }

    public TeamCollectionId getId()
        {
        return id;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public LocalDate getReportingDate()
        {
        return reportingDate;
        }

    public ReportingPeriod getReportingPeriod()
        {
        return reportingPeriod;
        }

    public Integer getChildTeamCount()
        {
        return childTeamCount;
        }

    public Integer getChildTeamsCollectingMetrics()
        {
        return childTeamsCollectingMetrics;
        }

    public Double getChildPercentageTeamsCollectingMetrics()
        {
        return childPercentageTeamsCollectingMetrics;
        }

    public Set<TeamCollectionStat> getTeamStats()
        {
        return teamStats;
        }

    @Override
    public String toString()
        {
        return "TeamCollectionReport{" +
                "id='" + id + '\'' +
                ", teamId='" + teamId + '\'' +
                ", reportingDate=" + reportingDate +
                ", reportingPeriod=" + reportingPeriod +
                ", childTeamCount=" + childTeamCount +
                ", childTeamsCollectingMetrics=" + childTeamsCollectingMetrics +
                ", childPercentageTeamsCollectingMetrics=" + childPercentageTeamsCollectingMetrics +
                ", teamStats=" + teamStats +
                '}';
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCollectionReport that = (TeamCollectionReport) o;
        return id.equals(that.id) &&
                teamId.equals(that.teamId) &&
                reportingDate.equals(that.reportingDate) &&
                reportingPeriod == that.reportingPeriod &&
                childTeamCount.equals(that.childTeamCount) &&
                childTeamsCollectingMetrics.equals(that.childTeamsCollectingMetrics) &&
                childPercentageTeamsCollectingMetrics.equals(that.childPercentageTeamsCollectingMetrics) &&
                teamStats.equals(that.teamStats);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(id, teamId, reportingDate, reportingPeriod, childTeamCount, childTeamsCollectingMetrics, childPercentageTeamsCollectingMetrics, teamStats);
        }
    }
