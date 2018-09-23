package team.dashboard.web.metrics;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@DataMongoTest
public class TeamMetricRepositoryTest
    {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TeamMetricRepository repository;

    @Before
    public void setUp() {

    List<TeamMetric> ratings = new ArrayList<>();
    ratings.add(new TeamMetric("testid", TeamMetricType.CYCLE_TIME, 3,
                createDate(2018, Month.JANUARY, 05)));
    ratings.add(new TeamMetric("testid", TeamMetricType.CYCLE_TIME, 5,
                createDate(2018, Month.JANUARY, 29)));
    ratings.add(new TeamMetric("fspbm", TeamMetricType.CYCLE_TIME, 1,
                createDate(2018, Month.FEBRUARY, 18)));
    ratings.add(new TeamMetric("fspbm", TeamMetricType.CYCLE_TIME, 3,
                createDate(2018, Month.MARCH, 05)));

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

        List<TeamMetric> metrics = repository.findByTeamIdIgnoreCaseAndTeamMetricTypeOrderByDateDesc("fspbm", TeamMetricType.CYCLE_TIME);

        AssertionsForClassTypes.assertThat(metrics.size()).isEqualTo(2);
    }

    @Test
    public void findAllTest()
        {

        List<TeamMetric> times = repository.findAll();

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(4);
    }

    @Test
    public void findByDateRangeTest()
        {

        List<TeamMetric> times = repository.findByTeamMetricTypeAndDateBetweenOrderByDateDesc(TeamMetricType.CYCLE_TIME,
                createDate(2018, Month.JANUARY, 01),
                createDate(2018, Month.JANUARY, 30));

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest()
        {

        List<TeamMetric> times = repository.findByTeamIdAndTeamMetricTypeAndDateBetweenOrderByDateDesc(
                "testid",
                TeamMetricType.CYCLE_TIME,
                createDate(2018, Month.JANUARY, 01),
                createDate(2018, Month.JANUARY, 20));

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(1);
    }
}
