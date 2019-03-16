package team.dashboard.web.metrics;

import java.util.List;

public interface TeamMetricAggregationRepository
    {

    List<TeamMetricTrend> getWeeklyMetrics(String teamSlug, TeamMetricType metricType);

    List<TeamMetric> getWeeklyChildMetrics(String teamSlug, TeamMetricType metricType);

    }
