package team.dashboard.web.dora.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.repos.IncidentClient;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;

import java.util.*;

@Service
public class IncidentServiceImpl implements IncidentService
    {
    private static final Logger log = LoggerFactory.getLogger(IncidentServiceImpl.class);
    
    private final IncidentClient incidentClient;
    private final HierarchyClient hierarchyClient;

    @Autowired
    public IncidentServiceImpl(IncidentClient incidentClient, HierarchyClient hierarchyClient)
        {
        this.incidentClient = incidentClient;
        this.hierarchyClient = hierarchyClient;
        }

    @Override
    public List<Incident> list(String applicationId)
        {
        List<Incident> incidents = new ArrayList<>();
        Set<String> teams = new HashSet<>();
        HierarchyEntity team = hierarchyClient.findEntityBySlug(applicationId);
        if(team == null){
            return incidents;
        }
        teams.add(team.getSlug());
        team.getChildren().forEach(child -> teams.add(child.getSlug()));
        log.info("List all incidents for applications with ids {}", teams);
        teams.forEach(t -> incidents.addAll(incidentClient.listForApplication(t)));
        incidents.sort(Comparator.comparing(Incident::getResolved).reversed());
        return incidents;
        }
    }
