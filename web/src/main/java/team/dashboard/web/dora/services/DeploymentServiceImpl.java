package team.dashboard.web.dora.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.Deployment;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class DeploymentServiceImpl implements DeploymentService
    {

    private static final Logger log = LoggerFactory.getLogger(DeploymentServiceImpl.class);

    private final DeploymentClient deploymentClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;

    @Autowired
    public DeploymentServiceImpl(DeploymentClient deploymentClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService)
        {
        this.deploymentClient = deploymentClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        List<Deployment> deploys = deploymentClient.getDeploymentsForApplicationWithDate(applicationId, reportingDate);

        log.info("Loading {} deployments with date {}", deploys.size(), DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))));
        
        teamMetricService.delete(
                TeamMetricType.DEPLOYMENT_COUNT.getKey(),
                applicationId,
                LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));

        if(deploys.size() > 0)
            {
            teamMetricService.save(
                TeamMetricType.DEPLOYMENT_COUNT.getKey(),
                applicationId,
                LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
                (double) deploys.size(),
                null
            );
            }
        }

    @Override
    public void loadAll(Date reportingDate)
        {
        log.info("Loading all deployments with date {}", DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))));
        List<HierarchyEntity> teams = hierarchyClient.findAll();
        teams.forEach(team -> load(team.getSlug(), reportingDate));
        }
    }
