package team.dashboard.web.happiness.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.happiness.domain.HappinessTrend;
import team.dashboard.web.happiness.repos.HappinessClient;
import team.dashboard.web.hierarchy.domain.EntityType;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class HappinessServiceImplTest
    {

    @TestConfiguration
    static class HappinessServiceImplTestContextConfiguration
        {
        @MockBean private HappinessClient mockHappinessClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private TeamMetricService mockTeamMetricService;

        @Bean
        public HappinessService happinessService()
            {
            return new HappinessServiceImpl(
                mockHappinessClient,
                mockHierarchyClient,
                mockTeamMetricService
            );
            }
        }
    
    @Autowired private HappinessService happinessService;
    @Autowired private HappinessClient mockHappinessClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private TeamMetricService mockTeamMetricService;
    
    @Test
    void get()
        {           
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        HappinessTrend tr = new HappinessTrend(appId, 3.4, 5, reportingDate.toLocalDate());
        when(mockHappinessClient.get90DayTrend(appId, Date.from(reportingDate.toInstant()))).thenReturn(tr);
     
        Optional<HappinessTrend> ltr = happinessService.get(appId, Date.from(reportingDate.toInstant()));

        verify(mockHappinessClient, times(1)).get90DayTrend(appId, Date.from(reportingDate.toInstant()));
        assertThat(ltr.isPresent(), is(equalTo(true)));
        assertThat(ltr.get().getTeamId(), is(equalTo(appId)));
        assertThat(ltr.get().getTrendDate(), is(equalTo(reportingDate.toLocalDate())));
        }

    @Test
    void load()
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        HappinessTrend tr = new HappinessTrend(appId, 3.4, 5, reportingDate.toLocalDate());

        when(mockHappinessClient.get90DayTrend(appId, Date.from(reportingDate.toInstant()))).thenReturn(tr);

        happinessService.load(appId, Date.from(reportingDate.toInstant()));

        verify(mockHappinessClient, times(1)).get90DayTrend(appId, Date.from(reportingDate.toInstant()));
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            appId, 
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            appId, 
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            3.4,
            null
        );
        }

    @Test
    void loadWithNoTrend()
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        HappinessTrend tr = new HappinessTrend(appId, 0.0, 0, reportingDate.toLocalDate());

        when(mockHappinessClient.get90DayTrend(appId, Date.from(reportingDate.toInstant()))).thenReturn(tr);

        happinessService.load(appId, Date.from(reportingDate.toInstant()));

        verify(mockHappinessClient, times(1)).get90DayTrend(appId, Date.from(reportingDate.toInstant()));
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            appId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        verify(mockTeamMetricService, times(0)).save(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            appId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            0.0,
            null
        );
        }

    @Test
    void loadAll()
        {
        List<HierarchyEntity> teams = new ArrayList<>();
        HierarchyEntity t1 = new HierarchyEntity("app1",
            EntityType.TEAM,
            "app1",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
        HierarchyEntity t2 = new HierarchyEntity("app2",
            EntityType.TEAM,
            "app2",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
        HierarchyEntity t3 = new HierarchyEntity("app3",
            EntityType.TEAM,
            "app3",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());

        teams.add(t1);
        teams.add(t2);
        teams.add(t3);
        when(mockHierarchyClient.findAll()).thenReturn(teams);
        
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        HappinessTrend tr1 = new HappinessTrend("app1", 3.0, 5, reportingDate.toLocalDate());
        HappinessTrend tr2 = new HappinessTrend("app2", 3.0, 5, reportingDate.toLocalDate());
        HappinessTrend tr3 = new HappinessTrend("app3", 3.0, 5, reportingDate.toLocalDate());
        when(mockHappinessClient.get90DayTrend("app1", Date.from(reportingDate.toInstant()))).thenReturn(tr1);
        when(mockHappinessClient.get90DayTrend("app2", Date.from(reportingDate.toInstant()))).thenReturn(tr2);
        when(mockHappinessClient.get90DayTrend("app3", Date.from(reportingDate.toInstant()))).thenReturn(tr3);
        
        happinessService.loadAll(Date.from(reportingDate.toInstant()));

        verify(mockHierarchyClient, times(1)).findAll();
        verify(mockHappinessClient, times(3)).get90DayTrend(anyString(), eq(Date.from(reportingDate.toInstant())));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.TEAM_HAPPINESS.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(3.0),
            isNull()
        );
        }

    @Test
    void delete()
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
  
        happinessService.delete(appId, Date.from(reportingDate.toInstant()));
        
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.TEAM_HAPPINESS.getKey(),
            appId,
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        }
    }