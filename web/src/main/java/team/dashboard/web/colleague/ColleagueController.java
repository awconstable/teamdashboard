package team.dashboard.web.colleague;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by awconstable on 20/02/2017.
 */
@RestController
public class ColleagueController
    {
    @Autowired
    private ColleagueRepository repository;

    }
