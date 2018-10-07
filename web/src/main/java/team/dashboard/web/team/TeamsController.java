package team.dashboard.web.team;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamsController
    {

    @GetMapping("/teams")
    public String graph(Model model)
        {
        return "teams";
        }
    }
