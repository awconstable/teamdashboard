package team.dashboard.web.collection.domain;

import java.time.LocalDate;
import java.util.Objects;

public class TeamCollectionId
    {
    private final String teamId;
    private final LocalDate reportingDate;
    private final ReportingPeriod reportingPeriod;

    public TeamCollectionId(String teamId, LocalDate reportingDate, ReportingPeriod reportingPeriod)
        {
        this.teamId = teamId;
        this.reportingDate = reportingDate;
        this.reportingPeriod = reportingPeriod;
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

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCollectionId that = (TeamCollectionId) o;
        return teamId.equals(that.teamId) &&
                reportingDate.equals(that.reportingDate) &&
                reportingPeriod == that.reportingPeriod;
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, reportingDate, reportingPeriod);
        }

    @Override
    public String toString()
        {
        return "TeamCollectionId{" +
                "teamId='" + teamId + '\'' +
                ", reportingDate=" + reportingDate +
                ", reportingPeriod=" + reportingPeriod +
                '}';
        }
    }
