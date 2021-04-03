package team.dashboard.web.version;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class VersionHolder
    {

    private final String version;

    public VersionHolder(ApplicationContext context)
        {
        version = context.getBeansWithAnnotation(SpringBootApplication.class).entrySet().stream()
                .findFirst()
                .flatMap(es -> {
                final String implementationVersion = es.getValue().getClass().getPackage().getImplementationVersion();
                return Optional.ofNullable(implementationVersion);
                }).orElse("unknown");
        }

    public String getVersion()
        {
        return version;
        }
    }