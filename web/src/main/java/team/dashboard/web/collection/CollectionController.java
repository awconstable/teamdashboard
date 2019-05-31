package team.dashboard.web.collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/collection")
public class CollectionController
    {
    @GetMapping("/*")
    public String index()
        {
        return "dashboard";
        }
    }
