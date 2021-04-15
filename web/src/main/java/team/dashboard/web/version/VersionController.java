package team.dashboard.web.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/version", produces = "application/json")
public class VersionController
    {
    private final VersionHolder versionHolder;

    @Autowired
    public VersionController(VersionHolder versionHolder)
        {
        this.versionHolder = versionHolder;
        }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, String> getVersion()
        {
        HashMap<String, String> map = new HashMap<>();
        map.put("version", versionHolder.getVersion());
        map.put("buildDate", versionHolder.getBuildDate());
        return map;
        }
    }
