package team.dashboard.web.dora.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.dashboard.web.dora.domain.ChangeFailureRate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DORAChangeFailureRateRepository extends MongoRepository<ChangeFailureRate, String>
    {
    List<ChangeFailureRate> findByApplicationId(String applicationId);

    Optional<ChangeFailureRate> findByApplicationIdAndReportingDate(String applicationId, Date reportingDate);
    }
