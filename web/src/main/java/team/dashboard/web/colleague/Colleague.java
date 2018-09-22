package team.dashboard.web.colleague;

import org.springframework.data.annotation.Id;

/**
 * Created by awconstable on 20/02/2017.
 */
public class Colleague
    {

    @Id
    private String id;
    private String teamId;
    private String email;

    public Colleague()
        {
        }

    public Colleague(String teamId, String email)
        {
        this.teamId = teamId;
        this.email = email;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public String getEmail()
        {
        return email;
        }
    }
