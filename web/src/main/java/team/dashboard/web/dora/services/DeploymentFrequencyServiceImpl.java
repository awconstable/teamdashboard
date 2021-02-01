package team.dashboard.web.dora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.repos.DORADeployFreqRepository;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeploymentFrequencyServiceImpl implements DeploymentFrequencyService
    {
    private final DeploymentClient deploymentClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;
    private final DORADeployFreqRepository doraDeployFreqRepository;

    @Autowired
    public DeploymentFrequencyServiceImpl(DeploymentClient deploymentClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService, DORADeployFreqRepository doraDeployFreqRepository)
        {
        this.deploymentClient = deploymentClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        this.doraDeployFreqRepository = doraDeployFreqRepository;
        }

    @Override
    public Optional<DeploymentFrequency> get(String applicationId, Date reportingDate)
        {
            return doraDeployFreqRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        delete(applicationId, reportingDate);
        DeploymentFrequency freq = deploymentClient.getDeployFrequency(applicationId, reportingDate);
        if(DORALevel.UNKNOWN.equals(freq.getDeployFreqLevel()))
            {
            teamMetricService.delete(
                TeamMetricType.DEPLOYMENT_COUNT.getKey(),
                applicationId,
                LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
            } else {
            doraDeployFreqRepository.save(freq);
            teamMetricService.save(
                TeamMetricType.DEPLOYMENT_COUNT.getKey(),
                applicationId,
                LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
                freq.getDeploymentCount().doubleValue(),
                null
            );
            }
        }

    @Override
    public void loadAll(final Date reportingDate)
        {
        List<HierarchyEntity> teams = hierarchyClient.findAll();
        teams.forEach(team -> load(team.getSlug(), reportingDate));
        }

    @Override
    public void delete(String applicationId, Date reportingDate)
        {
        Optional<DeploymentFrequency> freq = doraDeployFreqRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        freq.ifPresent(doraDeployFreqRepository::delete);
        }

    }
