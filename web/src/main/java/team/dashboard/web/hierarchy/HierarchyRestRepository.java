package team.dashboard.web.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Repository
public class HierarchyRestRepository
    {

    private RestTemplate restTemplate;
    @Value("${team.service.url}")
    private String teamServiceUrl;

    @Autowired
    public HierarchyRestRepository(RestTemplateBuilder builder)
        {
        this.restTemplate = builder.build();
        }

    public HierarchyEntity findEntityBySlug(String slug)
        {
        return restTemplate.getForObject(teamServiceUrl + "/v2/hierarchy/relatives/" + slug, HierarchyEntity.class);
        }

    public List<HierarchyEntity> findAll()
        {
        ResponseEntity<List<HierarchyEntity>> response = restTemplate.exchange(teamServiceUrl + "/v2/hierarchy/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<HierarchyEntity>>()
                    {
                    });

        return response.getBody();
        }

    public List<Relation> findCompleteHierarchy()
        {

        ResponseEntity<List<Relation>> response = restTemplate.exchange(teamServiceUrl + "/v2/hierarchy/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Relation>>()
                    {
                    });

        return response.getBody();
        }

    public Relation findHierarchyBySlug(String slug)
        {
        ResponseEntity<List<Relation>> response = restTemplate.exchange(teamServiceUrl + "/v2/hierarchy/" + slug,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Relation>>()
                    {
                    });

        return Objects.requireNonNull(response.getBody()).get(0);
        }

    }
