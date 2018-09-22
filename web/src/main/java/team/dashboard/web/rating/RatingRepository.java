package team.dashboard.web.rating;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by awconstable on 13/12/2017.
 */
@RepositoryRestResource(collectionResourceRel = "rating", path = "rating")
public interface RatingRepository extends MongoRepository<Rating, String>
    {
        List<Rating> findByTeamIdIgnoreCase(String teamId);

        List<Rating> findByRatingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

        List<Rating> findByTeamIdAndRatingDateBetween(String teamId, LocalDateTime startDate, LocalDateTime endDate);

    }
