package team.dashboard.web.dora.repos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.LeadTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class DORALeadTimeRepositoryTest
    {
    
    @Autowired
    private DORALeadTimeRepository repo;
    
    @BeforeEach
    void setUp()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        List<LeadTime> leadTimeList = new ArrayList<>();
        LeadTime lt1 = new LeadTime("app1", Date.from(reportingDate.toInstant()), 12000L, DORALevel.ELITE);
        LeadTime lt2 = new LeadTime("app1", Date.from(reportingDate.minusDays(1).toInstant()), 12000L, DORALevel.HIGH);
        LeadTime lt3 = new LeadTime("app2", Date.from(reportingDate.minusDays(2).toInstant()), 12000L, DORALevel.LOW);
        leadTimeList.add(lt1);
        leadTimeList.add(lt2);
        leadTimeList.add(lt3);
        repo.saveAll(leadTimeList);
        }

    @AfterEach
    void tearDown()
        {
        repo.deleteAll();
        }

    @Test
    void findByApplicationId()
        {
        List<LeadTime> leadTimes = repo.findByApplicationId("app1");
        assertThat(leadTimes.size(), is(equalTo(2)));
        leadTimes.forEach(lt -> assertThat(lt.getApplicationId(), is(equalTo("app1"))));
        }

    @Test
    void findByApplicationIdAndReportingDate()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<LeadTime> freq = repo.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        assertThat(freq.isPresent(), is(true));
        assertThat(freq.get().getApplicationId(), is(equalTo("app1")));
        }
    }