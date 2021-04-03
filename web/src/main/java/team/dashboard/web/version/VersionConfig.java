package team.dashboard.web.version;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionConfig
    {

    @Bean
    VersionHolder getVersionHolder(ApplicationContext context)
        {
        return new VersionHolder(context);
        }
    }
