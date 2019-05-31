package team.dashboard.web.metrics;

import java.util.Objects;

public class TeamCollectionStat
    {
    private final String teamId;
    private final Integer count;
    private final Integer year;
    private final Integer month;

    public TeamCollectionStat(String teamId, Integer count, Integer year, Integer month)
        {
        this.teamId = teamId;
        this.count = count;
        this.year = year;
        this.month = month;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getCount()
        {
        return count;
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
    public String toString()
        {
        return "TeamCollectionStat{" +
                "teamId='" + teamId + '\'' +
                ", count=" + count +
                ", year=" + year +
                ", month=" + month +
                '}';
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCollectionStat that = (TeamCollectionStat) o;
        return teamId.equals(that.teamId) &&
                year.equals(that.year) &&
                month.equals(that.month);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, year, month);
        }
    }
