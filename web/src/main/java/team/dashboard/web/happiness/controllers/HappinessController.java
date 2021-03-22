package team.dashboard.web.happiness.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.happiness.services.HappinessService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
@RequestMapping(value = "/happiness", produces = "application/json")
public class HappinessController
    {
    private static final Logger log = LoggerFactory.getLogger(HappinessController.class);
    
    private final HappinessService happinessService;

    @Autowired
    public HappinessController(HappinessService happinessService)
        {
        this.happinessService = happinessService;
        }

    @GetMapping("/load/happiness")
    @ResponseBody
    public String loadHappinessTrend(){
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        log.info("Load deployments with date {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
        happinessService.loadAll(Date.from(reportingDate.toInstant()));
        return "OK";
    }
    @GetMapping("/load/happiness/{reportingDate}")
    @ResponseBody
    public String loadHappinessTrend(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate){
        log.info("Load deployments with date {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
        happinessService.loadAll(Date.from(reportingDate.atStartOfDay(ZoneId.of("UTC")).toInstant()));
        return "OK";
    }
    
    }
