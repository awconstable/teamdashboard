package team.dashboard.web.dora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.ChangeFailureRate;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.repos.ChangeRequestClient;
import team.dashboard.web.dora.repos.DORAChangeFailureRateRepository;
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
public class ChangeFailureRateServiceImpl implements ChangeFailureRateService
    {

    private final ChangeRequestClient changeRequestClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;
    private final DORAChangeFailureRateRepository doraChangeFailureRateRepository;

    @Autowired
    public ChangeFailureRateServiceImpl(ChangeRequestClient changeRequestClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService, DORAChangeFailureRateRepository doraChangeFailureRateRepository)
        {
        this.changeRequestClient = changeRequestClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        this.doraChangeFailureRateRepository = doraChangeFailureRateRepository;
        }

    @Override
    public Optional<ChangeFailureRate> get(String applicationId, Date reportingDate)
        {
        return doraChangeFailureRateRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        delete(applicationId, reportingDate);
        ChangeFailureRate cfr = changeRequestClient.getChangeFailureRate(applicationId, reportingDate);
        if(DORALevel.UNKNOWN.equals(cfr.getDoraLevel())){
        teamMetricService.delete(
            TeamMetricType.CHANGE_FAILURE_RATE.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
        } else {
        doraChangeFailureRateRepository.save(cfr);
        teamMetricService.save(
            TeamMetricType.CHANGE_FAILURE_RATE.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            cfr.getChangeFailureRatePercent(),
            null
        );
        }
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
        Optional<ChangeFailureRate> cfr = doraChangeFailureRateRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        cfr.ifPresent(doraChangeFailureRateRepository::delete);
        }
    }
