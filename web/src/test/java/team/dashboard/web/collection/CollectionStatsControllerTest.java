package team.dashboard.web.collection;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.hierarchy.EntityType;
import team.dashboard.web.hierarchy.HierarchyRestRepository;
import team.dashboard.web.hierarchy.Relation;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CollectionStatsController.class)
public class CollectionStatsControllerTest
    {

    @Captor
    ArgumentCaptor<List<String>> teamIdCaptor;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HierarchyRestRepository mockHierarchyRepository;
    @MockBean
    private TeamCollectionReportRepository teamCollectionReportRepository;
    @MockBean
    private TeamCollectionReportService teamCollectionReportService;
    @MockBean
    private TeamMetricRepository mockTeamMetricRepository;

    @Test
    public void checkChartCollectionGraphsIncludeChildrenAndParent() throws Exception
        {
        Relation team = new Relation("team1", EntityType.TEAM, "Team 1", null,
                Collections.singletonList(new Relation("team2", EntityType.TEAM, "Team 2", "team1",
                        Collections.singletonList(new Relation("team3", EntityType.TEAM, "Team 3", "team2", Collections.emptyList())))));

        when(mockHierarchyRepository.findHierarchyBySlug("team1")).thenReturn(team);

        mockMvc.perform(get("/collection-stats/team1/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(teamCollectionReportRepository, times(1)).findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(teamIdCaptor.capture(), eq(ReportingPeriod.MONTH), eq(LocalDate.now().minusMonths(12).atStartOfDay()), eq(LocalDate.now().atStartOfDay()));

        assertThat(teamIdCaptor.getValue(), IsCollectionWithSize.hasSize(2));
        assertThat(teamIdCaptor.getValue(), IsIterableContaining.hasItems("team1", "team2"));
        }
    }