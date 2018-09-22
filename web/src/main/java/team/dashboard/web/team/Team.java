package team.dashboard.web.team;

import org.springframework.data.annotation.Id;

public class Team {
    @Id
    private String id;
    private final String teamId;
    private final String teamName;
    private final String platformName;
    private final String domainName;

    public Team(String teamId, String teamName, String platformName, String domainName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.platformName = platformName;
        this.domainName = domainName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getDomainName() {
        return domainName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", platformName='" + platformName + '\'' +
                ", domainName='" + domainName + '\'' +
                '}';
    }
}
