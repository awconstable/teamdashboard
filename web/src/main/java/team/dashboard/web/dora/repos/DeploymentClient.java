package team.dashboard.web.dora.repos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.dashboard.web.dora.domain.Deployment;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.LeadTime;

import java.util.Date;
import java.util.List;

@FeignClient(value = "deployment-service")
public interface DeploymentClient
    {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}/frequency")
    DeploymentFrequency getDeployFrequency(@PathVariable("appId") String applicationId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}/frequency/{reportingDate}")
    DeploymentFrequency getDeployFrequency(@PathVariable("appId") String applicationId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}/lead_time")
    LeadTime getLeadTime(@PathVariable("appId") String applicationId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}/lead_time/{reportingDate}")
    LeadTime getLeadTime(@PathVariable("appId") String applicationId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}")
    List<Deployment> getDeploymentsForApplication(@PathVariable("appId") String applicationId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/deployment/application/{appId}/date/{reportingDate}")
    List<Deployment> getDeploymentsForApplicationWithDate(@PathVariable("appId") String applicationId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);
    }
