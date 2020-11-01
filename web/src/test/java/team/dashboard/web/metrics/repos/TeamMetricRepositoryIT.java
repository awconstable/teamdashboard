package team.dashboard.web.metrics.repos;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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


@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TeamMetricRepositoryIT
    {

    @Autowired
    private TeamMetricRepository repository;

    @BeforeEach
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
            createDate(2019, Month.JANUARY, 3)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("2.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 5)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("1.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 12)));
    ratings.add(new TeamMetric("team3", TeamMetricType.CYCLE_TIME, Double.valueOf("5.0"), Double.valueOf("1.0"),
            createDate(2019, Month.JANUARY, 15)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_AUTOMATION_COVERAGE, Double.valueOf("10.0"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 3)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, Double.valueOf("100"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 3)));
    ratings.add(new TeamMetric("team4", TeamMetricType.TEST_TOTAL_EXECUTION_COUNT, Double.valueOf("1000"), Double.valueOf("1.0"),
            createDate(2019, Month.MARCH, 3)));
        repository.saveAll(ratings);
    }

    @AfterEach
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
                createDate(2019, Month.JANUARY, 1),
                createDate(2019, Month.JANUARY, 30));

        AssertionsForClassTypes.assertThat(times.size()).isEqualTo(5);
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

        //TODO check calculated values
        //TODO Dates and timezones cause a problem. Need to move away from LocalDate
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

    @Test
    public void getTeamCollectionStatsMultiTeam()
        {
        Set<TeamCollectionStat> stats = repository.getCollectionStats(new String[]{"team4", "team3", "team2", "team1"}, 2019, Month.JANUARY.getValue());

        assertThat(stats.size(), is(equalTo(2)));
       
        for(TeamCollectionStat stat:stats){
            int teamCount = 0;
            if(stat.getTeamId().getTeamId().equals("team2")){
                teamCount = 1;
            } else if (stat.getTeamId().getTeamId().equals("team3")){
                teamCount = 4;
            }
            assertThat(stat.getCount(), is(equalTo(teamCount)));
        }
        }
}
