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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class IncidentServiceImplTest
    {

    @TestConfiguration
    static class IncidentServiceTestContextConfiguration
        {
        @MockBean private IncidentClient mockIncidentClient;

        @Bean
        public IncidentService incidentService()
            {
            return new IncidentServiceImpl(
                mockIncidentClient
            );
            }
        }
    @Autowired private IncidentService incidentService;
    @Autowired private IncidentClient mockIncidentClient;
    
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
        when(mockIncidentClient.listForHierarchy(appId)).thenReturn(getIncidents(new Date()));

        List<Incident> incidents = incidentService.list(appId);

        verify(mockIncidentClient, times(1)).listForHierarchy(appId);
        assertThat(incidents.size(), is(equalTo(3)));
        }
    
    }