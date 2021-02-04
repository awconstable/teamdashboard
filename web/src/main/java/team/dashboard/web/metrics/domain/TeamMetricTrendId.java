package team.dashboard.web.metrics.domain;

public class TeamMetricTrendId
    {
    private final TeamMetricType teamMetricType;
    private final Integer year;
    private final Integer month;
    private final Integer day;

    public TeamMetricTrendId(TeamMetricType teamMetricType, Integer year, Integer month, Integer day)
        {
        this.teamMetricType = teamMetricType;
        this.year = year;
        this.month = month;
        this.day = day;
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

    public Integer getDay() { return day; }

    @Override
    public String toString()
        {
        return "TeamMetricTrendId{" +
            "teamMetricType=" + teamMetricType +
            ", year=" + year +
            ", month=" + month +
            ", day=" + day +
            '}';
        }
    }
