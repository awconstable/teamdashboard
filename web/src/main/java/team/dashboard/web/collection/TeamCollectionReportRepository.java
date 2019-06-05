package team.dashboard.web.collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "teamcollection", path = "teamcollection")
public interface TeamCollectionReportRepository extends MongoRepository<TeamCollectionReport, TeamCollectionId>
    {
    List<TeamCollectionReport> findByIdTeamIdInAndIdReportingPeriodAndIdReportingDateGreaterThanEqualAndReportingDateLessThanEqualOrderByReportingDateDesc(List<String> teamIds, ReportingPeriod period, LocalDateTime startDate, LocalDateTime endDate);
    }
