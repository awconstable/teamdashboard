package team.dashboard.web.metrics;

public class TeamMetricTrend
    {
    private final TeamMetricType teamMetricType;
    private final String teamId;
    private final Double avg;
    private final Double sum;
    private final Integer count;
    private final Integer year;
    private final Integer month;

    public TeamMetricTrend(TeamMetricType teamMetricType, String teamId, Double avg, Double sum, Integer count, Integer year, Integer month)
        {
        this.teamMetricType = teamMetricType;
        this.teamId = teamId;
        this.avg = avg;
        this.sum = sum;
        this.count = count;
        this.year = year;
        this.month = month;
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
        return "TeamMetricTrend{" +
                "teamMetricType=" + teamMetricType +
                ", teamId='" + teamId + '\'' +
                ", avg=" + avg +
                ", sum=" + sum +
                ", count=" + count +
                ", year=" + year +
                ", month=" + month +
                '}';
        }
    }
