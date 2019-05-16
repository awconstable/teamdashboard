package team.dashboard.web.metrics;

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
    }
