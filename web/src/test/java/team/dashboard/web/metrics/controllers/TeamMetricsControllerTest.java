package team.dashboard.web.metrics.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;
import team.dashboard.web.metrics.services.TeamMetricServiceImpl;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TeamMetricsController.class)
public class TeamMetricsControllerTest
    {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMetricRepository mockTeamMetricRepository;

    @MockBean
    private TeamRestRepository teamRestRepository;

    @MockBean
    private TeamCollectionReportService mockTeamCollectionReportService;

    @MockBean
    private TeamMetricServiceImpl teamMetricServiceImpl;

    private LocalDate now;

    private TeamMetric metric;

    @Before
    public void init()
        {
        now = LocalDate.now();
        }

    @Test
    public void metricIngest() throws Exception
        {
        metric = new TeamMetric("team1", TeamMetricType.CYCLE_TIME, 12d, now);
        when(teamMetricServiceImpl.save(TeamMetricType.CYCLE_TIME.getKey(), "team1", now, 12d)).thenReturn(metric);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(get("/metrics/" + TeamMetricType.CYCLE_TIME.getKey() + "/team1/" + formattedString + "/12").contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(teamMetricServiceImpl, times(1)).save(TeamMetricType.CYCLE_TIME.getKey(), "team1", now, 12d);

        }


    }
