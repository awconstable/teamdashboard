package team.dashboard.web.metrics;

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
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now)).thenReturn(Optional.of(metric));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(get("/metrics/" + TeamMetricType.CYCLE_TIME.getKey() + "/team1/" + formattedString + "/12"))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now);
        verify(mockTeamMetricRepository, times(1)).save(metric);

        }

    @Test
    public void metricReportIngest() throws Exception
        {

        metric = new TeamMetric("team1", TeamMetricType.LEAD_TIME_FOR_CHANGE, 12d, now);
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.LEAD_TIME_FOR_CHANGE, now)).thenReturn(Optional.of(metric));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(post("/metrics/team1/" + formattedString)
                .content("[{ \"teamMetricType\": \"lead_time\", \"value\":\"12.0\"}]")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.LEAD_TIME_FOR_CHANGE, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }

    @Test
    public void metricReportCalculateTestCoverageIngest() throws Exception
        {

        metric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 50d, now);
        TeamMetric totalExecution = new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 100d, now);
        TeamMetric coverageMetric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 50d, now);

        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now)).thenReturn(Optional.of(metric));
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now)).thenReturn(Optional.of(totalExecution));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(post("/metrics/team1/" + formattedString)
                .content("[{ \"teamMetricType\": \"test_automation_execution_count\", \"value\":\"50.0\"}]")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        verify(mockTeamMetricRepository, times(2)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamMetricRepository, times(1)).save(coverageMetric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }
    }
