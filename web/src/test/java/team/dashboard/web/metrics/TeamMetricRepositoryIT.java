package team.dashboard.web.metrics;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@DataMongoTest
public class TeamMetricRepositoryIT
    {

    @Autowired
    private TeamMetricRepository repository;

    @Before
    public void setUp() {

    List<TeamMetric> ratings = new ArrayList<>();
    ratings.add(new TeamMetric("team1", TeamMetricType.CYCLE_TIME, new Double("3.0"),
            createDate(2018, Month.JANUARY, 5)));
    ratings.add(new TeamMetric("team1", TeamMetricType.CYCLE_TIME, new Double("5"),
                createDate(2018, Month.JANUARY, 29)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, new Double("1"),
                createDate(2018, Month.FEBRUARY, 18)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, new Double("3.0"),
            createDate(2018, Month.MARCH, 5)));
    ratings.add(new TeamMetric("team2", TeamMetricType.CYCLE_TIME, new Double("4.0"),
            createDate(2019, Month.JANUARY, 12)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, new Double("4.0"),
            createDate(2019, Month.JANUARY, 1)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, new Double("2.0"),
            createDate(2019, Month.JANUARY, 5)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, new Double("1.0"),
            createDate(2019, Month.JANUARY, 12)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, new Double("5.0"),
            createDate(2019, Month.JANUARY, 15)));

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

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(9);
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
}
