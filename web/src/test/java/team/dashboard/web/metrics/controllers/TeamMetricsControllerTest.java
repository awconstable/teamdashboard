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
import team.dashboard.web.collection.services.TeamCollectionReportService;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetric;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;
import team.dashboard.web.metrics.services.TeamMetricServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TeamMetricsController.class)
public class TeamMetricsControllerTest
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
    public void getMetric() throws Exception
        {
        TeamMetric metric = new TeamMetric("team1", TeamMetricType.CYCLE_TIME, 12d, 5d, now);
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now)).thenReturn(Optional.of(metric));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(get("/metrics/" + TeamMetricType.CYCLE_TIME.getKey() + "/team1/" + formattedString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now);

        }


    }
