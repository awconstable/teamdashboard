package team.dashboard.web.trend;

import team.dashboard.web.rating.Rating;
import team.dashboard.web.rating.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RatingTrendService {

    @Autowired
    private RatingRepository repository;

    public ArrayList<RatingTrend> getTrendData(String team) {

        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        LocalDateTime endDate = LocalDateTime.now();

        List<Rating> ratings = new ArrayList<Rating>();

        if(team == null || "all".equals(team)){
            ratings = repository.findByRatingDateBetween(startDate, endDate);
        } else {
            ratings = repository.findByTeamIdAndRatingDateBetween(team, startDate, endDate);
        }



        HashMap<Month, ArrayList<Rating>> calcstore = new HashMap<>();

        for (Rating rating:ratings) {
            LocalDateTime ratingDate = rating.getRatingDate();
            if(calcstore.containsKey(ratingDate.getMonth())){
                calcstore.get(ratingDate.getMonth()).add(rating);
            } else {
                ArrayList<Rating> list = new ArrayList<Rating>();
                list.add(rating);
                calcstore.put(ratingDate.getMonth(), list);
            }
        }

        ArrayList<RatingTrend> trends = new ArrayList<>();


        Integer monthCount = 0;

        while(monthCount < 12) {

            LocalDate date = LocalDate.now().minusMonths(monthCount);

            Month month = date.getMonth();

            if (calcstore.containsKey(month)) {
                ArrayList<Rating> ratings1 = calcstore.get(month);
                Integer count = 0;
                Integer total = 0;
                String teamId = "";
                for (Rating r : ratings1) {
                    count++;
                    total = total + r.getRating();
                    teamId = r.getTeamId();
                }
                Double avg = (double) total / (double) count;

                trends.add(new RatingTrend(teamId, avg, count, date));
            } else {
                trends.add(new RatingTrend(team, 0.0, 0, date));
            }
            monthCount++;
        }
        return trends;
    }
}
