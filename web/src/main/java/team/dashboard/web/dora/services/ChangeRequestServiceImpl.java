package team.dashboard.web.dora.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.dashboard.web.dora.domain.ChangeRequest;
import team.dashboard.web.dora.repos.ChangeRequestClient;

import java.util.List;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService
    {
    private static final Logger log = LoggerFactory.getLogger(ChangeRequestServiceImpl.class);
    
    private final ChangeRequestClient changeRequestClient;

    @Autowired
    public ChangeRequestServiceImpl(ChangeRequestClient changeRequestClient)
        {
        this.changeRequestClient = changeRequestClient;
        }

    @Override
    public List<ChangeRequest> list(String applicationId)
        {
        log.info("List all change requests for hierarchy starting at application with id {}", applicationId);
        return changeRequestClient.listForHierarchy(applicationId);
        }
    }
