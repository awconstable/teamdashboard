package team.dashboard.web.metrics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TeamMetricServiceImpl implements TeamMetricService
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamCollectionReportService teamCollectionReportService;

    @Autowired
    public TeamMetricServiceImpl(TeamMetricRepository teamMetricRepository, TeamCollectionReportService teamCollectionReportService)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.teamCollectionReportService = teamCollectionReportService;
        }

    @Override
    public TeamMetric save(String metricType, String teamId, LocalDate date, Double value, Double target)
        {
        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        delete(metricType, teamId, date);

        TeamMetric newMetric = new TeamMetric(teamId, type, value, target, date);
        teamMetricRepository.save(newMetric);

        if (TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.equals(type) || TeamMetricType.TEST_TOTAL_EXECUTION_COUNT.equals(type))
            {
            Optional<TeamMetric> totalTestExecution = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, date);
            Optional<TeamMetric> totalAutomationExecution = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, date);
            if (totalTestExecution.isPresent() && totalAutomationExecution.isPresent())
                {
                Double testCoverage = calculateTotalTestCoverage(totalAutomationExecution.get().getValue(), totalTestExecution.get().getValue());
                Double targetCoverage = calculateTotalTestCoverage(totalAutomationExecution.get().getTarget(), totalTestExecution.get().getTarget());
                TeamMetric testCoverageMetric = new TeamMetric(teamId, TeamMetricType.TEST_AUTOMATION_COVERAGE, testCoverage, targetCoverage, date);

                Optional<TeamMetric> existingTestCoverage = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, TeamMetricType.TEST_AUTOMATION_COVERAGE, date);
                existingTestCoverage.ifPresent(teamMetricRepository::delete);
                teamMetricRepository.save(testCoverageMetric);
                }
            }

        teamCollectionReportService.updateCollectionStats(teamId, date.getYear(), date.getMonth().getValue());

        return newMetric;
        }

    Double calculateTotalTestCoverage(Double automationExecutionCount, Double testExecutionCount)
        {
        if (automationExecutionCount == null || testExecutionCount == null)
            {
            return null;
            }

        if (automationExecutionCount.isNaN() || automationExecutionCount.equals(0.0)
                || testExecutionCount.isNaN() || testExecutionCount.equals(0.0))
            {
            return 0.0;
            }

        return automationExecutionCount / testExecutionCount * 100;
        }

    @Override
    public void delete(String metricType, String teamId, LocalDate reportingDate)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, reportingDate);

        metric.ifPresent(teamMetric -> teamMetricRepository.deleteById(teamMetric.getId()));
        }

    @Override
    public List<TeamMetric> list(String teamId, LocalDate reportingDate)
        {
        return teamMetricRepository.findByTeamIdIgnoreCaseAndDate(teamId, reportingDate);
        }

    @Override
    public TeamMetricType[] listTypes()
        {
        return TeamMetricType.values();
        }

    }
