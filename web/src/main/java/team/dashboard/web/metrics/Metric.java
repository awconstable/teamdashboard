package team.dashboard.web.metrics;

import java.time.LocalDate;

public class Metric
    {
    private final String teamMetricType;
    private final Double value;
    private final LocalDate date;

    public Metric(String teamMetricType, Double value, LocalDate date)
        {
        this.teamMetricType = teamMetricType;
        this.value = value;
        this.date = date;
        }

    public String getTeamMetricType()
        {
        return teamMetricType;
        }

    public Double getValue()
        {
        return value;
        }

    public LocalDate getDate()
        {
        return date;
        }

    @Override
    public String toString()
        {
        return "Metric{" +
                "teamMetricType='" + teamMetricType + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
        }
    }
