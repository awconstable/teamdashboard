package team.dashboard.web.dora.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.services.IncidentService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IncidentController.class)
class IncidentControllerTest
    {

    @Autowired private MockMvc mockMvc;
    @MockBean private IncidentService incidentService;

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
    void listIncidentsForApplication() throws Exception
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        when(incidentService.list(appId)).thenReturn(getIncidents(Date.from(reportingDate.toInstant())));

        MvcResult result = mockMvc.perform(get("/incs/app1/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        verify(incidentService, times(1)).list("app1");
        assertThat(content, is(equalTo("[{\"incidentId\":\"id1\",\"incidentDesc\":\"Incident 1\",\"applicationId\":\"app1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"resolved\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"test\",\"mttrSeconds\":0,\"mttrPerfLevel\":null},{\"incidentId\":\"id2\",\"incidentDesc\":\"Incident 2\",\"applicationId\":\"app2\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"resolved\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"test\",\"mttrSeconds\":0,\"mttrPerfLevel\":null},{\"incidentId\":\"id3\",\"incidentDesc\":\"Incident 3\",\"applicationId\":\"app3\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"resolved\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"test\",\"mttrSeconds\":0,\"mttrPerfLevel\":null}]")));
        }
    }