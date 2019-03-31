package team.dashboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController
    {

    @GetMapping("/*")
    public String index(Model model)
        {
        return "dashboard";
        }

    @GetMapping(value = "/capture/*")
    public String capture(Model model)
        {
        return "dashboard";
        }

    }
