package team.dashboard.web.metrics;

import io.swagger.annotations.ApiModelProperty;

public class Metric
    {
    @ApiModelProperty(notes = "The team metric type")
    private final String teamMetricType;
    @ApiModelProperty(notes = "The metric value")
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
