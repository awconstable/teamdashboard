package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.MTTR;
import team.dashboard.web.dora.repos.DORAMttrRepository;
import team.dashboard.web.dora.repos.IncidentClient;
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
class MttrServiceImplTest
    {

    @TestConfiguration
    static class LeadTimeServiceTestContextConfiguration
        {
        @MockBean private IncidentClient mockIncidentClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private TeamMetricService mockTeamMetricService;
        @MockBean private DORAMttrRepository mockDoraMttrRepository;

        @Bean
        public MttrService mttrService()
            {
            return new MttrServiceImpl(
                mockIncidentClient,
                mockHierarchyClient,
                mockTeamMetricService,
                mockDoraMttrRepository
            );
            }
        }

    @Autowired private MttrService mttrService;
    @Autowired private IncidentClient mockIncidentClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private TeamMetricService mockTeamMetricService;
    @Autowired private DORAMttrRepository mockDoraMttrRepository;
    
    @Test
    void get() {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        MTTR mttr = new MTTR(appId, Date.from(reportingDate.toInstant()), 120, 4, DORALevel.ELITE);
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(mttr));
    
        Optional<MTTR> mttr1 = mttrService.get(appId, Date.from(reportingDate.toInstant()));
    
        verify(mockDoraMttrRepository, times(1)).findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()));
        assertThat(mttr1.isPresent(), is(equalTo(true)));
        assertThat(mttr1.get().getApplicationId(), is(equalTo(appId)));
        assertThat(mttr1.get().getReportingDate(), is(equalTo(Date.from(reportingDate.toInstant()))));
    }
    
    @Test
    void load()
        {
        String appId = "app1";
        ZonedDateTime date = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Date reportingDate = Date.from(date.toInstant());
        MTTR mttr = new MTTR(appId, reportingDate, 1200, 4, DORALevel.ELITE);
        when(mockIncidentClient.getMttr("app1", reportingDate)).thenReturn(mttr);
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.empty());

        mttrService.load("app1", reportingDate);

        verify(mockIncidentClient, times(1)).getMttr("app1", reportingDate);
        verify(mockDoraMttrRepository, times(1)).save(mttr);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.MTTR.getKey(),
            "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            20.0,
            null
        );
        }
    
    @Test
    void loadWithExistingData()
        {
        String appId = "app1";
        ZonedDateTime date = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Date reportingDate = Date.from(date.toInstant());
        MTTR mttr = new MTTR(appId, reportingDate, 1200, 4, DORALevel.ELITE);
        when(mockIncidentClient.getMttr("app1", reportingDate)).thenReturn(mttr);
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(mttr));

        mttrService.load("app1", reportingDate);

        verify(mockDoraMttrRepository, times(1)).delete(any(MTTR.class));
        verify(mockIncidentClient, times(1)).getMttr("app1", reportingDate);
        verify(mockDoraMttrRepository, times(1)).save(mttr);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.MTTR.getKey(),
            "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            20.0,
            null
        );
        }

    @Test
    void loadWithUnknownPerformance()
        {
        Date reportingDate = new Date();
        MTTR mttr = new MTTR("app1", reportingDate, 0, 4, DORALevel.UNKNOWN);
        
        when(mockIncidentClient.getMttr("app1", reportingDate)).thenReturn(mttr);
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(mttr));

        mttrService.load("app1", reportingDate);

        verify(mockDoraMttrRepository, times(1)).delete(any(MTTR.class));
        verify(mockIncidentClient, times(1)).getMttr("app1", reportingDate);
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.MTTR.getKey(),
            "app1", 
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        verify(mockDoraMttrRepository, never()).save(mttr);
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

        MTTR mttr1 = new MTTR("app1", reportingDate, 1200, 4, DORALevel.ELITE);
        MTTR mttr2 = new MTTR("app2", reportingDate, 1200, 4, DORALevel.ELITE);
        MTTR mttr3 = new MTTR("app3", reportingDate, 1200, 4, DORALevel.ELITE);

        when(mockIncidentClient.getMttr("app1", reportingDate)).thenReturn(mttr1);
        when(mockIncidentClient.getMttr("app2", reportingDate)).thenReturn(mttr2);
        when(mockIncidentClient.getMttr("app3", reportingDate)).thenReturn(mttr3);
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(mttr1));
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app2", reportingDate)).thenReturn(Optional.of(mttr2));
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app3", reportingDate)).thenReturn(Optional.of(mttr3));

        mttrService.loadAll(reportingDate);

        verify(mockDoraMttrRepository, times(3)).delete(any(MTTR.class));
        verify(mockIncidentClient, times(3)).getMttr(anyString(), eq(reportingDate));
        verify(mockDoraMttrRepository, times(3)).save(any(MTTR.class));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.MTTR.getKey()),
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
        MTTR mttr = new MTTR("app1", reportingDate, 120, 4, DORALevel.ELITE);
        
        when(mockDoraMttrRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(mttr));

        mttrService.delete("app1", reportingDate);

        verify(mockDoraMttrRepository, times(1)).delete(mttr);
        }
    }