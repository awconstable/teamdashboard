package team.dashboard.web.metrics.repos;

import team.dashboard.web.metrics.domain.TeamCollectionStat;
import team.dashboard.web.metrics.domain.TeamMetricTrend;
import team.dashboard.web.metrics.domain.TeamMetricType;

import java.util.List;
import java.util.Set;

public interface TeamMetricAggregationRepository
    {
    List<TeamMetricTrend> getDailyChildMetrics(String[] slugs, TeamMetricType metricType);

    Set<TeamCollectionStat> getCollectionStats(String[] slugs, Integer year, Integer month);
    }
