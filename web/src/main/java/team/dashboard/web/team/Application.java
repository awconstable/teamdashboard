package team.dashboard.web.team;

public class Application
    {
    private final String applicationId;
    private final String applicationName;

    public Application(String applicationId, String applicationName)
        {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        }

    public String getApplicationId()
        {
        return applicationId;
        }

    public String getApplicationName()
        {
        return applicationName;
        }

    @Override
    public String toString()
        {
        return "Application{" +
                "applicationId='" + applicationId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                '}';
        }
    }
