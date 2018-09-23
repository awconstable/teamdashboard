package team.dashboard.web.metrics;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "cycletime", path = "cycletime")
public interface CycleTimeRepository extends MongoRepository<CycleTime, String>
    {
        List<CycleTime> findByTeamIdIgnoreCaseOrderByDateDesc(String teamId);

        List<CycleTime> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

        List<CycleTime> findByTeamIdAndDateBetweenOrderByDateDesc(String teamId, LocalDate startDate, LocalDate endDate);

        Optional<CycleTime> findByTeamIdAndDate(String teamId, LocalDate date);

    }
