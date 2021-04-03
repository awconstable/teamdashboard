package team.dashboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController
    {

    @GetMapping({"/", "/teamexplorer/**", "/dashboard/**", "/capture/**", "/collection/**", "/deployments/**", "/incidents/**", "/changerequests/**"})
    public String index(Model model)
        {
        return "dashboard";
        }

    }
