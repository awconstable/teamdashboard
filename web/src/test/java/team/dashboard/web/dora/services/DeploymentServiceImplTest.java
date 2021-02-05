package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.Change;
import team.dashboard.web.dora.domain.Deployment;
import team.dashboard.web.dora.repos.DeploymentClient;
import team.dashboard.web.hierarchy.domain.EntityType;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DeploymentServiceImplTest
    {

    @TestConfiguration
    static class DeploymentServiceTestContextConfiguration
        {
        @MockBean private DeploymentClient mockDeploymentClient;
        @MockBean private HierarchyClient mockHierarchyClient;
        @MockBean private TeamMetricService mockTeamMetricService;

        @Bean
        public DeploymentService deploymentService()
            {
            return new DeploymentServiceImpl(
                mockDeploymentClient,
                mockHierarchyClient,
                mockTeamMetricService
            );
            }
        }

    @Autowired private DeploymentService deploymentService;
    @Autowired private DeploymentClient mockDeploymentClient;
    @Autowired private HierarchyClient mockHierarchyClient;
    @Autowired private TeamMetricService mockTeamMetricService;
    
    private List<HierarchyEntity> getTeams(){
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
    return teams;
    }
    
    @Test
    void loadZeroDeploys()
        {
        Date reportingDate = new Date();

        when(mockDeploymentClient.getDeploymentsForApplicationWithDate("app1", reportingDate)).thenReturn(Collections.emptyList());

        deploymentService.load("app1", reportingDate);

        verify(mockTeamMetricService, times(1)).delete(TeamMetricType.DEPLOYMENT_COUNT.getKey(), "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
        verify(mockDeploymentClient, times(1)).getDeploymentsForApplicationWithDate(anyString(), eq(reportingDate));
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
        
        when(mockHierarchyClient.findAll()).thenReturn(getTeams());

        Deployment d1 = new Deployment("id1", "d1","", "app1", "rfc1", reportingDate, "", new HashSet<Change>());
        Deployment d2 = new Deployment("id1","d2","","app2", "rfc2", reportingDate, "", new HashSet<Change>());
        Deployment d3 = new Deployment("id1","d3","","app3", "rfc3", reportingDate, "", new HashSet<Change>());
        
        List<Deployment> deploys = new ArrayList<>();
        deploys.add(d1);
        deploys.add(d2);
        deploys.add(d3);
        
        when(mockDeploymentClient.getDeploymentsForApplicationWithDate(anyString(), eq(reportingDate))).thenReturn(deploys);

        deploymentService.loadAll(reportingDate);

        verify(mockTeamMetricService, times(3)).delete(anyString(), anyString(), any(LocalDate.class));
        verify(mockDeploymentClient, times(3)).getDeploymentsForApplicationWithDate(anyString(), eq(reportingDate));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.DEPLOYMENT_COUNT.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(3.0),
            isNull()
        );
        }
    }