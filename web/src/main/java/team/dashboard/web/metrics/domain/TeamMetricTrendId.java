package team.dashboard.web.metrics.domain;

public class TeamMetricTrendId
    {
    private final TeamMetricType teamMetricType;
    private final Integer year;
    private final Integer month;

    public TeamMetricTrendId(TeamMetricType teamMetricType, Integer year, Integer month)
        {
        this.teamMetricType = teamMetricType;
        this.year = year;
        this.month = month;
        }

    public TeamMetricType getTeamMetricType()
        {
        return teamMetricType;
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
        return "TeamMetricTrendId{" +
            "teamMetricType=" + teamMetricType +
            ", year=" + year +
            ", month=" + month +
            '}';
        }
    }
