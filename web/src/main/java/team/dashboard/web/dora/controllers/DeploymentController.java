package team.dashboard.web.dora.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.dora.domain.Deployment;
import team.dashboard.web.dora.services.DeploymentService;

import java.util.List;

@Controller
@RequestMapping(value = "/deploys", produces = "application/json")
public class DeploymentController
    {
    private static final Logger log = LoggerFactory.getLogger(DeploymentController.class);

    private final DeploymentService deploymentService;

    @Autowired
    public DeploymentController(DeploymentService deploymentService)
        {
        this.deploymentService = deploymentService;
        }

    @GetMapping("/{applicationId}")
    @ResponseBody
    public List<Deployment> listDeploymentsForApplication(@PathVariable String applicationId){
        log.info("List all deployments for application with id {}", applicationId);
        return deploymentService.listDeployments(applicationId);
    }
    }
