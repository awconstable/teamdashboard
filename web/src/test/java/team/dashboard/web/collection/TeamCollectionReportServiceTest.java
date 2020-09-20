package team.dashboard.web.collection;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.hierarchy.EntityType;
import team.dashboard.web.hierarchy.HierarchyEntity;
import team.dashboard.web.hierarchy.HierarchyRestRepository;
import team.dashboard.web.hierarchy.Relation;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TeamCollectionReportServiceTest
    {

    @Autowired
    private HierarchyRestRepository mockHierarchyRestRepository;
    @Autowired
    private TeamMetricRepository mockTeamMetricRepository;
    @Autowired
    private TeamCollectionReportService teamCollectionReportService;
    @Autowired
    private TeamCollectionReportRepository mockTeamCollectionReportRepository;
    private HierarchyEntity team0;
    private HierarchyEntity team1;
    private HierarchyEntity team2;

    @BeforeEach
    public void setUp()
        {
        team0 = new HierarchyEntity("team0", EntityType.TEAM, "Team 0", null,
                null,
                Arrays.asList(new Relation("team1", EntityType.TEAM, "Team 1", "team0", Collections.emptyList()),
                        new Relation("team2", EntityType.TEAM, "Team 2", "team1", Collections.emptyList()))
                , null, null);
        team1 = new HierarchyEntity("team1", EntityType.TEAM, "Team 1", "team0",
                Collections.singletonList(new Relation("team0", EntityType.TEAM, "Team 0", null, Collections.emptyList())),
                Collections.singletonList(new Relation("team2", EntityType.TEAM, "Team 2", "team1", Collections.emptyList()))
                , null, null);
        team2 = new HierarchyEntity("team2", EntityType.TEAM, "Team 2", "team1",
                Arrays.asList(new Relation("team0", EntityType.TEAM, "Team 0", null, Collections.emptyList()),
                        new Relation("team1", EntityType.TEAM, "Team 1", "team0", Collections.emptyList())),
                Collections.emptyList()
                , null, null);

        }

    @Test
    public void updateCollectionStats()
        {
        TeamCollectionId teamCollectionId = new TeamCollectionId("team1", LocalDate.of(2019, Month.APRIL.getValue(), 1), ReportingPeriod.MONTH);
        when(mockHierarchyRestRepository.findEntityBySlug(team0.getSlug())).thenReturn(team0);
        when(mockHierarchyRestRepository.findEntityBySlug(team1.getSlug())).thenReturn(team1);
        when(mockHierarchyRestRepository.findEntityBySlug(team2.getSlug())).thenReturn(team2);
        Set<TeamCollectionStat> stats = new HashSet<>();
        stats.add(new TeamCollectionStat("team0", 3, 2019, Month.APRIL.getValue()));
        stats.add(new TeamCollectionStat("team1", 2, 2019, Month.APRIL.getValue()));
        stats.add(new TeamCollectionStat("team2", 1, 2019, Month.APRIL.getValue()));
        when(mockTeamMetricRepository.getCollectionStats(any(), eq(2019), eq(Month.APRIL.getValue()))).thenReturn(stats);

        //Test that reports get delete if they exist.
        when(mockTeamCollectionReportRepository.findById(teamCollectionId))
                .thenReturn(Optional.of(new TeamCollectionReport(teamCollectionId,
                        "team1", LocalDate.of(2019, Month.APRIL.getValue(), 1), ReportingPeriod.MONTH, 3, 1, 0.3d, Collections.emptySet())));

        teamCollectionReportService.updateCollectionStats("team1", 2019, Month.APRIL.getValue());
        //Only one existing report exists, so only one is deleted.
        verify(mockTeamCollectionReportRepository, times(1)).deleteById(teamCollectionId);

        verify(mockTeamCollectionReportRepository, times(3))
                .save(any());
        }

    @Captor
    ArgumentCaptor<String[]> teamIdCaptor;

    @Test
    public void ensureParentTeamIsCountedInStats()
        {
        when(mockHierarchyRestRepository.findEntityBySlug(team0.getSlug())).thenReturn(team0);
        when(mockHierarchyRestRepository.findEntityBySlug(team1.getSlug())).thenReturn(team1);
        when(mockHierarchyRestRepository.findEntityBySlug(team2.getSlug())).thenReturn(team2);

        when(mockHierarchyRestRepository.findEntityBySlug("team-no-ancestors"))
                .thenReturn(new HierarchyEntity("team-no-ancestors", EntityType.TEAM, "Team No Ancestors", null,
                        Collections.emptyList(), Collections.emptyList(), null, null));

        teamCollectionReportService.updateCollectionStats("team-no-ancestors", 2019, Month.APRIL.getValue());

        verify(mockTeamMetricRepository, times(1)).getCollectionStats(teamIdCaptor.capture(), eq(2019), eq(Month.APRIL.getValue()));

        assertThat(teamIdCaptor.getValue(), IsArrayWithSize.arrayWithSize(1));
        }

    @TestConfiguration
    static class TeamMetricServiceTestContextConfiguration
        {

        @MockBean
        private TeamMetricRepository mockTeamMetricRepository;

        @MockBean
        private HierarchyRestRepository mockHierarchyRestRepository;

        @MockBean
        private TeamCollectionReportRepository mockTeamCollectionReportRepository;

        @Bean
        public TeamCollectionReportService teamCollectionReportService()
            {
            return new TeamCollectionReportService(mockTeamMetricRepository, mockHierarchyRestRepository, mockTeamCollectionReportRepository);
            }

        }
    }