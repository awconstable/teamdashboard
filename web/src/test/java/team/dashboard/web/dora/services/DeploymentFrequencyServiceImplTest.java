package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.TimePeriod;
import team.dashboard.web.dora.repos.DORADeployFreqRepository;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.hierarchy.domain.EntityType;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DeploymentFrequencyServiceImplTest
    {

    @TestConfiguration
    static class DeploymentServiceTestContextConfiguration
        {
        @MockBean private DeploymentClient mockDeploymentClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private DORADeployFreqRepository mockDoraFreqRepository;
        
        @Bean
        public DeploymentFrequencyService deploymentService()
            {
            return new DeploymentFrequencyServiceImpl(
                mockDeploymentClient, 
                mockHierarchyClient,
                mockDoraFreqRepository
            );
            }
        }
    
    @Autowired private DeploymentFrequencyService deploymentFrequencyService;
    @Autowired private DeploymentClient mockDeploymentClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private DORADeployFreqRepository mockDoraFreqRepository;
 
    @Test
    void get(){
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        DeploymentFrequency df = new DeploymentFrequency(appId, java.sql.Date.from(reportingDate.toInstant()), 10, TimePeriod.DAY, DORALevel.ELITE);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()))).thenReturn(Optional.of(df));
        
        Optional<DeploymentFrequency> fr = deploymentFrequencyService.get(appId, Date.from(reportingDate.toInstant()));
        
        verify(mockDoraFreqRepository, times(1)).findByApplicationIdAndReportingDate(appId, Date.from(reportingDate.toInstant()));
        assertThat(fr.isPresent(), is(equalTo(true)));
        assertThat(fr.get().getApplicationId(), is(equalTo(appId)));
        assertThat(fr.get().getReportingDate(), is(equalTo(Date.from(reportingDate.toInstant()))));
    }
    
    @Test
    void load()
        {
        Date reportingDate = new Date();

        DeploymentFrequency freq = new DeploymentFrequency("app1", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);
        when(mockDeploymentClient.getDeployFrequency("app1", reportingDate)).thenReturn(freq);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.empty());
        
        deploymentFrequencyService.load("app1", reportingDate);
        
        verify(mockDeploymentClient, times(1)).getDeployFrequency("app1", reportingDate);
        verify(mockDoraFreqRepository, times(1)).save(freq);
        }

    @Test
    void loadWithZeroDeploymentCount()
        {
        Date reportingDate = new Date();

        DeploymentFrequency freq = new DeploymentFrequency("app1", reportingDate, 0, TimePeriod.WEEK, DORALevel.MEDIUM);
        when(mockDeploymentClient.getDeployFrequency("app1", reportingDate)).thenReturn(freq);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.empty());

        deploymentFrequencyService.load("app1", reportingDate);

        verify(mockDeploymentClient, times(1)).getDeployFrequency("app1", reportingDate);
        verify(mockDoraFreqRepository, times(1)).save(freq);
        }

    @Test
    void loadWithExistingData()
        {
        Date reportingDate = new Date();

        DeploymentFrequency freq = new DeploymentFrequency("app1", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);
        when(mockDeploymentClient.getDeployFrequency("app1", reportingDate)).thenReturn(freq);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(freq));
        
        deploymentFrequencyService.load("app1", reportingDate);
        
        verify(mockDoraFreqRepository, times(1)).delete(any(DeploymentFrequency.class));
        verify(mockDeploymentClient, times(1)).getDeployFrequency("app1", reportingDate);
        verify(mockDoraFreqRepository, times(1)).save(freq);
        }

    @Test
    void loadWithUnknownPerformance()
        {
        Date reportingDate = new Date();

        DeploymentFrequency freq = new DeploymentFrequency("app1", reportingDate, 0, TimePeriod.YEAR, DORALevel.UNKNOWN);
        when(mockDeploymentClient.getDeployFrequency("app1", reportingDate)).thenReturn(freq);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(freq));

        deploymentFrequencyService.load("app1", reportingDate);

        verify(mockDoraFreqRepository, times(1)).delete(any(DeploymentFrequency.class));
        verify(mockDeploymentClient, times(1)).getDeployFrequency("app1", reportingDate);
        verify(mockDoraFreqRepository, never()).save(freq);
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
            Collections.emptyList(),
            Collections.emptyList());
        HierarchyEntity t2 = new HierarchyEntity("app2",
            EntityType.TEAM,
            "app2",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
        HierarchyEntity t3 = new HierarchyEntity("app3",
            EntityType.TEAM,
            "app3",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
        
        teams.add(t1);
        teams.add(t2);
        teams.add(t3);
        when(mockHierarchyClient.findAll()).thenReturn(teams);
        
        DeploymentFrequency f1 = new DeploymentFrequency("app1", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);
        DeploymentFrequency f2 = new DeploymentFrequency("app2", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);
        DeploymentFrequency f3 = new DeploymentFrequency("app3", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);

        when(mockDeploymentClient.getDeployFrequency("app1", reportingDate)).thenReturn(f1);
        when(mockDeploymentClient.getDeployFrequency("app2", reportingDate)).thenReturn(f2);
        when(mockDeploymentClient.getDeployFrequency("app3", reportingDate)).thenReturn(f3);
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(f1));
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app2", reportingDate)).thenReturn(Optional.of(f2));
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app3", reportingDate)).thenReturn(Optional.of(f3));

        deploymentFrequencyService.loadAll(reportingDate);

        verify(mockDoraFreqRepository, times(3)).delete(any(DeploymentFrequency.class));
        verify(mockDeploymentClient, times(3)).getDeployFrequency(anyString(), eq(reportingDate));
        verify(mockDoraFreqRepository, times(3)).save(any(DeploymentFrequency.class));
        }
    
    @Test
    void delete()
        {
        Date reportingDate = new Date();
        DeploymentFrequency f1 = new DeploymentFrequency("app1", reportingDate, 10, TimePeriod.DAY, DORALevel.ELITE);
        
        when(mockDoraFreqRepository.findByApplicationIdAndReportingDate("app1", reportingDate)).thenReturn(Optional.of(f1));

        deploymentFrequencyService.delete("app1", reportingDate);
        
        verify(mockDoraFreqRepository, times(1)).delete(f1);
        }
    }