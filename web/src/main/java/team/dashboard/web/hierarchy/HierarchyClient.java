package team.dashboard.web.hierarchy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "team-service")
public interface HierarchyClient
    {
        @RequestMapping(method = RequestMethod.GET, value = "/v2/hierarchy/relatives/{slug}")
        HierarchyEntity findEntityBySlug(@PathVariable("slug") String slug);
    
        @RequestMapping(method = RequestMethod.GET, value = "/v2/hierarchy/all")
        List<HierarchyEntity> findAll();

        @RequestMapping(method = RequestMethod.GET, value = "/v2/hierarchy/complete")
        List<Relation> findCompleteHierarchy();

        @RequestMapping(method = RequestMethod.GET, value = "/v2/hierarchy/complete/{slug}")
        Relation findHierarchyBySlug(@PathVariable("slug") String slug);
    }
