package team.dashboard.web.dora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.LeadTime;
import team.dashboard.web.dora.repos.DORALeadTimeRepository;
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
public class LeadTimeServiceImpl implements LeadTimeService
    {
    private final DeploymentClient deploymentClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;
    private final DORALeadTimeRepository doraLeadTimeRepository;

    @Autowired
    public LeadTimeServiceImpl(DeploymentClient deploymentClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService, DORALeadTimeRepository doraLeadTimeRepository)
        {
        this.deploymentClient = deploymentClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        this.doraLeadTimeRepository = doraLeadTimeRepository;
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        delete(applicationId, reportingDate);
        LeadTime leadTime = deploymentClient.getLeadTime(applicationId, reportingDate);
        doraLeadTimeRepository.save(leadTime);
        teamMetricService.save(
            TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            Long.valueOf(leadTime.getLeadTimeSeconds()).doubleValue(),
            null
        );
        }

    @Override
    public void loadAll(Date reportingDate)
        {
        List<HierarchyEntity> teams = hierarchyClient.findAll();
        teams.forEach(team -> load(team.getSlug(), reportingDate));
        }

    @Override
    public void delete(String applicationId, Date reportingDate)
        {
        Optional<LeadTime> lt = doraLeadTimeRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        lt.ifPresent(doraLeadTimeRepository::delete);
        }
    }
