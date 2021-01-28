package team.dashboard.web.dora.services;

import java.util.Date;

public interface LeadTimeService
    {
        void load(String applicationId, Date reportingDate);

        void loadAll(Date reportingDate);
        
        void delete(String applicationId, Date reportDate);
    }
