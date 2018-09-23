package team.dashboard.web.rating;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import team.dashboard.web.metrics.CycleTime;
import team.dashboard.web.metrics.CycleTimeRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class CycleTimeRepositoryTest
    {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CycleTimeRepository repository;

    @Before
    public void setUp() {

        List<CycleTime> ratings = new ArrayList<>();
        ratings.add(new CycleTime("testid", 3,
                createDate(2018, Month.JANUARY, 05)));
        ratings.add(new CycleTime("testid", 5,
                createDate(2018, Month.JANUARY, 29)));
        ratings.add(new CycleTime("fspbm", 1,
                createDate(2018, Month.FEBRUARY, 18)));
        ratings.add(new CycleTime("fspbm", 3,
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
    public void findByTeamIdTest() throws Exception {

        List<CycleTime> times = repository.findByTeamIdIgnoreCaseOrderByDateDesc("fspbm");

        assertThat(times.size()).isEqualTo(2);
    }

    @Test
    public void findAllTest() throws Exception {

        List<CycleTime> times = repository.findAll();

        assertThat(times.size()).isEqualTo(4);
    }

    @Test
    public void findByDateRangeTest() throws Exception {

        List<CycleTime> times = repository.findByDateBetweenOrderByDateDesc(
                createDate(2018, Month.JANUARY, 01),
                createDate(2018, Month.JANUARY, 30));

        assertThat(times.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest() throws Exception {

        List<CycleTime> times = repository.findByTeamIdAndDateBetweenOrderByDateDesc(
                "testid",
                createDate(2018, Month.JANUARY, 01),
                createDate(2018, Month.JANUARY, 20));

        assertThat(times.size()).isEqualTo(1);
    }
}
