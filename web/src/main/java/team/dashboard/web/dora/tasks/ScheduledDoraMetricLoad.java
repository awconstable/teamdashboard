package team.dashboard.web.dora.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.dashboard.web.dora.services.*;

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
        private final ChangeFailureRateService changeFailureRateService;

        @Autowired
        public ScheduledDoraMetricLoad(DeploymentFrequencyService deploymentFrequencyService, DeploymentService deploymentService, LeadTimeService leadTimeService, MttrService mttrService, ChangeFailureRateService changeFailureRateService)
        {
            this.deploymentFrequencyService = deploymentFrequencyService;
            this.deploymentService = deploymentService;
            this.leadTimeService = leadTimeService;
            this.mttrService = mttrService;
            this.changeFailureRateService = changeFailureRateService;
        }
        
        @Scheduled(cron = "${cron.schedule}")
        public void loadDoraMetricsDaily() {
        try
            {
                ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
                log.info("The reporting date is {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
                Date date = Date.from(reportingDate.toInstant());
                deploymentFrequencyService.loadAll(date);
                deploymentService.loadAll(date);
                leadTimeService.loadAll(date);
                mttrService.loadAll(date);
                changeFailureRateService.loadAll(date);
            } catch (Exception e){
                log.error("Error loading dora metrics", e);
            }
        }
    }
