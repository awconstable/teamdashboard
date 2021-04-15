package team.dashboard.web.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class VersionHolder
    {

    private static final Logger log = LoggerFactory.getLogger(VersionHolder.class);
    
    private final String version;
    private final String buildDate;

    public VersionHolder()
        {
        Properties props = new Properties();
        Resource resource = new ClassPathResource("version.txt", SpringBootApplication.class.getClassLoader());
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error("unable to load version.txt from the classpath", e);
        }
        version = props.getProperty("version", "unknown");
        buildDate = props.getProperty("build.date", "unknown");
        }

    public String getVersion() { return version; }

    public String getBuildDate() { return buildDate; }
    }