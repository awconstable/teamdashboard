package team.dashboard.web.happiness.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.happiness.domain.HappinessTrend;
import team.dashboard.web.happiness.repos.HappinessClient;
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
public class HappinessServiceImpl implements HappinessService
    {
    private final HappinessClient happinessClient;
    private final HierarchyClient hierarchyClient;
    private final TeamMetricService teamMetricService;

    @Autowired
    public HappinessServiceImpl(HappinessClient happinessClient, HierarchyClient hierarchyClient, TeamMetricService teamMetricService)
        {
        this.happinessClient = happinessClient;
        this.hierarchyClient = hierarchyClient;
        this.teamMetricService = teamMetricService;
        }

    @Override
    public Optional<HappinessTrend> get(String applicationId, Date reportingDate)
        {
        return Optional.of(happinessClient.get90DayTrend(applicationId, reportingDate));
        }

    @Override
    public void load(String applicationId, Date reportingDate)
        {
        delete(applicationId, reportingDate);
        Optional<HappinessTrend> trend = get(applicationId, reportingDate);
        trend.ifPresent(happinessTrend -> {
            if(happinessTrend.getResponseCount() > 0){    
            teamMetricService.save(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            happinessTrend.getAvgHappinessRating(),
            null
            );
            }
        });
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
        teamMetricService.delete(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            applicationId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
        }
    }
