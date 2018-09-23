package team.dashboard.web.metrics;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class CycleTime
    {
    @Id
    private String id;
    private final String teamId;
    private final Integer cycleTime;
    private final LocalDate date;

    public CycleTime(String teamId, Integer cycleTime, LocalDate date)
        {
        this.teamId = teamId;
        this.cycleTime = cycleTime;
        this.date = date;
        }

    public String getId()
        {
        return id;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getCycleTime()
        {
        return cycleTime;
        }

    public LocalDate getDate()
        {
        return date;
        }

    @Override
    public String toString()
        {
        return "CycleTime{" +
                "teamId='" + teamId + '\'' +
                ", cycleTime=" + cycleTime +
                ", date=" + date +
                '}';
        }
    }
