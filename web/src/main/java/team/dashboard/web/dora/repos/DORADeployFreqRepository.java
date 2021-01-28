package team.dashboard.web.dora.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.dashboard.web.dora.domain.DeploymentFrequency;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DORADeployFreqRepository extends MongoRepository<DeploymentFrequency, String>
    {
        List<DeploymentFrequency> findByApplicationId(String applicationId);
        
        Optional<DeploymentFrequency> findByApplicationIdAndReportingDate(String applicationId, Date reportingDate);
    }
