package team.dashboard.web.metrics.repos;

import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamMetricTrend;
import team.dashboard.web.metrics.TeamMetricType;

import java.util.List;
import java.util.Set;

public interface TeamMetricAggregationRepository
    {

    List<TeamMetricTrend> getMonthlyMetrics(String slug, TeamMetricType metricType);

    List<TeamMetricTrend> getMonthlyChildMetrics(String[] slugs, TeamMetricType metricType);

    List<TeamCollectionStat> getMonthlyCollectionStats(String[] slugs);

    Set<TeamCollectionStat> getCollectionStats(String[] slugs, Integer year, Integer month);
    }
