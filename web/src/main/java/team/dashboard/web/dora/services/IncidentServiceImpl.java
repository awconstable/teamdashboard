package team.dashboard.web.dora.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.repos.IncidentClient;

import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService
    {
    private static final Logger log = LoggerFactory.getLogger(IncidentServiceImpl.class);
    
    private final IncidentClient incidentClient;

    @Autowired
    public IncidentServiceImpl(IncidentClient incidentClient)
        {
        this.incidentClient = incidentClient;
        }

    @Override
    public List<Incident> list(String applicationId)
        {
        log.info("List all incidents for hierarchy starting at application with id {}", applicationId);
        return incidentClient.listForHierarchy(applicationId);
        }
    }
