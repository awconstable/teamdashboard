package team.dashboard.web.dora.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.DeploymentService;
import team.dashboard.web.dora.services.LeadTimeService;
import team.dashboard.web.dora.services.MttrService;

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
        private final DeploymentService deploymentService;
        private final LeadTimeService leadTimeService;
        private final MttrService mttrService;

        @Autowired
        public ScheduledDoraMetricLoad(DeploymentFrequencyService deploymentFrequencyService, DeploymentService deploymentService, LeadTimeService leadTimeService, MttrService mttrService)
        {
            this.deploymentFrequencyService = deploymentFrequencyService;
            this.deploymentService = deploymentService;
            this.leadTimeService = leadTimeService;
            this.mttrService = mttrService;
        }
        
        @Scheduled(cron = "${cron.schedule}")
        public void loadDoraMetricsDaily() {
        try
            {
                ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
                log.info("The reporting date is {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
                deploymentFrequencyService.loadAll(Date.from(reportingDate.toInstant()));
                deploymentService.loadAll(Date.from(reportingDate.toInstant()));
                leadTimeService.loadAll(Date.from(reportingDate.toInstant()));
                mttrService.loadAll(Date.from(reportingDate.toInstant()));
            } catch (Exception e){
                log.error("Error loading dora metrics", e);
            }
        }
    }
