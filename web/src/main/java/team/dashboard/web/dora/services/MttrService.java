package team.dashboard.web.dora.services;

import team.dashboard.web.dora.domain.MTTR;

import java.util.Date;
import java.util.Optional;

public interface MttrService
    {
    Optional<MTTR> get(String applicationId, Date reportingDate);

    void load(String applicationId, Date reportingDate);

    void loadAll(Date reportingDate);

    void delete(String applicationId, Date reportDate);
    }
