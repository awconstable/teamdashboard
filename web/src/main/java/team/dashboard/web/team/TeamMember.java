package team.dashboard.web.team;

public class TeamMember
    {
    private final String email;
    private final String name;

    public TeamMember(String email, String name)
        {
        this.email = email;
        this.name = name;
        }

    public String getEmail()
        {
        return email;
        }

    public String getName()
        {
        return name;
        }

    @Override
    public String toString()
        {
        return "TeamMember{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
        }
    }
