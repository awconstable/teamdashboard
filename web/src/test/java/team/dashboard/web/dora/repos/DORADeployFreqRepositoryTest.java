package team.dashboard.web.dora.repos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dashboard.web.BaseMongoTest;
import team.dashboard.web.dora.domain.DORALevel;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.TimePeriod;

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

class DORADeployFreqRepositoryTest extends BaseMongoTest
    {
    
    @Autowired
    private DORADeployFreqRepository doraDeployFreqRepository;
    
    @BeforeEach
    void setUp()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        List<DeploymentFrequency> dfList = new ArrayList<>();
        DeploymentFrequency df1 = new DeploymentFrequency("app1", Date.from(reportingDate.toInstant()), 10, TimePeriod.DAY, DORALevel.ELITE );
        DeploymentFrequency df2 = new DeploymentFrequency("app1", Date.from(reportingDate.minusDays(1).toInstant()), 10, TimePeriod.DAY, DORALevel.HIGH );
        DeploymentFrequency df3 = new DeploymentFrequency("app2", Date.from(reportingDate.minusDays(2).toInstant()), 10, TimePeriod.DAY, DORALevel.LOW );
        dfList.add(df1);
        dfList.add(df2);
        dfList.add(df3);
        doraDeployFreqRepository.saveAll(dfList);
        }

    @AfterEach
    void tearDown()
        {
        doraDeployFreqRepository.deleteAll();
        }
    
    @Test
    void findByApplicationId()
        {
        List<DeploymentFrequency> freq = doraDeployFreqRepository.findByApplicationId("app1");
        assertThat(freq.size(), is(equalTo(2)));
        freq.forEach(fr -> assertThat(fr.getApplicationId(), is(equalTo("app1"))));
        }

    @Test
    void findByApplicationIdAndReportingDate()
        {
        ZonedDateTime reportingDate = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        Optional<DeploymentFrequency> freq = doraDeployFreqRepository.findByApplicationIdAndReportingDate("app1", Date.from(reportingDate.toInstant()));
        assertThat(freq.isPresent(), is(true));
        assertThat(freq.get().getApplicationId(), is(equalTo("app1")));
        }
}