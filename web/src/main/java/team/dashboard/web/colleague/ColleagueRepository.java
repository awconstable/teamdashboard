package team.dashboard.web.colleague;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by awconstable on 20/02/2017.
 */
@RepositoryRestResource(collectionResourceRel = "colleague", path = "colleague")
public interface ColleagueRepository extends MongoRepository<Colleague, String>
    {
    List<Colleague> findByTeamId(@Param("teamId") String teamId);

    List<Colleague> findByEmailIgnoreCase(@Param("email") String email);
    }
