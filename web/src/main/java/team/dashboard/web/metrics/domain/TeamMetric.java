package team.dashboard.web.metrics.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

public class TeamMetric
    {
    @ApiModelProperty(notes = "The team ID")
    private final String teamId;
    @ApiModelProperty(notes = "The team metric type")
    private final TeamMetricType teamMetricType;
    @ApiModelProperty(notes = "The metric value")
    private final Double value;
    @ApiModelProperty(notes = "The metric target")
    private final Double target;
    @ApiModelProperty(notes = "The metric reporting date")
    private final LocalDate date;
    @Id
    @ApiModelProperty(notes = "The database generated metric ID")
    private String id;

    public TeamMetric(String teamId, TeamMetricType teamMetricType, Double value, Double target, LocalDate date)
        {
        this.teamId = teamId;
        this.teamMetricType = teamMetricType;
        this.value = value;
        this.target = target;
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

    public Double getTarget()
        {
        return target;
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
                ", target=" + target +
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
                Objects.equals(target, that.target) &&
                Objects.equals(date, that.date) &&
                Objects.equals(id, that.id);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, teamMetricType, value, target, date, id);
        }
    }