package team.dashboard.web.dora.services;

import team.dashboard.web.dora.domain.ChangeRequest;

import java.util.List;

public interface ChangeRequestService
    {
    List<ChangeRequest> list(String applicationId);
    }
