package team.dashboard.web.rating;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;


public class Rating
    {
    @Id
    private String id;
    private final String teamId;
    private final Integer rating;
    private final LocalDateTime ratingDate;

    public Rating(String teamId, Integer rating, LocalDateTime ratingDate)
        {
        this.teamId = teamId;
        this.rating = rating;
        this.ratingDate = ratingDate;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getRating()
        {
        return rating;
        }

    public LocalDateTime getRatingDate()
        {
        return ratingDate;
        }

    @Override
    public String toString()
        {
        return "Rating{" +
                "teamId='" + teamId + '\'' +
                ", rating=" + rating +
                ", ratingDate=" + ratingDate +
                '}';
        }
    }
