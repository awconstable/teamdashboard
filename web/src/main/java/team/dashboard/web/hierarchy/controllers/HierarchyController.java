package team.dashboard.web.hierarchy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;
import team.dashboard.web.hierarchy.domain.Relation;
import team.dashboard.web.hierarchy.repos.HierarchyClient;

import java.util.List;

@Controller
@RequestMapping("/hierarchy")
public class HierarchyController
    {

    private final HierarchyClient hierarchyRestRepository;

    @Autowired
    public HierarchyController(HierarchyClient hierarchyRestRepository)
        {
        this.hierarchyRestRepository = hierarchyRestRepository;
        }

    @GetMapping("")
    @ResponseBody
    public List<Relation> findHierarchy()
        {
        return hierarchyRestRepository.findCompleteHierarchy();
        }

    @GetMapping("/{slug}")
    @ResponseBody
    public HierarchyEntity findEntity(@PathVariable String slug)
        {
        return hierarchyRestRepository.findEntityBySlug(slug);
        }
    }
