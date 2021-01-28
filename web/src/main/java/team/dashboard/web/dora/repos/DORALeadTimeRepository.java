package team.dashboard.web.dora.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.dashboard.web.dora.domain.LeadTime;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DORALeadTimeRepository extends MongoRepository<LeadTime, String>
    {
        List<LeadTime> findByApplicationId(String applicationId);

        Optional<LeadTime> findByApplicationIdAndReportingDate(String applicationId, Date reportingDate);
    }
