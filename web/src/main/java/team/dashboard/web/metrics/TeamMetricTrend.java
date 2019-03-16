package team.dashboard.web.metrics;

public class TeamMetricTrend
    {
    private final TeamMetricType teamMetricType;
    private final String teamId;
    private final Double avg;
    private final Double sum;
    private final Integer year;
    private final Integer week;

    public TeamMetricTrend(TeamMetricType teamMetricType, String teamId, Double avg, Double sum, Integer year, Integer week)
        {
        this.teamMetricType = teamMetricType;
        this.teamId = teamId;
        this.avg = avg;
        this.sum = sum;
        this.year = year;
        this.week = week;
        }

    public TeamMetricType getTeamMetricType()
        {
        return teamMetricType;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Double getAvg()
        {
        return avg;
        }

    public Double getSum()
        {
        return sum;
        }

    public Integer getYear()
        {
        return year;
        }

    public Integer getWeek()
        {
        return week;
        }

    @Override
    public String toString()
        {
        return "TeamMetricTrend{" +
                "teamMetricType=" + teamMetricType +
                ", teamId='" + teamId + '\'' +
                ", avg=" + avg +
                ", sum=" + sum +
                ", year=" + year +
                ", week=" + week +
                '}';
        }
    }
