package team.dashboard.web.dora.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.dora.repos.DORADeployFreqRepository;
import team.dashboard.web.dora.repos.DORALeadTimeRepository;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.LeadTimeService;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DORAController.class)
class DORAControllerTest
    {

    @Autowired private MockMvc mockMvc;
    @MockBean private DeploymentFrequencyService deploymentFrequencyService;
    @MockBean private LeadTimeService leadTimeService;
    @MockBean private DeploymentClient deploymentClient;
    @MockBean private HierarchyClient hierarchyClient;
    @MockBean private TeamMetricService teamMetricService;
    @MockBean private DORADeployFreqRepository doraDeployFreqRepository;
    @MockBean private DORALeadTimeRepository doraLeadTimeRepository;

    @Test
    void testLoadLeadTime() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        
        mockMvc.perform(get("/dora/load/lead_time/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(leadTimeService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }

    @Test
    void testLoadLeadTimeWithReportingDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/dora/load/lead_time/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(leadTimeService, times(1)).loadAll(Date.from(reportingDate.toInstant()));

        }

    @Test
    void testLoadDeployFreq() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));

        mockMvc.perform(get("/dora/load/deployment/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deploymentFrequencyService, times(1)).loadAll(Date.from(reportingDate.toInstant()));

        }

    @Test
    void testLoadDeployFreqWithReportingDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/dora/load/deployment/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deploymentFrequencyService, times(1)).loadAll(Date.from(reportingDate.toInstant()));

        }
    }