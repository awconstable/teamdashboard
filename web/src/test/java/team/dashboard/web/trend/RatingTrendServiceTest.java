package team.dashboard.web.trend;

import team.dashboard.web.rating.Rating;
import team.dashboard.web.rating.RatingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingTrendServiceTest {

    @MockBean
    private RatingRepository repository;

    @Autowired
    private RatingTrendService service;

    @Test
    public void rollingTrendTest() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("test", 1, LocalDateTime.now()));
        ratings.add(new Rating("test", 4, LocalDateTime.now()));
        given(this.repository.findByRatingDateBetween(isA(LocalDateTime.class),isA(LocalDateTime.class))).willReturn(ratings);

        ArrayList<RatingTrend> trends = service.getTrendData("all");

        assertThat(trends.size(), is(12));

        assertThat(trends.get(0).getAvgRating(), is(2.5));
    }


}
