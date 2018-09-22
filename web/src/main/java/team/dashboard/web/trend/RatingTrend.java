package team.dashboard.web.trend;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class RatingTrend {

    @Id
    private String id;
    private final String teamId;
    private final Double avgRating;
    private final Integer responseCount;
    private final LocalDate trendDate;

    public RatingTrend(String teamId, Double avgRating, Integer responseCount, LocalDate trendDate) {
        this.teamId = teamId;
        this.avgRating = avgRating;
        this.responseCount = responseCount;
        this.trendDate = trendDate;
    }

    public String getTeamId() {
        return teamId;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public Integer getResponseCount() {
        return responseCount;
    }

    public LocalDate getTrendDate() {
        return trendDate;
    }

    @Override
    public String toString() {
        return "RatingTrend{" +
                "id='" + id + '\'' +
                ", teamId='" + teamId + '\'' +
                ", avgRating=" + avgRating +
                ", responseCount=" + responseCount +
                ", trendDate=" + trendDate +
                '}';
    }
}
