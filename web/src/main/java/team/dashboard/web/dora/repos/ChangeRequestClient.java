package team.dashboard.web.dora.repos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.dashboard.web.dora.domain.ChangeFailureRate;
import team.dashboard.web.dora.domain.ChangeRequest;

import java.util.Date;
import java.util.List;

@FeignClient(value = "change-service")
public interface ChangeRequestClient
    {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/changerequest/application/{appId}/cfr")
    ChangeFailureRate getChangeFailureRate(@PathVariable("appId") String applicationId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/changerequest/application/{appId}/cfr/{reportingDate}")
    ChangeFailureRate getChangeFailureRate(@PathVariable("appId") String applicationId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);
    
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/changerequest/hierarchy/{appId}")
    List<ChangeRequest> listForHierarchy(@PathVariable("appId") String applicationId);
    }
