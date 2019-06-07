package team.dashboard.web.metrics;

public class Metric
    {
    private final String teamMetricType;
    private final Double value;

    public Metric(String teamMetricType, Double value)
        {
        this.teamMetricType = teamMetricType;
        this.value = value;
        }

    public String getTeamMetricType()
        {
        return teamMetricType;
        }

    public Double getValue()
        {
        return value;
        }

    @Override
    public String toString()
        {
        return "Metric{" +
                "teamMetricType='" + teamMetricType + '\'' +
                ", value=" + value +
                '}';
        }
    }
