package team.dashboard.web.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Created by awconstable on 12/02/2017.
 */
@RestController
public class RatingController
    {

    @Autowired
    private RatingRepository repository;

    @RequestMapping("/satisfaction/{teamId}/{ratingSubmission}")
    public void rating(@PathVariable String teamId, @PathVariable int ratingSubmission, HttpServletResponse response) throws Exception
        {

            Rating rating = new Rating(teamId, ratingSubmission, LocalDateTime.now());
            repository.save(rating);
            response.sendRedirect("/?team=" + teamId + "&success=true");
        }

    }