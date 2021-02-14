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
import team.dashboard.web.hierarchy.domain.Relation;
import team.dashboard.web.hierarchy.repos.HierarchyClient;
import team.dashboard.web.metrics.domain.TeamMetricType;
import team.dashboard.web.metrics.services.TeamMetricService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
    
    private HierarchyEntity getTeam(String appId){
    Relation c1 = new Relation(appId + "c1", EntityType.TEAM, appId + "c1", appId, Collections.emptyList());
    Relation c2 = new Relation(appId + "c2", EntityType.TEAM, appId + "c2", appId, Collections.emptyList());
    Relation c3 = new Relation(appId + "c3", EntityType.TEAM, appId + "c3", appId, Collections.emptyList());

    return new HierarchyEntity(appId,
        EntityType.TEAM,
        appId,
        null,
        Collections.emptyList(),
        Arrays.asList(c1, c2, c3),
        Collections.emptyList(),
        Collections.emptyList());
    }
    
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
    
    private List<Deployment> getDeployments(Date reportingDate)
        {
        Change c1 = new Change("c1", reportingDate, "test", "test");
        Change c2 = new Change("c2", reportingDate, "test", "test");
        Change c3 = new Change("c3", reportingDate, "test", "test");
        Change c4 = new Change("c4", reportingDate, "test", "test");
        Change c5 = new Change("c5", reportingDate, "test", "test");
        HashSet<Change> changes = new HashSet<>(Arrays.asList(c1, c2, c3, c4, c5));
        Deployment d1 = new Deployment("id1", "d1","", "app1", "rfc1", reportingDate, "", changes);
        Deployment d2 = new Deployment("id1","d2","","app2", "rfc2", reportingDate, "", changes);
        Deployment d3 = new Deployment("id1","d3","","app3", "rfc3", reportingDate, "", changes);
    
        List<Deployment> deploys = new ArrayList<>();
        deploys.add(d1);
        deploys.add(d2);
        deploys.add(d3);
        return deploys;
        }
    
    @Test
    void loadZeroDeploys()
        {
        Date reportingDate = new Date();

        when(mockDeploymentClient.getDeploymentsForApplicationWithDate("app1", reportingDate)).thenReturn(Collections.emptyList());

        deploymentService.load("app1", reportingDate);

        verify(mockTeamMetricService, times(1)).delete(TeamMetricType.DEPLOYMENT_COUNT.getKey(), "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
        verify(mockTeamMetricService, times(1)).delete(TeamMetricType.BATCH_SIZE.getKey(), "app1", LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC")));
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
        when(mockDeploymentClient.getDeploymentsForApplicationWithDate(anyString(), eq(reportingDate))).thenReturn(getDeployments(reportingDate));

        deploymentService.loadAll(reportingDate);

        verify(mockTeamMetricService, times(3)).delete(eq(TeamMetricType.DEPLOYMENT_COUNT.getKey()), anyString(), any(LocalDate.class));
        verify(mockTeamMetricService, times(3)).delete(eq(TeamMetricType.BATCH_SIZE.getKey()), anyString(), any(LocalDate.class));
        verify(mockDeploymentClient, times(3)).getDeploymentsForApplicationWithDate(anyString(), eq(reportingDate));
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.DEPLOYMENT_COUNT.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(3.0),
            isNull()
        );
        verify(mockTeamMetricService, times(3)).save(
            eq(TeamMetricType.BATCH_SIZE.getKey()),
            anyString(),
            eq(LocalDate.ofInstant(reportingDate.toInstant(), ZoneId.of("UTC"))),
            eq(5.0),
            isNull()
        );
        }
    
    @Test
    void listDeployments()
        {
        String appId = "app1";
        when(mockHierarchyClient.findEntityBySlug(appId)).thenReturn(getTeam(appId));
        when(mockDeploymentClient.getDeploymentsForApplication(anyString())).thenReturn(getDeployments(new Date()));
        
        List<Deployment> deployments = deploymentService.listDeployments(appId);

        verify(mockDeploymentClient, times(4)).getDeploymentsForApplication(anyString());
        assertThat(deployments.size(), is(equalTo(12)));
        }

    @Test
    void listDeploymentsWithoutHierarchy()
        {
        String appId = "app1";
        when(mockHierarchyClient.findEntityBySlug(appId)).thenReturn(null);
        when(mockDeploymentClient.getDeploymentsForApplication(anyString())).thenReturn(getDeployments(new Date()));

        List<Deployment> deployments = deploymentService.listDeployments(appId);

        verify(mockDeploymentClient, never()).getDeploymentsForApplication(anyString());
        assertThat(deployments.size(), is(equalTo(0)));
        }
    }