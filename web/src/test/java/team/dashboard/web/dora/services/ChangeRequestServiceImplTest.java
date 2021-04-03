package team.dashboard.web.dora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.dashboard.web.dora.domain.ChangeRequest;
import team.dashboard.web.dora.repos.ChangeRequestClient;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ChangeRequestServiceImplTest
    {
    @TestConfiguration
    static class IncidentServiceTestContextConfiguration
        {
        @MockBean
        private ChangeRequestClient mockChangeRequestService;

        @Bean
        public ChangeRequestService changeRequestService()
            {
            return new ChangeRequestServiceImpl(
                mockChangeRequestService
            );
            }
        }
    @Autowired private ChangeRequestService changeRequestService;
    @Autowired private ChangeRequestClient mockChangeRequestClient;

    private List<ChangeRequest> getChangeRequests(Date reportingDate)
        {
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "app1", reportingDate, reportingDate, reportingDate, reportingDate,false, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "app2", reportingDate, reportingDate, reportingDate, reportingDate,false,"test");
        ChangeRequest cr3 = new ChangeRequest("cr3", "change request 3", "app3", reportingDate, reportingDate, reportingDate, reportingDate,false,"test");
        return Arrays.asList(cr1, cr2, cr3);
        }
    
    @Test
    void list()
        {
        String appId = "app1";
        when(mockChangeRequestClient.listForHierarchy(appId)).thenReturn(getChangeRequests(new Date()));

        List<ChangeRequest> crs = changeRequestService.list(appId);

        verify(mockChangeRequestClient, times(1)).listForHierarchy(appId);
        assertThat(crs.size(), is(equalTo(3)));
        }
    }