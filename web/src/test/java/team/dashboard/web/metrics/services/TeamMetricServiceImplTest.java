package team.dashboard.web.metrics.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
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

    @BeforeEach
    public void setUp()
        {
        now = LocalDate.now();
        }

    @Test
    public void metricReportCalculateTestCoverageIngest()
        {

        metric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 50d, 10d, now);
        TeamMetric totalExecution = new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 100d, 100d, now);
        TeamMetric coverageMetric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 50d, 10d, now);

        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now)).thenReturn(Optional.of(metric));
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now)).thenReturn(Optional.of(totalExecution));

        teamMetricServiceImpl.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", now, 50d, 10d);

        verify(mockTeamMetricRepository, times(2)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamMetricRepository, times(1)).save(coverageMetric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }

    @Test
    public void metricReportCalculateTestCoverageIngestZeros()
        {

        metric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 0.0, 10.0, now);
        TeamMetric totalExecution = new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 0.0, 100.0, now);
        TeamMetric coverageMetric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 0.0, 10.0, now);

        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now)).thenReturn(Optional.of(metric));
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now)).thenReturn(Optional.of(totalExecution));

        teamMetricServiceImpl.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", now, 0.0, 10.0);

        verify(mockTeamMetricRepository, times(2)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamMetricRepository, times(1)).save(coverageMetric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }

    @Test
    public void metricReportCalculateTestCoverageNullTargets()
        {

        metric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 0.0, null, now);
        TeamMetric totalExecution = new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 0.0, null, now);
        TeamMetric coverageMetric = new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 0.0, null, now);

        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now)).thenReturn(Optional.of(metric));
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now)).thenReturn(Optional.of(totalExecution));

        teamMetricServiceImpl.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", now, 0.0, null);

        verify(mockTeamMetricRepository, times(2)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, now);
        verify(mockTeamMetricRepository, times(1)).deleteById(metric.getId());
        verify(mockTeamMetricRepository, times(1)).save(metric);
        verify(mockTeamMetricRepository, times(1)).save(coverageMetric);
        verify(mockTeamCollectionReportService, times(1)).updateCollectionStats("team1", now.getYear(), now.getMonth().getValue());

        }

    @Test
    public void testTotalTestCoverageCalculation()
        {
        Double totalCoveragePercentage = teamMetricServiceImpl.calculateTotalTestCoverage(20.0, 100.0);
        assertThat(totalCoveragePercentage, is(equalTo(20.0)));
        }

    @Test
    public void testTotalTestCoverageCalculationZeros()
        {
        Double totalCoveragePercentage = teamMetricServiceImpl.calculateTotalTestCoverage(0.0, 0.0);
        assertThat(totalCoveragePercentage, is(equalTo(0.0)));
        }

    @Test
    public void testTotalTestCoverageCalculationNulls()
        {
        Double totalCoveragePercentage = teamMetricServiceImpl.calculateTotalTestCoverage(null, null);
        assertThat(totalCoveragePercentage, is(equalTo(null)));
        }

    @Test
    public void testTotalTestCoverageCalculationZeroAutomation()
        {
        Double totalCoveragePercentage = teamMetricServiceImpl.calculateTotalTestCoverage(0.0, 100.0);
        assertThat(totalCoveragePercentage, is(equalTo(0.0)));
        }

    @Test
    public void testTotalTestCoverageCalculationAutomationZeroTestCount()
        {
        Double totalCoveragePercentage = teamMetricServiceImpl.calculateTotalTestCoverage(10.0, 0.0);
        assertThat(totalCoveragePercentage, is(equalTo(0.0)));
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