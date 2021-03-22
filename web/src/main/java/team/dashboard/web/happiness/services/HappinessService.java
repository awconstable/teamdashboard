package team.dashboard.web.happiness.services;

import team.dashboard.web.happiness.domain.HappinessTrend;

import java.util.Date;
import java.util.Optional;

public interface HappinessService
    {
    Optional<HappinessTrend> get(String applicationId, Date reportingDate);

    void load(String applicationId, Date reportingDate);

    void loadAll(Date reportingDate);

    void delete(String applicationId, Date reportDate);
    }
