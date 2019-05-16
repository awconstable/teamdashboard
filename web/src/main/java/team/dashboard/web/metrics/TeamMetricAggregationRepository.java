package team.dashboard.web.metrics;

import java.util.List;

public interface TeamMetricAggregationRepository
    {

    List<TeamMetricTrend> getMonthlyMetrics(String slug, TeamMetricType metricType);

    List<TeamMetricTrend> getMonthlyChildMetrics(String[] slugs, TeamMetricType metricType);

    List<TeamCollectionStat> getMonthlyCollectionStats(String[] slugs);
    }
