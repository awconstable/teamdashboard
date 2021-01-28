package team.dashboard.web.metrics.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class TeamCollectionStatId
    {
    private final String teamId;
    private final Integer year;
    private final Integer month;

    public TeamCollectionStatId(String teamId, Integer year, Integer month)
        {
        this.teamId = teamId;
        this.year = year;
        this.month = month;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getYear()
        {
        return year;
        }

    public Integer getMonth()
        {
        return month;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCollectionStatId that = (TeamCollectionStatId) o;
        return Objects.equals(teamId, that.teamId) &&
            Objects.equals(year, that.year) &&
            Objects.equals(month, that.month);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, year, month);
        }

    @Override
    public String toString()
        {
        return "TeamCollectionId{" +
            "teamId='" + teamId + '\'' +
            ", year=" + year +
            ", month=" + month +
            '}';
        }
    }
