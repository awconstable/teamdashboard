package team.dashboard.web.dora.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.dora.domain.Incident;
import team.dashboard.web.dora.services.IncidentService;

import java.util.List;

@Controller
@RequestMapping(value = "/incs", produces = "application/json")
public class IncidentController
    {
    private static final Logger log = LoggerFactory.getLogger(IncidentController.class);

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService)
        {
        this.incidentService = incidentService;
        }

    @GetMapping("/{applicationId}")
    @ResponseBody
    public List<Incident> listForApplication(@PathVariable String applicationId){
        log.info("List all incidents for application with id {}", applicationId);
        return incidentService.list(applicationId);
    }
    }
