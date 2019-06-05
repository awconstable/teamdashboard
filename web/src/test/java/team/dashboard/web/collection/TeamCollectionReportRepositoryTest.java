package team.dashboard.web.collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.dashboard.web.metrics.TeamCollectionStat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataMongoTest
public class TeamCollectionReportRepositoryTest
    {

    @Autowired
    private TeamCollectionReportRepository repository;

    @Before
    public void setUp() throws Exception
        {
        String teamId = "team1";
        LocalDate reportingDate = LocalDate.of(2019, 2, 1);
        Set<TeamCollectionStat> stats = new HashSet();
        TeamCollectionReport report1 = new TeamCollectionReport(
                new TeamCollectionId(teamId, reportingDate, ReportingPeriod.MONTH)
                , teamId, reportingDate, ReportingPeriod.MONTH, 5,
                1, 0.2, stats);

        repository.save(report1);
        }

    @Test
    public void findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateBetweenTest()
        {
        LocalDate startDate = LocalDate.of(2019, 2, 1);
        LocalDate endDate = LocalDate.of(2019, 2, 1).plusMonths(1).minusDays(1);
        List<TeamCollectionReport> allReports = repository.findAll();
        assertThat(allReports.size(), is(equalTo(1)));
        List<TeamCollectionReport> reports = repository.findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(Arrays.asList("team1"), ReportingPeriod.MONTH, startDate.atStartOfDay(), endDate.atStartOfDay());
        assertThat(reports.size(), is(equalTo(1)));
        }
    }