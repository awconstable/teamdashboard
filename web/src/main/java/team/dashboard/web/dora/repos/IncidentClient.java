package team.dashboard.web.dora.repos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.domain.MTTR;

import java.util.Date;
import java.util.List;

@FeignClient(value = "incident-service")
public interface IncidentClient
    {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/incident/application/{appId}/mttr")
    MTTR getMttr(@PathVariable("appId") String applicationId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/incident/application/{appId}/mttr/{reportingDate}")
    MTTR getMttr(@PathVariable("appId") String applicationId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/incident/application/{appId}")
    List<Incident> listForApplication(@PathVariable("appId") String applicationId);
    }
