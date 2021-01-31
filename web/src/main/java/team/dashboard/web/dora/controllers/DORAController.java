package team.dashboard.web.dora.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.LeadTime;
import team.dashboard.web.dora.domain.TeamPerformance;
import team.dashboard.web.dora.services.DeploymentFrequencyService;
import team.dashboard.web.dora.services.LeadTimeService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Controller
@RequestMapping(value = "/dora", produces = "application/json")
public class DORAController
    {
    private final DeploymentFrequencyService deploymentFrequencyService;
    private final LeadTimeService leadTimeService;

    @Autowired
    public DORAController(DeploymentFrequencyService deploymentFrequencyService, LeadTimeService leadTimeService)
        {
        this.deploymentFrequencyService = deploymentFrequencyService;
        this.leadTimeService = leadTimeService;
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

    @GetMapping("/load/deployment")
    @ResponseBody
    public String loadDeployFreq(){
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        deploymentFrequencyService.loadAll(Date.from(reportingDate.toInstant()));
        return "OK";
    }
    @GetMapping("/load/deployment/{reportingDate}")
    @ResponseBody
    public String loadDeployFreq(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate){
        deploymentFrequencyService.loadAll(Date.from(reportingDate.atStartOfDay(ZoneId.of("UTC")).toInstant()));
        return "OK";
    }
    
    @GetMapping("/team/{applicationId}/performance")
    @ResponseBody
    public TeamPerformance getTeamPerformance(@PathVariable String applicationId) {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        Optional<DeploymentFrequency> freq = deploymentFrequencyService.get(applicationId, Date.from(reportingDate.toInstant()));
        Optional<LeadTime> leadTime = leadTimeService.get(applicationId, Date.from(reportingDate.toInstant()));
        return new TeamPerformance(Date.from(reportingDate.toInstant()), freq.orElse(null), leadTime.orElse(null));
    }
    }
