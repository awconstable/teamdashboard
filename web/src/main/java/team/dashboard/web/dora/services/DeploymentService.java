package team.dashboard.web.dora.services;

import team.dashboard.web.dora.domain.Deployment;

import java.util.Date;
import java.util.List;

public interface DeploymentService
    {
    void load(String applicationId, Date reportingDate);

    void loadAll(Date reportingDate);

    List<Deployment> listDeployments(String applicationId);
    }
