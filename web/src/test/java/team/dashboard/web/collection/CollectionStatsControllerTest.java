package team.dashboard.web.collection;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.metrics.repos.TeamMetricRepository;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CollectionStatsController.class)
public class CollectionStatsControllerTest
    {

    @Captor
    ArgumentCaptor<List<String>> teamIdCaptor;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamRestRepository mockTeamRepository;
    @MockBean
    private TeamCollectionReportRepository teamCollectionReportRepository;
    @MockBean
    private TeamCollectionReportService teamCollectionReportService;
    @MockBean
    private TeamMetricRepository mockTeamMetricRepository;

    @Test
    public void checkChartCollectionGraphsIncludeChildrenAndParent() throws Exception
        {
        TeamRelation team = new TeamRelation("team1", "Team 1", null,
                Collections.singletonList(new TeamRelation("team2", "Team 2", "team1",
                        Collections.singletonList(new TeamRelation("team3", "Team 3", "team2", Collections.emptyList())))));

        when(mockTeamRepository.findTeamHierarchyBySlug("team1")).thenReturn(team);

        mockMvc.perform(get("/collection-stats/team1/").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(teamCollectionReportRepository, times(1)).findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(teamIdCaptor.capture(), eq(ReportingPeriod.MONTH), eq(LocalDate.now().minusMonths(12).atStartOfDay()), eq(LocalDate.now().atStartOfDay()));

        assertThat(teamIdCaptor.getValue(), IsCollectionWithSize.hasSize(2));
        assertThat(teamIdCaptor.getValue(), IsCollectionContaining.hasItems("team1", "team2"));
        }
    }