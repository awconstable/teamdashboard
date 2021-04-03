package team.dashboard.web.dora.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.dora.domain.ChangeRequest;
import team.dashboard.web.dora.services.ChangeRequestService;

import java.util.List;

@Controller
@RequestMapping(value = "/crs", produces = "application/json")
public class ChangeRequestController
    {
    private static final Logger log = LoggerFactory.getLogger(ChangeRequestController.class);

    private final ChangeRequestService changeRequestService;

    @Autowired
    public ChangeRequestController(ChangeRequestService changeRequestService)
        {
        this.changeRequestService = changeRequestService;
        }

    @GetMapping("/{applicationId}")
    @ResponseBody
    public List<ChangeRequest> listForApplication(@PathVariable String applicationId){
        log.info("List all change requests for application with id {}", applicationId);
        return changeRequestService.list(applicationId);
    }
    }
