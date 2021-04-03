package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.LeadTime;
import team.dashboard.web.dora.repos.DORALeadTimeRepository;
import team.dashboard.web.dora.repos.DeploymentClient;
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
class LeadTimeServiceImplTest
    {

    @TestConfiguration
    static class LeadTimeServiceTestContextConfiguration
        {
        @MockBean private DeploymentClient mockDeploymentClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private TeamMetricService mockTeamMetricService;
        @MockBean private DORALeadTimeRepository mockDoraLeadTimeRepository;

        @Bean
        public LeadTimeService deploymentService()
            {
            return new LeadTimeServiceImpl(
                mockDeploymentClient,
                mockHierarchyClient,
                mockTeamMetricService,
                mockDoraLeadTimeRepository
            );
            }
        }

    @Autowired private LeadTimeService leadTimeService;
    @Autowired private DeploymentClient mockDeploymentClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private TeamMetricService mockTeamMetricService;
    @Autowired private DORALeadTimeRepository mockDoraLeadTimeRepository;
    
    @Test
    void get() {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        LeadTime lt = new LeadTime(appId, java.sql.Date.from(reportingDate.toInstant()), 120, DORALevel.ELITE);
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(lt));
    
        Optional<LeadTime> ltr = leadTimeService.get(appId, Date.from(reportingDate.toInstant()));
    
        verify(mockDoraLeadTimeRepository, times(1)).findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()));
        assertThat(ltr.isPresent(), is(equalTo(true)));
        assertThat(ltr.get().getApplicationId(), is(equalTo(appId)));
        assertThat(ltr.get().getReportingDate(), is(equalTo(Date.from(reportingDate.toInstant()))));
    }
    
    @Test
    void load()
        {
        Date reportingDate = new Date();

        LeadTime lt = new LeadTime("app1", reportingDate, 1200, DORALevel.ELITE);
        when(mockDeploymentClient.getLeadTime("app1", reportingDate)).thenReturn(lt);
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.empty());

        leadTimeService.load("app1", reportingDate);

        verify(mockDeploymentClient, times(1)).getLeadTime("app1", reportingDate);
        verify(mockDoraLeadTimeRepository, times(1)).save(lt);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey(),
            "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            20.0,
            null
        );
        }

    @Test
    void loadWithExistingData()
        {
        Date reportingDate = new Date();

        LeadTime lt = new LeadTime("app1", reportingDate, 1200, DORALevel.ELITE);
        when(mockDeploymentClient.getLeadTime("app1", reportingDate)).thenReturn(lt);
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(lt));

        leadTimeService.load("app1", reportingDate);

        verify(mockDoraLeadTimeRepository, times(1)).delete(any(LeadTime.class));
        verify(mockDeploymentClient, times(1)).getLeadTime("app1", reportingDate);
        verify(mockDoraLeadTimeRepository, times(1)).save(lt);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey(),
            "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            20.0,
            null
        );
        }

    @Test
    void loadWithUnknownPerformance()
        {
        Date reportingDate = new Date();

        LeadTime lt = new LeadTime("app1", reportingDate, 0, DORALevel.UNKNOWN);
        when(mockDeploymentClient.getLeadTime("app1", reportingDate)).thenReturn(lt);
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(lt));

        leadTimeService.load("app1", reportingDate);

        verify(mockDoraLeadTimeRepository, times(1)).delete(any(LeadTime.class));
        verify(mockDeploymentClient, times(1)).getLeadTime("app1", reportingDate);
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey(),
            "app1", 
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        verify(mockDoraLeadTimeRepository, never()).save(lt);
        verify(mockTeamMetricService, never()).save(
            anyString(),
            anyString(),
            any(LocalDate.class),
            anyDouble(),
            isNull()
        );
        }

    @Test
    void loadAll()
        {
        Date reportingDate = new Date();

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

        LeadTime lt1 = new LeadTime("app1", reportingDate, 1200, DORALevel.ELITE);
        LeadTime lt2 = new LeadTime("app2", reportingDate, 1200, DORALevel.ELITE);
        LeadTime lt3 = new LeadTime("app3", reportingDate, 1200, DORALevel.ELITE);

        when(mockDeploymentClient.getLeadTime("app1", reportingDate)).thenReturn(lt1);
        when(mockDeploymentClient.getLeadTime("app2", reportingDate)).thenReturn(lt2);
        when(mockDeploymentClient.getLeadTime("app3", reportingDate)).thenReturn(lt3);
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(lt1));
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app2", reportingDate)).thenReturn(Optional.of(lt2));
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app3", reportingDate)).thenReturn(Optional.of(lt3));

        leadTimeService.loadAll(reportingDate);

        verify(mockDoraLeadTimeRepository, times(3)).delete(any(LeadTime.class));
        verify(mockDeploymentClient, times(3)).getLeadTime(anyString(), eq(reportingDate));
        verify(mockDoraLeadTimeRepository, times(3)).save(any(LeadTime.class));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.LEAD_TIME_FOR_CHANGE.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(20.0),
            isNull()
        );
        }

    @Test
    void delete()
        {
        Date reportingDate = new Date();
        LeadTime lt1 = new LeadTime("app1", reportingDate, 1200, DORALevel.ELITE);
        
        when(mockDoraLeadTimeRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(lt1));

        leadTimeService.delete("app1", reportingDate);

        verify(mockDoraLeadTimeRepository, times(1)).delete(lt1);
        }
    }