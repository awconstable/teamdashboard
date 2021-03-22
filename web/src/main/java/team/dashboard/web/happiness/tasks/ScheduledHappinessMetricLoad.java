package team.dashboard.web.happiness.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.dashboard.web.happiness.services.HappinessService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class ScheduledHappinessMetricLoad
    {
        private static final Logger log = LoggerFactory.getLogger(ScheduledHappinessMetricLoad.class);
        
        private final HappinessService happinessService;

        @Autowired  
        public ScheduledHappinessMetricLoad(HappinessService happinessService)
        {
        this.happinessService = happinessService;
        }

        @Scheduled(cron = "${cron.schedule}")
        public void loadHappinessMetricsDaily() {
        try
            {
                ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
                log.info("The reporting date is {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
                happinessService.loadAll(Date.from(reportingDate.toInstant()));
            } catch (Exception e){
                log.error("Error loading happiness metrics", e);
            }
        }
    }
