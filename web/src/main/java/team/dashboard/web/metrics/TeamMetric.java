package team.dashboard.web.metrics;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class TeamMetric
    {

    private final TeamMetricType teamMetricType;
    private final String teamId;
    private final Integer value;
    private final LocalDate date;
    @Id
    private String id;

    public TeamMetric(String teamId, TeamMetricType teamMetricType, Integer value, LocalDate date)
        {
        this.teamMetricType = teamMetricType;
        this.teamId = teamId;
        this.value = value;
        this.date = date;
        }

    public String getId()
        {
        return id;
        }

    public TeamMetricType getTeamMetricType()
        {
        return teamMetricType;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getValue()
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
        return "TeamMetric{" +
                "id='" + id + '\'' +
                ", teamMetricType=" + teamMetricType +
                ", teamId='" + teamId + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
        }
    }
