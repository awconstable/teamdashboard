package team.dashboard.web.dora.services;

import team.dashboard.web.dora.domain.DeploymentFrequency;

import java.util.Date;
import java.util.Optional;

public interface DeploymentFrequencyService
    {
        Optional<DeploymentFrequency> get(String applicationId, Date reportingDate);
        
        void load(String applicationId, Date reportingDate);

        void loadAll(Date reportingDate);
        
        void delete(String applicationId, Date reportDate);
    }
