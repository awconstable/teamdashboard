package team.dashboard.web.metrics.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TeamMetricServiceImplTest
    {

    @Autowired
    private TeamMetricRepository mockTeamMetricRepository;
    @Autowired
    private TeamCollectionReportService mockTeamCollectionReportService;
    @Autowired
    private TeamMetricServiceImpl teamMetricServiceImpl;
    private LocalDate now;
    private TeamMetric metric;

    @Before
    public void setUp()
        {
        now = LocalDate.now();
        }

    @Test
    public void metricReportCalculateTestCoverageIngest()
        {

        metric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 50d, now);
        TeamMetric totalExecution = new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 100d, now);
        TeamMetric coverageMetric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 50d, now);

        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now)).thenReturn(Optional.of(metric));
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now)).thenReturn(Optional.of(totalExecution));

        teamMetricServiceImpl.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", now, 50d);

        verify(mockTeamMetricRepository, times(2)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamMetricRepository, times(1)).save(coverageMetric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }

    @TestConfiguration
    static class TeamMetricServiceTestContextConfiguration
        {

        @MockBean
        private TeamMetricRepository mockTeamMetricRepository;

        @MockBean
        private TeamCollectionReportService mockTeamCollectionReportService;

        @Bean
        public TeamMetricService teamMetricService()
            {
            return new TeamMetricServiceImpl(mockTeamMetricRepository, mockTeamCollectionReportService);
            }
        }
    }