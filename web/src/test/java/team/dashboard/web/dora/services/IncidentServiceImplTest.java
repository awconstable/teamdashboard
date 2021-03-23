package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.repos.IncidentClient;
import team.dashboard.web.hierarchy.domain.EntityType;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.domain.Relation;
import team.dashboard.web.hierarchy.repos.HierarchyClient;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class IncidentServiceImplTest
    {

    @TestConfiguration
    static class IncidentServiceTestContextConfiguration
        {
        @MockBean private IncidentClient mockIncidentClient;
        @MockBean private HierarchyClient mockHierarchyClient;

        @Bean
        public IncidentService incidentService()
            {
            return new IncidentServiceImpl(
                mockIncidentClient,
                mockHierarchyClient
            );
            }
        }
    @Autowired private IncidentService incidentService;
    @Autowired private IncidentClient mockIncidentClient;
    @Autowired private HierarchyClient mockHierarchyClient;

    private HierarchyEntity getTeam(String appId){
        Relation c1 = new Relation(appId + "c1", EntityType.TEAM, appId + "c1", appId, Collections.emptyList());
        Relation c2 = new Relation(appId + "c2", EntityType.TEAM, appId + "c2", appId, Collections.emptyList());
        Relation c3 = new Relation(appId + "c3", EntityType.TEAM, appId + "c3", appId, Collections.emptyList());

    return new HierarchyEntity(appId,
        EntityType.TEAM,
        appId,
        null,
        Collections.emptyList(),
        Arrays.asList(c1, c2, c3),
        Collections.emptyList(),
        Collections.emptyList());
    }

    private List<Incident> getIncidents(Date reportingDate)
        {
        Incident i1 = new Incident("id1", "Incident 1", "app1", reportingDate, reportingDate, "test");
        Incident i2 = new Incident("id2", "Incident 2", "app2", reportingDate, reportingDate, "test");
        Incident i3 = new Incident("id3", "Incident 3", "app3", reportingDate, reportingDate, "test");

        List<Incident> incidents = new ArrayList<>();
        incidents.add(i1);
        incidents.add(i2);
        incidents.add(i3);
        return incidents;
        }
    
    @Test
    void listIncidents()
        {
        String appId = "app1";
        when(mockHierarchyClient.findEntityBySlug(appId)).thenReturn(getTeam(appId));
        when(mockIncidentClient.listForApplication(anyString())).thenReturn(getIncidents(new Date()));

        List<Incident> incidents = incidentService.list(appId);

        verify(mockIncidentClient, times(4)).listForApplication(anyString());
        assertThat(incidents.size(), is(equalTo(12)));
        }

    @Test
    void listIncidentsWithoutHierarchy()
        {
        String appId = "app1";
        when(mockHierarchyClient.findEntityBySlug(appId)).thenReturn(null);
        when(mockIncidentClient.listForApplication(anyString())).thenReturn(getIncidents(new Date()));

        List<Incident> incidents = incidentService.list(appId);

        verify(mockIncidentClient, never()).listForApplication(anyString());
        assertThat(incidents.size(), is(equalTo(0)));
        }
    }