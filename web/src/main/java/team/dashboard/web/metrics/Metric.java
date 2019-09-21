package team.dashboard.web.metrics;

import io.swagger.annotations.ApiModelProperty;

public class Metric
    {
    @ApiModelProperty(notes = "The team metric type")
    private final String teamMetricType;
    @ApiModelProperty(notes = "The metric value")
    private final Double value;
    @ApiModelProperty(notes = "The metric target")
    private final Double target;

    public Metric(String teamMetricType, Double value, Double target)
        {
        this.teamMetricType = teamMetricType;
        this.value = value;
        this.target = target;
        }

    public String getTeamMetricType()
        {
        return teamMetricType;
        }

    public Double getValue()
        {
        return value;
        }

    public Double getTarget()
        {
        return target;
        }

    @Override
    public String toString()
        {
        return "Metric{" +
                "teamMetricType='" + teamMetricType + '\'' +
                ", value=" + value +
                ", target=" + target +
                '}';
        }
    }
