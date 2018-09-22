package team.dashboard.web.rating;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Month;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class RatingRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RatingRepository repository;

    @Before
    public void setUp() {

        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("testid", 3,
                createDate(2018, Month.JANUARY, 05, 06, 30)));
        ratings.add(new Rating("testid", 5,
                createDate(2018, Month.JANUARY, 29, 06, 30)));
        ratings.add(new Rating("fspbm", 1,
                createDate(2018, Month.FEBRUARY, 18, 06, 30)));
        ratings.add(new Rating("fspbm", 3,
                createDate(2018, Month.MARCH, 05, 06, 30)));

        repository.saveAll(ratings);
    }

    @After
    public void clearDown() {
        repository.deleteAll();
    }

    private LocalDateTime createDate(Integer year, Month month, Integer day, Integer hour, Integer minute){
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Test
    public void findByTeamIdTest() throws Exception {

        List<Rating> Ratings = repository.findByTeamIdIgnoreCase("fspbm");

        assertThat(Ratings.size()).isEqualTo(2);
    }

    @Test
    public void findAllTest() throws Exception {

        List<Rating> Ratings = repository.findAll();

        assertThat(Ratings.size()).isEqualTo(4);
    }

    @Test
    public void findByDateRangeTest() throws Exception {

        List<Rating> Ratings = repository.findByRatingDateBetween(
                createDate(2018, Month.JANUARY, 01, 04, 30),
                createDate(2018, Month.JANUARY, 30, 22, 30));

        assertThat(Ratings.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest() throws Exception {

        List<Rating> Ratings = repository.findByTeamIdAndRatingDateBetween(
                "testid",
                createDate(2018, Month.JANUARY, 01, 04, 30),
                createDate(2018, Month.JANUARY, 20, 22, 30));

        assertThat(Ratings.size()).isEqualTo(1);
    }
}
