package team.dashboard.web.dora.services;

import team.dashboard.web.dora.domain.Incident;

import java.util.List;

public interface IncidentService
    {
    List<Incident> list(String applicationId);
    }
