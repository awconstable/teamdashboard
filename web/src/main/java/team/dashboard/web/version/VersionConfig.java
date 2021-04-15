package team.dashboard.web.version;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionConfig
    {

    @Bean
    VersionHolder getVersionHolder()
        {
        return new VersionHolder();
        }
    }
