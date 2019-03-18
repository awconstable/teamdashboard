package team.dashboard.web.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.util.List;

@Controller
public class DashboardController
    {

    private final TeamRestRepository teamRepository;

    @Autowired
    public DashboardController(TeamRestRepository teamRepository)
        {
        this.teamRepository = teamRepository;
        }

    @GetMapping("/dashboard/{slug}/")
    public String graph(Model model, @PathVariable String slug)
        {

        Team team = teamRepository.findByTeamSlug(slug);
        if (team != null)
            {
            model.addAttribute("team", team);

            model.addAttribute("ancestors", team.getAncestors());

            }
        model.addAttribute("slug", slug);
        return "dashboard";
        }


    @GetMapping("/")
    public String index(Model model)
        {

        List<TeamRelation> teams = teamRepository.findHierarchy();
        System.out.println(teams);
        if (teams != null)
            {
            model.addAttribute("teams", teams);

            }
        return "teamexplorer";
        }
    }

