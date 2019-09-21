package team.dashboard.web.metrics.services;

import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;

import java.time.LocalDate;
import java.util.List;

public interface TeamMetricService
    {
    TeamMetric save(String metricType, String teamId, LocalDate reportingDate, Double value, Double target);

    void delete(String metricType, String teamId, LocalDate reportingDate);

    List<TeamMetric> list(String teamId, LocalDate reportingDate);

    TeamMetricType[] listTypes();
    }
