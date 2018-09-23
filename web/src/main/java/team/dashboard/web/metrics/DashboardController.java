package team.dashboard.web.metrics;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController
    {

    @GetMapping("/dashboard/{teamId}/")
    public String graph(Model model, @PathVariable String teamId)
        {
        model.addAttribute("team", teamId);
        return "trendgraph";
        }
    }
