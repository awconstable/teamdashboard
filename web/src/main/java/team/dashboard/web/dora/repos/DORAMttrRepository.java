package team.dashboard.web.dora.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.dashboard.web.dora.domain.MTTR;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DORAMttrRepository extends MongoRepository<MTTR, String>
    {
        List<MTTR> findByApplicationId(String applicationId);

        Optional<MTTR> findByApplicationIdAndReportingDate(String applicationId, Date reportingDate);
    }
