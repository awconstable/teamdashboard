package team.dashboard.web.dora.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.LeadTime;
import team.dashboard.web.dora.domain.MTTR;
import team.dashboard.web.dora.domain.TeamPerformance;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.DeploymentService;
import team.dashboard.web.dora.services.LeadTimeService;
import team.dashboard.web.dora.services.MttrService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(value = "/dora", produces = "application/json")
public class DORAController
    {
    private static final Logger log = LoggerFactory.getLogger(DORAController.class);
    
    private final DeploymentService deploymentService;
    private final DeploymentFrequencyService deploymentFrequencyService;
    private final LeadTimeService leadTimeService;
    private final MttrService mttrService;

    @Autowired
    public DORAController(DeploymentService deploymentService, DeploymentFrequencyService deploymentFrequencyService, LeadTimeService leadTimeService, MttrService mttrService)
        {
        this.deploymentService = deploymentService;
        this.deploymentFrequencyService = deploymentFrequencyService;
        this.leadTimeService = leadTimeService;
        this.mttrService = mttrService;
        }
    
    @GetMapping("/load/lead_time")
    @ResponseBody
    public String loadLeadTime(){
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        leadTimeService.loadAll(Date.from(reportingDate.toInstant()));
        return "OK";
    }
    @GetMapping("/load/lead_time/{reportingDate}")
    @ResponseBody
    public String loadLeadTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate){
        leadTimeService.loadAll(Date.from(reportingDate.atStartOfDay(ZoneId.of("UTC")).toInstant()));
        return "OK";
    }

    @GetMapping("/load/mttr")
    @ResponseBody
    public String loadMttr(){
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        mttrService.loadAll(Date.from(reportingDate.toInstant()));
        return "OK";
    }
    
    @GetMapping("/load/mttr/{reportingDate}")
    @ResponseBody
    public String loadMttr(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate){
        mttrService.loadAll(Date.from(reportingDate.atStartOfDay(ZoneId.of("UTC")).toInstant()));
        return "OK";
    }

    @GetMapping("/load/deployment")
    @ResponseBody
    public String loadDeployFreq(){
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        log.info("Load deployments with date {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
        deploymentService.loadAll(Date.from(reportingDate.toInstant()));
        deploymentFrequencyService.loadAll(Date.from(reportingDate.toInstant()));
        return "OK";
    }
    @GetMapping("/load/deployment/{reportingDate}")
    @ResponseBody
    public String loadDeployFreq(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate){
        log.info("Load deployments with date {}", DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate));
        Date date = Date.from(reportingDate.atStartOfDay(ZoneId.of("UTC")).toInstant());
        deploymentService.loadAll(date);
        deploymentFrequencyService.loadAll(date);
        return "OK";
    }
    
    @GetMapping("/team/{applicationId}/performance")
    @ResponseBody
    public TeamPerformance getTeamPerformance(@PathVariable String applicationId) {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Optional<DeploymentFrequency> freq = deploymentFrequencyService.get(applicationId, Date.from(reportingDate.toInstant()));
        Optional<LeadTime> leadTime = leadTimeService.get(applicationId, Date.from(reportingDate.toInstant()));
        Optional<MTTR> mttr = mttrService.get(applicationId, Date.from(reportingDate.toInstant()));
        return new TeamPerformance(Date.from(reportingDate.toInstant()), freq.orElse(null), leadTime.orElse(null), mttr.orElse(null));
    }
    }
