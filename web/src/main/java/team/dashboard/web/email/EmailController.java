package team.dashboard.web.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class EmailController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping("/email")
    public void requestFeedback() throws Exception {
        feedbackService.processFeedbackRequests();
    }

    @RequestMapping("/email/{email}")
    public void requestFeedback(@PathVariable String email, HttpServletResponse response) throws Exception {
        feedbackService.processFeedbackRequest(email);
    }

}
