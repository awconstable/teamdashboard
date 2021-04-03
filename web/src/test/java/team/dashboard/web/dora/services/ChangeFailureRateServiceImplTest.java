package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.ChangeFailureRate;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.repos.ChangeRequestClient;
import team.dashboard.web.dora.repos.DORAChangeFailureRateRepository;
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
class ChangeFailureRateServiceImplTest
    {

    @TestConfiguration
    static class ChangeFailureRateServiceTestContextConfiguration
        {
        @MockBean private ChangeRequestClient mockChangeRequestClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private TeamMetricService mockTeamMetricService;
        @MockBean private DORAChangeFailureRateRepository mockDoraChangeFailureRateRepository;

        @Bean
        public ChangeFailureRateService cfrService()
            {
            return new ChangeFailureRateServiceImpl(
                mockChangeRequestClient,
                mockHierarchyClient,
                mockTeamMetricService,
                mockDoraChangeFailureRateRepository
            );
            }
        }

    @Autowired private ChangeFailureRateService cfrService;
    @Autowired private ChangeRequestClient mockChangeRequestClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private TeamMetricService mockTeamMetricService;
    @Autowired private DORAChangeFailureRateRepository mockDoraChangeFailureRateRepository;
    
    @Test
    void get()
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        ChangeFailureRate cfr = new ChangeFailureRate(appId, Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(cfr));

        Optional<ChangeFailureRate> cfr1 = cfrService.get(appId, Date.from(reportingDate.toInstant()));

        verify(mockDoraChangeFailureRateRepository, times(1)).findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()));
        assertThat(cfr1.isPresent(), is(equalTo(true)));
        assertThat(cfr1.get().getApplicationId(), is(equalTo(appId)));
        assertThat(cfr1.get().getReportingDate(), is(equalTo(Date.from(reportingDate.toInstant()))));
        }

    @Test
    void load()
        {
        String appId = "app1";
        ZonedDateTime date = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Date reportingDate = Date.from(date.toInstant());
        ChangeFailureRate cfr = new ChangeFailureRate(appId, Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        when(mockChangeRequestClient.getChangeFailureRate(appId, reportingDate)).thenReturn(cfr);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.empty());

        cfrService.load(appId, reportingDate);

        verify(mockChangeRequestClient, times(1)).getChangeFailureRate(appId, reportingDate);
        verify(mockDoraChangeFailureRateRepository, times(1)).save(cfr);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.CHANGE_FAILURE_RATE.getKey(),
            appId, LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            0.0,
            null
        );
        }

    @Test
    void loadWithExistingData()
        {
        String appId = "app1";
        ZonedDateTime date = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Date reportingDate = Date.from(date.toInstant());
        ChangeFailureRate cfr = new ChangeFailureRate(appId, Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        when(mockChangeRequestClient.getChangeFailureRate(appId, reportingDate)).thenReturn(cfr);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(cfr));

        cfrService.load(appId, reportingDate);

        verify(mockDoraChangeFailureRateRepository, times(1)).delete(any(ChangeFailureRate.class));
        verify(mockChangeRequestClient, times(1)).getChangeFailureRate(appId, reportingDate);
        verify(mockDoraChangeFailureRateRepository, times(1)).save(cfr);
        verify(mockTeamMetricService, times(1)).save(
            TeamMetricType.CHANGE_FAILURE_RATE.getKey(),
            appId, LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")),
            0.0,
            null
        );
        }

    @Test
    void loadWithUnknownPerformance()
        {
        Date reportingDate = new Date();
        String appId = "app1";
        ChangeFailureRate cfr = new ChangeFailureRate(appId, Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.UNKNOWN);
        when(mockChangeRequestClient.getChangeFailureRate(appId, reportingDate)).thenReturn(cfr);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(cfr));

        cfrService.load(appId, reportingDate);

        verify(mockDoraChangeFailureRateRepository, times(1)).delete(any(ChangeFailureRate.class));
        verify(mockChangeRequestClient, times(1)).getChangeFailureRate(appId, reportingDate);
        verify(mockTeamMetricService, times(1)).delete(
            TeamMetricType.CHANGE_FAILURE_RATE.getKey(),
            appId, 
            LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))
        );
        verify(mockDoraChangeFailureRateRepository, never()).save(cfr);
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

        ChangeFailureRate cfr1 = new ChangeFailureRate("app1", Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        ChangeFailureRate cfr2 = new ChangeFailureRate("app2", Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        ChangeFailureRate cfr3 = new ChangeFailureRate("app3", Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.LOW);

        when(mockChangeRequestClient.getChangeFailureRate("app1", reportingDate)).thenReturn(cfr1);
        when(mockChangeRequestClient.getChangeFailureRate("app2", reportingDate)).thenReturn(cfr2);
        when(mockChangeRequestClient.getChangeFailureRate("app3", reportingDate)).thenReturn(cfr3);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(cfr1));
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate("app2", reportingDate)).thenReturn(Optional.of(cfr2));
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate("app3", reportingDate)).thenReturn(Optional.of(cfr3));

        cfrService.loadAll(reportingDate);

        verify(mockDoraChangeFailureRateRepository, times(3)).delete(any(ChangeFailureRate.class));
        verify(mockChangeRequestClient, times(3)).getChangeFailureRate(anyString(), eq(reportingDate));
        verify(mockDoraChangeFailureRateRepository, times(3)).save(any(ChangeFailureRate.class));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.CHANGE_FAILURE_RATE.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(0.0),
            isNull()
        );
        }

    @Test
    void delete()
        {
        Date reportingDate = new Date();
        String appId = "app1";
        ChangeFailureRate cfr = new ChangeFailureRate(appId, Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.UNKNOWN);
        when(mockDoraChangeFailureRateRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(cfr));

        cfrService.delete(appId, reportingDate);

        verify(mockDoraChangeFailureRateRepository, times(1)).delete(cfr);
        }
    }