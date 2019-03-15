package team.dashboard.web.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class TeamRestRepository
    {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${team.service.url}")
    private String teamServiceUrl;

    public Team findByTeamSlug(String slug)
        {
        return restTemplate.getForObject(teamServiceUrl + "/team/search/findBySlug?slug=" + slug, Team.class);
        }
    }
