package team.dashboard.web.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class TeamRestRepository
    {

    private RestTemplate restTemplate;

    @Autowired
    public TeamRestRepository(RestTemplateBuilder builder)
        {
        this.restTemplate = builder.build();
        }

    @Value("${team.service.url}")
    private String teamServiceUrl;

    public Team findByTeamSlug(String slug)
        {
        return restTemplate.getForObject(teamServiceUrl + "/team/search/findBySlug?slug=" + slug, Team.class);
        }
    }
