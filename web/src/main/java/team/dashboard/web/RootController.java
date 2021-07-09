package team.dashboard.web;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RootController
    {

    @GetMapping({"/", "/teamexplorer/**", "/dashboard/**", "/capture/**", "/collection/**", "/deployments/**", "/incidents/**", "/changerequests/**"})
    public String index(Model model, HttpServletResponse response)
        {
        response.setHeader(HttpHeaders.CACHE_CONTROL,"no-cache, must-revalidate, no-store");
        response.setHeader("Expires", "0");
        
        return "dashboard";
        }

    }
