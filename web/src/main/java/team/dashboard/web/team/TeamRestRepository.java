package team.dashboard.web.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public List<Team> findAllTeams()
        {
        ResponseEntity<List<Team>> response = restTemplate.exchange(teamServiceUrl + "/teams/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Team>>()
                    {
                    });

        return response.getBody();
        }


    public Team findByTeamSlug(String slug)
        {
        return restTemplate.getForObject(teamServiceUrl + "/teams/relatives/" + slug, Team.class);
        }

    public List<TeamRelation> findCompleteHierarchy()
        {

        ResponseEntity<List<TeamRelation>> response = restTemplate.exchange(teamServiceUrl + "/teams/hierarchy/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamRelation>>()
                    {
                    });

        return response.getBody();
        }
    }
