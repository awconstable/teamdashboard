package team.dashboard.web.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController
    {

    private final TeamRestRepository teamRepository;

    @Autowired
    public TeamController(TeamRestRepository teamRepository)
        {
        this.teamRepository = teamRepository;
        }

    @GetMapping("")
    @ResponseBody
    public List<TeamRelation> findTeamHierarchy(Model model)
        {
        return teamRepository.findHierarchy();
        }

    @GetMapping("/{slug}")
    @ResponseBody
    public Team findTeam(Model model, @PathVariable String slug)
        {
        return teamRepository.findByTeamSlug(slug);
        }
    }
