package team.dashboard.web.dora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.MTTR;
import team.dashboard.web.dora.repos.DORAMttrRepository;
import team.dashboard.web.dora.repos.IncidentClient;
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
public class MttrServiceImpl implements MttrService
    {

    private final IncidentClient incidentClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;
    private final DORAMttrRepository doraMttrRepository;

    @Autowired
    public MttrServiceImpl(IncidentClient incidentClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService, DORAMttrRepository doraMttrRepository)
        {
        this.incidentClient = incidentClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        this.doraMttrRepository = doraMttrRepository;
        }

    @Override
    public Optional<MTTR> get(String applicationId, Date reportingDate)
        {
        return doraMttrRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        delete(applicationId, reportingDate);
        MTTR mttr = incidentClient.getMttr(applicationId, reportingDate);
        if(DORALevel.UNKNOWN.equals(mttr.getDoraLevel())){
        teamMetricService.delete(
            TeamMetricType.MTTR.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
        } else {
        doraMttrRepository.save(mttr);
        long mttrMins = mttr.getMeanTimeToRecoverSeconds() / 60;
        teamMetricService.save(
            TeamMetricType.MTTR.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            Long.valueOf(mttrMins).doubleValue(),
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
        Optional<MTTR> mttr = doraMttrRepository.findByApplicationIdAndReportingDate(applicationId, reportingDate);
        mttr.ifPresent(doraMttrRepository::delete);
        }
    }
