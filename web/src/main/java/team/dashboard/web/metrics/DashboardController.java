package team.dashboard.web.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRestRepository;

@Controller
public class DashboardController
    {

    @Autowired
    private TeamRestRepository teamRepository;

    @GetMapping("/dashboard/{slug}/")
    public String graph(Model model, @PathVariable String slug)
        {

        Team team = teamRepository.findByTeamSlug(slug);
        if (team != null)
            {
            model.addAttribute("teamName", team.getName());
            model.addAttribute("platformName", "");
            model.addAttribute("domainName", "");
            }

        model.addAttribute("team", slug);
        return "dashboard";
        }
    }
