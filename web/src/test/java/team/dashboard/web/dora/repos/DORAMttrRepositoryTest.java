package team.dashboard.web.dora.repos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dashboard.web.BaseMongoTest;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.MTTR;

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

class DORAMttrRepositoryTest extends BaseMongoTest
    {
    
    @Autowired
    private DORAMttrRepository repo;
    
    @BeforeEach
    void setUp()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        List<MTTR> mttrList = new ArrayList<>();
        MTTR mttr1 = new MTTR("app1", Date.from(reportingDate.toInstant()), 1200, 4, DORALevel.ELITE);
        MTTR mttr2 = new MTTR("app1", Date.from(reportingDate.minusDays(1).toInstant()), 1200, 4, DORALevel.HIGH);
        MTTR mttr3 = new MTTR("app2", Date.from(reportingDate.minusDays(2).toInstant()), 1200, 4, DORALevel.LOW);
        mttrList.add(mttr1);
        mttrList.add(mttr2);
        mttrList.add(mttr3);
        repo.saveAll(mttrList);
        }

    @AfterEach
    void tearDown()
        {
        repo.deleteAll();
        }

    @Test
    void findByApplicationId()
        {
        List<MTTR> mttrs = repo.findByApplicationId("app1");
        assertThat(mttrs.size(), is(equalTo(2)));
        mttrs.forEach(mttr -> assertThat(mttr.getApplicationId(), is(equalTo("app1"))));
        }

    @Test
    void findByApplicationIdAndReportingDate()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<MTTR> mttr = repo.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        assertThat(mttr.isPresent(), is(true));
        assertThat(mttr.get().getApplicationId(), is(equalTo("app1")));
        }

    @Test
    void deleteExistingData()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<MTTR> mttr = repo.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        mttr.ifPresent(mttrObj -> repo.delete(mttrObj));
        assertThat(mttr.isPresent(), is(true));
        assertThat(mttr.get().getApplicationId(), is(equalTo("app1")));
        }
    }