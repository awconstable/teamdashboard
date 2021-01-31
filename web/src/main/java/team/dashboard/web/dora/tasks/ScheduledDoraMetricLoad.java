package team.dashboard.web.dora.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.LeadTimeService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class ScheduledDoraMetricLoad
    {
        private static final Logger log = LoggerFactory.getLogger(ScheduledDoraMetricLoad.class);

        private final DeploymentFrequencyService deploymentFrequencyService;
        private final LeadTimeService leadTimeService;

        @Autowired
        public ScheduledDoraMetricLoad(DeploymentFrequencyService deploymentFrequencyService, LeadTimeService leadTimeService)
        {
            this.deploymentFrequencyService = deploymentFrequencyService;
            this.leadTimeService = leadTimeService;
        }
        
        @Scheduled(cron = "0 0 3 * * *")
        public void loadDoraMetricsDaily() {
        try
            {
                ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
                log.info("The reporting date is {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
                deploymentFrequencyService.loadAll(Date.from(reportingDate.toInstant()));
                leadTimeService.loadAll(Date.from(reportingDate.toInstant()));
            } catch (Exception e){
                log.error("Error loading dora metrics", e);
            }
        }
    }
