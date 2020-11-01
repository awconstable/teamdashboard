package team.dashboard.web.metrics.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.hierarchy.HierarchyClient;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;
import team.dashboard.web.metrics.services.TeamMetricServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TeamMetricsApiController.class)
public class TeamMetricsApiControllerTest
    {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMetricRepository mockTeamMetricRepository;

    @MockBean
    private HierarchyClient hierarchyRestRepository;

    @MockBean
    private TeamCollectionReportService mockTeamCollectionReportService;

    @MockBean
    private TeamMetricServiceImpl teamMetricServiceImpl;

    private LocalDate now;

    @BeforeEach
    public void init()
        {
        now = LocalDate.now();
        }

    @Test
    public void listMetrics() throws Exception
        {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(get("/api/metrics/team1/" + formattedString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamMetricServiceImpl, times(1)).list("team1", now);
        }

    @Test
    public void deleteMetric() throws Exception
        {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(delete("/api/metrics/team1/lead_time/" + formattedString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamMetricServiceImpl, times(1)).delete("lead_time", "team1", now);
        }

    @Test
    public void getMetricTypes() throws Exception
        {
        mockMvc.perform(get("/api/metrics/metric_types/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamMetricServiceImpl, times(1)).listTypes();
        }

    @Test
    public void metricIngest() throws Exception
        {

        TeamMetric metric = new TeamMetric("team1", TeamMetricType.LEAD_TIME_FOR_CHANGE, 12d, 1d, now);
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.LEAD_TIME_FOR_CHANGE, now)).thenReturn(Optional.of(metric));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(post("/api/metrics/team1/" + formattedString)
                .content("[{ \"teamMetricType\": \"lead_time\", \"value\":\"12.0\"},{ \"teamMetricType\": \"cycletime\", \"value\":\"6.3\", \"target\":\"5d\"}]")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(teamMetricServiceImpl, times(1)).save(TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey(), "team1", now, 12d, null);
        verify(teamMetricServiceImpl, times(1)).save(TeamMetricType.CYCLE_TIME.getKey(), "team1", now, 6.3d, 5d);

        }
    }