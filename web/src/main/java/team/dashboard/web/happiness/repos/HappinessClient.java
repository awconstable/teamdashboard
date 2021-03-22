package team.dashboard.web.happiness.repos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.dashboard.web.happiness.domain.HappinessTrend;

import java.util.Date;

@FeignClient(value = "team-happiness")
public interface HappinessClient
    {
    @RequestMapping(method = RequestMethod.GET, value = "/trend/90days/{teamId}")
    HappinessTrend get90DayTrend(@PathVariable("teamId") String teamId);

    @RequestMapping(method = RequestMethod.GET, value = "/trend/90days/{teamId}/{reportingDate}")
    HappinessTrend get90DayTrend(@PathVariable("teamId") String teamId, @PathVariable("reportingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date reportingDate);
    }
