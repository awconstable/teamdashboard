package team.dashboard.web.metrics.domain;

import org.springframework.data.annotation.Id;

public class TeamMetricTrend
    {
    @Id
    private final TeamMetricTrendId teamMetricTrendId;
    private final Double avg;
    private final Double sum;
    private final Integer count;
    
    public TeamMetricTrend(TeamMetricTrendId teamMetricTrendId, Double avg, Double sum, Integer count)
        {
        this.teamMetricTrendId = teamMetricTrendId;
        this.avg = avg;
        this.sum = sum;
        this.count = count;
        }

    public TeamMetricTrendId getTeamMetricTrendId() { return teamMetricTrendId; }

    public Double getAvg()
        {
        return avg;
        }

    public Double getSum()
        {
        return sum;
        }

    public Integer getCount()
        {
        return count;
        }
    
    @Override
    public String toString()
        {
        return "TeamMetricTrend{" +
            "teamMetricTrendId=" + teamMetricTrendId +
            ", avg=" + avg +
            ", sum=" + sum +
            ", count=" + count +
            '}';
        }
    }
