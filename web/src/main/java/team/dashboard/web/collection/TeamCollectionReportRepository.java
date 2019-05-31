package team.dashboard.web.collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "teammcollection", path = "teammcollection")
public interface TeamCollectionReportRepository extends MongoRepository<TeamCollectionReport, TeamCollectionId>
    {
    }
