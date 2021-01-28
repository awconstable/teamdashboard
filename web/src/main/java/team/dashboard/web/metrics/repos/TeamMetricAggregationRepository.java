package team.dashboard.web.metrics.repos;

import team.dashboard.web.metrics.domain.TeamCollectionStat;
import team.dashboard.web.metrics.domain.TeamMetricTrend;
import team.dashboard.web.metrics.domain.TeamMetricType;

import java.util.List;
import java.util.Set;

public interface TeamMetricAggregationRepository
    {

    List<TeamMetricTrend> getMonthlyMetrics(String slug, TeamMetricType metricType);

    List<TeamMetricTrend> getMonthlyChildMetrics(String[] slugs, TeamMetricType metricType);

    List<TeamCollectionStat> getMonthlyCollectionStats(String[] slugs);

    Set<TeamCollectionStat> getCollectionStats(String[] slugs, Integer year, Integer month);
    }
