package team.dashboard.web.metrics.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamMetricServiceImplIT
    {
    @Autowired
    private TeamMetricRepository repository;

    @Autowired
    private TeamMetricService teamMetricService;

    @Before
    public void setUp()
        {

        List<TeamMetric> ratings = new ArrayList<>();
        ratings.add(new TeamMetric("team1", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, 100d,
                LocalDate.of(2018, Month.JANUARY, 5)));
        //ratings.add(new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, 50d,
        //        LocalDate.of(2018, Month.JANUARY, 5)));

        repository.saveAll(ratings);
        }

    @After
    public void clearDown()
        {
        repository.deleteAll();
        }

    @Test
    public void checkTestCoverageStorage()
        {

        teamMetricService.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", LocalDate.of(2018, Month.JANUARY, 5), 50d);

        List<TeamMetric> all = repository.findAll();

        assertThat(all.size(), is(equalTo(3)));

        Optional<TeamMetric> metric = repository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, LocalDate.of(2018, Month.JANUARY, 5));

        assertThat(metric.get().getValue(), is(equalTo(50d)));
        }

    @Test
    public void checkTestCoverageStorageWhenExists()
        {
        repository.save(new TeamMetric("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, 50d, LocalDate.of(2018, Month.JANUARY, 5)));

        teamMetricService.save(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT.getKey(), "team1", LocalDate.of(2018, Month.JANUARY, 5), 50d);

        List<TeamMetric> all = repository.findAll();

        assertThat(all.size(), is(equalTo(3)));

        Optional<TeamMetric> metric = repository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.TEST_AUTOMATION_COVERAGE, LocalDate.of(2018, Month.JANUARY, 5));

        assertThat(metric.get().getValue(), is(equalTo(50d)));
        }
    }
