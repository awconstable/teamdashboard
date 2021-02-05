package team.dashboard.web.dora.services;

import java.util.Date;

public interface DeploymentService
    {
    void load(String applicationId, Date reportingDate);

    void loadAll(Date reportingDate);
    }
