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
import team.dashboard.web.dora.domain.*;
import team.dashboard.web.dora.repos.DORADeployFreqRepository;
import team.dashboard.web.dora.repos.DORALeadTimeRepository;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.DeploymentService;
import team.dashboard.web.dora.services.LeadTimeService;
import team.dashboard.web.dora.services.MttrService;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
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
    @MockBean private DeploymentService deploymentService;
    @MockBean private MttrService mttrService;

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
        ZonedDateTime reportingDate = LocalDate.now().minusDays(10).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/dora/load/lead_time/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(leadTimeService, times(1)).loadAll(Date.from(reportingDate.toInstant()));

        }

    @Test
    void testLoadMttr() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));

        mockMvc.perform(get("/dora/load/mttr/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(mttrService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }

    @Test
    void testLoadMttrWithReportingDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(10).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/dora/load/mttr/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(mttrService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }
    
    @Test
    void testLoadDeployFreq() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));

        mockMvc.perform(get("/dora/load/deployment/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deploymentFrequencyService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        verify(deploymentService, times(1)).loadAll(Date.from(reportingDate.toInstant()));

        }

    @Test
    void testLoadDeployFreqWithReportingDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(10).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/dora/load/deployment/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deploymentFrequencyService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        verify(deploymentService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }
    
    @Test
    void testGetTeamPerformance() throws Exception
        {
        String appId = "a1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        DeploymentFrequency fr = new DeploymentFrequency(appId, Date.from(reportingDate.toInstant()), 10, TimePeriod.DAY, DORALevel.ELITE);
        when(deploymentFrequencyService.get(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(fr));
        LeadTime lt = new LeadTime(appId, Date.from(reportingDate.toInstant()), 120, DORALevel.ELITE);
        when(leadTimeService.get(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(lt));
        MTTR mttr = new MTTR(appId, java.util.Date.from(reportingDate.toInstant()), 1200, 4, DORALevel.ELITE);
        when(mttrService.get(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(mttr));
        
        MvcResult result = mockMvc.perform(get("/dora/team/" + appId + "/performance").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);
        assertThat(content, is(equalTo("{\"reportingDate\":\"" + dateOut + "\",\"deploymentFrequency\":{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"deploymentCount\":10,\"timePeriod\":\"DAY\",\"deployFreqLevel\":\"ELITE\"},\"leadTime\":{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"leadTimeSeconds\":120,\"leadTimePerfLevel\":\"ELITE\"},\"mttr\":{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"meanTimeToRecoverSeconds\":1200,\"incidentCount\":4,\"doraLevel\":\"ELITE\"}}")));
        verify(deploymentFrequencyService, times(1)).get(appId, Date.from(reportingDate.toInstant()));
        verify(leadTimeService, times(1)).get(appId, Date.from(reportingDate.toInstant()));
        verify(mttrService, times(1)).get(appId, Date.from(reportingDate.toInstant()));
        }
    }