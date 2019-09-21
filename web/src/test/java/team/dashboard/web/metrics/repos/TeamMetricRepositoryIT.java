package team.dashboard.web.metrics.repos;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricTrend;
import team.dashboard.web.metrics.TeamMetricType;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class TeamMetricRepositoryIT
    {

    @Autowired
    private TeamMetricRepository repository;

    @Before
    public void setUp() {

    List<TeamMetric> ratings = new ArrayList<>();
    ratings.add(new TeamMetric("team1", TeamMetricType.CYCLE_TIME, Double.valueOf("3.0"), Double.valueOf("1.0"),
            createDate(2018, Month.JANUARY, 5)));
    ratings.add(new TeamMetric("team1", TeamMetricType.CYCLE_TIME, Double.valueOf("5"), Double.valueOf("1.0"),
                createDate(2018, Month.JANUARY, 29)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, Double.valueOf("1"), Double.valueOf("1.0"),
                createDate(2018, Month.FEBRUARY, 18)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, Double.valueOf("3.0"), Double.valueOf("1.0"),
            createDate(2018, Month.MARCH, 5)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, Double.valueOf("4.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 12)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("4.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 1)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("2.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 5)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("1.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 12)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("5.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 15)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_AUTOMATION_COVERAGE, Double.valueOf("10.0"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 1)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, Double.valueOf("100"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 1)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, Double.valueOf("1000"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 1)));
        repository.saveAll(ratings);
    }

    @After
    public void clearDown() {
        repository.deleteAll();
    }

    private LocalDate createDate(Integer year, Month month, Integer day){
        return LocalDate.of(year, month, day);
    }

    @Test
    public void findByTeamIdTest()
        {

        List<TeamMetric> metrics = repository.findByTeamIdIgnoreCaseAndTeamMetricTypeOrderByDateDesc("team2", TeamMetricType.CYCLE_TIME);

        AssertionsForClassTypes.assertThat(metrics.size()).isEqualTo(3);
    }

    @Test
    public void findAllTest()
        {

        List<TeamMetric> times = repository.findAll();

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(12);
    }

    @Test
    public void findByDateRangeTest()
        {

        List<TeamMetric> times = repository.findByTeamMetricTypeAndDateBetweenOrderByDateDesc(TeamMetricType.CYCLE_TIME,
                createDate(2018, Month.JANUARY, 1),
                createDate(2018, Month.JANUARY, 30));

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest()
        {

        List<TeamMetric> times = repository.findByTeamIdAndTeamMetricTypeAndDateBetweenOrderByDateDesc(
                "team1",
                TeamMetricType.CYCLE_TIME,
                createDate(2018, Month.JANUARY, 1),
                createDate(2018, Month.JANUARY, 20));

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(1);
    }

    @Test
    public void getMonthlyMetricsTest()
        {

        List<TeamMetricTrend> metrics = repository.getMonthlyMetrics("team3", TeamMetricType.CYCLE_TIME);

        //TODO check calculated values
        AssertionsForClassTypes.assertThat(metrics.size()).isEqualTo(1);
        }

    @Test
    public void getMonthlyChildMetricsTest()
        {

        List<TeamMetricTrend> metrics = repository.getMonthlyChildMetrics(new String[]{"team3", "team2", "team1"}, TeamMetricType.CYCLE_TIME);

        for (TeamMetricTrend trend : metrics)
            {
            System.out.println(trend);
            }

        //TODO check calculated values
        AssertionsForClassTypes.assertThat(metrics.size()).isEqualTo(4);
        }

    //Make sure TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT and
    // TeamMetricType.TEST_TOTAL_EXECUTION_COUNT are excluded from the collection count
    @Test
    public void getTeamCollectionStatsTestAutomationExcluded()
        {
        Set<TeamCollectionStat> stats = repository.getCollectionStats(new String[]{"team4"}, 2019, Month.MARCH.getValue());

        assertThat(stats.size(), is(equalTo(1)));
        assertThat(stats.iterator().next().getCount(), is(equalTo(1)));
        }
}
