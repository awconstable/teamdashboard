package team.dashboard.web.metrics;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

public class TeamMetric
    {
    private final String teamId;
    private final TeamMetricType teamMetricType;
    private final Double value;
    private final LocalDate date;
    @Id
    private String id;

    public TeamMetric(String teamId, TeamMetricType teamMetricType, Double value, LocalDate date)
        {
        this.teamId = teamId;
        this.teamMetricType = teamMetricType;
        this.value = value;
        this.date = date;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public TeamMetricType getTeamMetricType()
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

    public String getId()
        {
        return id;
        }

    @Override
    public String toString()
        {
        return "TeamMetric{" +
                "teamId='" + teamId + '\'' +
                ", teamMetricType=" + teamMetricType +
                ", value=" + value +
                ", date=" + date +
                ", id='" + id + '\'' +
                '}';
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMetric that = (TeamMetric) o;
        return Objects.equals(teamId, that.teamId) &&
                teamMetricType == that.teamMetricType &&
                Objects.equals(value, that.value) &&
                Objects.equals(date, that.date) &&
                Objects.equals(id, that.id);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, teamMetricType, value, date, id);
        }
    }
