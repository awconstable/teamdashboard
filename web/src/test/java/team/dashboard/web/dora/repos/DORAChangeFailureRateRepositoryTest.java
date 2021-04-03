package team.dashboard.web.dora.repos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dashboard.web.BaseMongoTest;
import team.dashboard.web.dora.domain.ChangeFailureRate;
import team.dashboard.web.dora.domain.DORALevel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DORAChangeFailureRateRepositoryTest extends BaseMongoTest
    {
    @Autowired
    private DORAChangeFailureRateRepository repo;
    
    @BeforeEach
    void setUp()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));

        ChangeFailureRate cfr1 = new ChangeFailureRate("app1", Date.from(reportingDate.toInstant()), 0.0, 4, DORALevel.ELITE);
        ChangeFailureRate cfr2 = new ChangeFailureRate("app1", Date.from(reportingDate.minusDays(1).toInstant()), 0.12, 4, DORALevel.ELITE);
        ChangeFailureRate cfr3 = new ChangeFailureRate("app2", Date.from(reportingDate.minusDays(2).toInstant()), 0.5, 4, DORALevel.LOW);

        repo.saveAll(Arrays.asList(cfr1, cfr2, cfr3));
        }

    @AfterEach
    void tearDown()
        {
        repo.deleteAll();
        }
    
    @Test
    void findByApplicationId()
        {
        List<ChangeFailureRate> cfrs = repo.findByApplicationId("app1");
        assertThat(cfrs.size(), is(equalTo(2)));
        cfrs.forEach(cfr -> assertThat(cfr.getApplicationId(), is(equalTo("app1"))));
        }

    @Test
    void findByApplicationIdAndReportingDate()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<ChangeFailureRate> cfr = repo.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        assertThat(cfr.isPresent(), is(true));
        assertThat(cfr.get().getApplicationId(), is(equalTo("app1")));
        }
    
    @Test
    void deleteExistingData()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<ChangeFailureRate> cfr = repo.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        cfr.ifPresent(cfrObj -> repo.delete(cfrObj));
        assertThat(cfr.isPresent(), is(true));
        assertThat(cfr.get().getApplicationId(), is(equalTo("app1")));
        }
    }