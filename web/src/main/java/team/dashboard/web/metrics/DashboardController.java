package team.dashboard.web.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRepository;

import java.util.Optional;

@Controller
public class DashboardController
    {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/dashboard/{teamId}/")
    public String graph(Model model, @PathVariable String teamId)
        {

        Optional<Team> team = teamRepository.findByTeamId(teamId);
        if (team.isPresent())
            {
            model.addAttribute("teamName", team.get().getTeamName());
            model.addAttribute("platformName", team.get().getPlatformName());
            model.addAttribute("domainName", team.get().getDomainName());
            }

        model.addAttribute("team", teamId);
        return "dashboard";
        }
    }
