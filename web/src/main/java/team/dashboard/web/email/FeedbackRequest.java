package team.dashboard.web.email;

/**
 * Created by awconstable on 20/02/2017.
 */
public class FeedbackRequest {
    private final String teamId;
    private final String toEmail;
    private final String viewUrl;
    private final String ratingUrl;
    private final String frequency;
    private final String departmentName;

    public FeedbackRequest(String teamId, String toEmail, String viewUrl, String ratingUrl, String frequency, String departmentName) {
        this.teamId = teamId;
        this.toEmail = toEmail;
        this.viewUrl = viewUrl;
        this.ratingUrl = ratingUrl;
        this.frequency = frequency;
        this.departmentName = departmentName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public String getRatingUrl() {
        return ratingUrl;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "teamId='" + teamId + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", ratingUrl='" + ratingUrl + '\'' +
                ", frequency='" + frequency + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
