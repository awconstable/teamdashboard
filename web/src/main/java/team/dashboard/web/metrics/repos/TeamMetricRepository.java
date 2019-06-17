package team.dashboard.web.metrics.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import team.dashboard.web.metrics.TeamMetric;
import team.dashboard.web.metrics.TeamMetricType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "teammetric", path = "teammetric")
public interface TeamMetricRepository extends MongoRepository<TeamMetric, String>, TeamMetricAggregationRepository
    {
    List<TeamMetric> findByTeamIdIgnoreCaseAndTeamMetricTypeOrderByDateDesc(String teamId, TeamMetricType type);

    List<TeamMetric> findByTeamIdIgnoreCaseAndDate(String teamId, LocalDate date);

    List<TeamMetric> findByTeamMetricTypeAndDateBetweenOrderByDateDesc(TeamMetricType type, LocalDate startDate, LocalDate endDate);

    List<TeamMetric> findByTeamIdAndTeamMetricTypeAndDateBetweenOrderByDateDesc(String teamId, TeamMetricType type, LocalDate startDate, LocalDate endDate);

    Optional<TeamMetric> findByTeamIdAndTeamMetricTypeAndDate(String teamId, TeamMetricType type, LocalDate date);

    }
