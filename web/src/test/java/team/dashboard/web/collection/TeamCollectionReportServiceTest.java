package team.dashboard.web.collection;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.repos.TeamMetricRepository;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TeamCollectionReportServiceTest
    {

    @Autowired
    private TeamRestRepository mockTeamRepository;
    @Autowired
    private TeamMetricRepository mockTeamMetricRepository;
    @Autowired
    private TeamCollectionReportService teamCollectionReportService;
    @Autowired
    private TeamCollectionReportRepository mockTeamCollectionReportRepository;
    private Team team0;
    private Team team1;
    private Team team2;

    @Before
    public void setUp()
        {
        team0 = new Team("team0", "Team 0", null,
                null,
                Arrays.asList(new TeamRelation("team1", "Team 1", "team0", Collections.emptyList()),
                        new TeamRelation("team2", "Team 2", "team1", Collections.emptyList()))
                , null, null);
        team1 = new Team("team1", "Team 1", "team0",
                Collections.singletonList(new TeamRelation("team0", "Team 0", null, Collections.emptyList())),
                Collections.singletonList(new TeamRelation("team2", "Team 2", "team1", Collections.emptyList()))
                , null, null);
        team2 = new Team("team2", "Team 2", "team1",
                Arrays.asList(new TeamRelation("team0", "Team 0", null, Collections.emptyList()),
                        new TeamRelation("team1", "Team 1", "team0", Collections.emptyList())),
                Collections.emptyList()
                , null, null);

        }

    @Test
    public void updateCollectionStats()
        {
        TeamCollectionId teamCollectionId = new TeamCollectionId("team1", LocalDate.of(2019, Month.APRIL.getValue(), 1), ReportingPeriod.MONTH);
        when(mockTeamRepository.findByTeamSlug(team0.getSlug())).thenReturn(team0);
        when(mockTeamRepository.findByTeamSlug(team1.getSlug())).thenReturn(team1);
        when(mockTeamRepository.findByTeamSlug(team2.getSlug())).thenReturn(team2);
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
        when(mockTeamRepository.findByTeamSlug(team0.getSlug())).thenReturn(team0);
        when(mockTeamRepository.findByTeamSlug(team1.getSlug())).thenReturn(team1);
        when(mockTeamRepository.findByTeamSlug(team2.getSlug())).thenReturn(team2);

        when(mockTeamRepository.findByTeamSlug("team-no-ancestors"))
                .thenReturn(new Team("team-no-ancestors", "Team No Ancestors", null,
                        Collections.emptyList(), Collections.emptyList(), null, null));

        teamCollectionReportService.updateCollectionStats("team-no-ancestors", 2019, Month.APRIL.getValue());

        verify(mockTeamMetricRepository, times(1)).getCollectionStats(teamIdCaptor.capture(), eq(2019), eq(Month.APRIL.getValue()));

        assertThat(teamIdCaptor.getValue(), IsArrayWithSize.emptyArray());
        }

    @TestConfiguration
    static class TeamMetricServiceTestContextConfiguration
        {

        @MockBean
        private TeamMetricRepository mockTeamMetricRepository;

        @MockBean
        private TeamRestRepository mockTeamRepository;

        @MockBean
        private TeamCollectionReportRepository mockTeamCollectionReportRepository;

        @Bean
        public TeamCollectionReportService teamCollectionReportService()
            {
            return new TeamCollectionReportService(mockTeamMetricRepository, mockTeamRepository, mockTeamCollectionReportRepository);
            }

        }
    }