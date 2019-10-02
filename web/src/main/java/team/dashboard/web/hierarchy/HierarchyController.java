package team.dashboard.web.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/hierarchy")
public class HierarchyController
    {

    private final HierarchyRestRepository hierarchyRestRepository;

    @Autowired
    public HierarchyController(HierarchyRestRepository hierarchyRestRepository)
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
