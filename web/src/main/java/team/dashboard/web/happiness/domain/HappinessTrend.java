package team.dashboard.web.happiness.domain;

import java.time.LocalDate;

public class HappinessTrend
    {
    
    private final String teamId;
    private final Double avgHappinessRating;
    private final Integer responseCount;
    private final LocalDate trendDate;

    public HappinessTrend(String teamId, Double avgHappinessRating, Integer responseCount, LocalDate trendDate) {
        this.teamId = teamId;
        this.avgHappinessRating = avgHappinessRating;
        this.responseCount = responseCount;
        this.trendDate = trendDate;
    }

    public String getTeamId() {
        return teamId;
    }

    public Double getAvgHappinessRating() {
        return avgHappinessRating;
    }

    public Integer getResponseCount() {
        return responseCount;
    }

    public LocalDate getTrendDate() {
        return trendDate;
    }

    @Override
    public String toString() {
        return "HappinessTrend{" +
                "teamId='" + teamId + '\'' +
                ", avgHappinessRating=" + avgHappinessRating +
                ", responseCount=" + responseCount +
                ", trendDate=" + trendDate +
                '}';
    }
}
