package team.dashboard.web.dora.services;

import java.util.Date;

public interface DeploymentFrequencyService
    {
        void load(String applicationId, Date reportingDate);

        void loadAll(Date reportingDate);
        
        void delete(String applicationId, Date reportDate);
    }
