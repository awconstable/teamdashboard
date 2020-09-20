package team.dashboard.web.metrics.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.collection.TeamCollectionReportService;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;
import team.dashboard.web.metrics.repos.TeamMetricRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TeamMetricServiceImplIT
    {
    @Autowired
    private TeamMetricRepository repository;

    @Autowired
    private TeamMetricService teamMetricService;

    @MockBean
    private TeamCollectionReportService teamCollectionReportService;

    @BeforeEach
    public void setUp()
        {

        List<TeamMetric> ratings = new ArrayList<>();
        ratings.add(new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 100d, 10d,
                LocalDate.of(2018, Month.JANUARY, 5)));

        repository.saveAll(ratings);
        }

    @AfterEach
    public void clearDown()
        {
        repository.deleteAll();
        }

    @Test
    public void checkTestCoverageStorage()
        {

        teamMetricService.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", LocalDate.of(2018, Month.JANUARY, 5), 50d, 10d);

        List<TeamMetric> all = repository.findAll();

        assertThat(all.size(), is(equalTo(3)));

        Optional<TeamMetric> metric = repository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, LocalDate.of(2018, Month.JANUARY, 5));

        assertThat(metric.isPresent(), is(true));
        assertThat(metric.get().getValue(), is(equalTo(50d)));
        assertThat(metric.get().getTarget(), is(equalTo(100d)));
        }

    @Test
    public void checkTestCoverageStorageWhenExists()
        {
        repository.save(new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 50d, 10d, LocalDate.of(2018, Month.JANUARY, 5)));

        teamMetricService.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", LocalDate.of(2018, Month.JANUARY, 5), 50d, 10d);

        List<TeamMetric> all = repository.findAll();

        assertThat(all.size(), is(equalTo(3)));

        Optional<TeamMetric> metric = repository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, LocalDate.of(2018, Month.JANUARY, 5));

        assertThat(metric.isPresent(), is(true));
        assertThat(metric.get().getValue(), is(equalTo(50d)));
        assertThat(metric.get().getTarget(), is(equalTo(100d)));
        }
    }
