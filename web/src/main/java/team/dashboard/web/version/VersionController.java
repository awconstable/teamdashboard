package team.dashboard.web.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public String getVersion()
        {
        return versionHolder.getVersion();
        }
    }
