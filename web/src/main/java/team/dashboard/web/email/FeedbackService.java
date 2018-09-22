package team.dashboard.web.email;

import team.dashboard.web.colleague.Colleague;
import team.dashboard.web.colleague.ColleagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by awconstable on 20/02/2017.
 */
@Service
public class FeedbackService {
    @Value("${view.url}")
    private String viewUrl;
    @Value("${rating.url}")
    private String ratingUrl;
    @Value("${frequency}")
    private String frequency;
    @Value("${department.name}")
    private String departmentName;


    @Autowired
    private ColleagueRepository colleagueRepository;
    @Autowired
    private EmailFeedbackRequestSender emailSender;

    public void processFeedbackRequests() {
        List<Colleague> colleagues = colleagueRepository.findAll();

        processRequestList(colleagues);
    }

    public void processFeedbackRequest(String email) {
        List<Colleague> colleagues = colleagueRepository.findByEmailIgnoreCase(email);
        System.out.println("email search:" + email);
        processRequestList(colleagues);
    }

    private void processRequestList(List<Colleague> colleagues) {
        for (Colleague colleague : colleagues) {

            try {
                FeedbackRequest request = new FeedbackRequest(colleague.getTeamId(), colleague.getEmail(), viewUrl, ratingUrl, frequency, departmentName);
                System.out.println(request);
                emailSender.requestFeedback(request);
            } catch (Exception e) {
                System.out.println("email failed to send for: " + colleague.getEmail());
                System.out.println(e);
            }
        }
    }

}
