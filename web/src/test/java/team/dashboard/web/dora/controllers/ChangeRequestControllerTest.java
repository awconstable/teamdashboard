package team.dashboard.web.dora.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import team.dashboard.web.dora.domain.ChangeRequest;
import team.dashboard.web.dora.services.ChangeRequestService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChangeRequestController.class)
class ChangeRequestControllerTest
    {

    @Autowired private MockMvc mockMvc;
    @MockBean private ChangeRequestService changeRequestService;

    private List<ChangeRequest> getChangeRequests(Date reportingDate)
        {
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "app1", reportingDate, reportingDate, reportingDate, reportingDate,false, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "app2", reportingDate, reportingDate, reportingDate, reportingDate,false,"test");
        ChangeRequest cr3 = new ChangeRequest("cr3", "change request 3", "app3", reportingDate, reportingDate, reportingDate, reportingDate,false,"test");
        return Arrays.asList(cr1, cr2, cr3);
        }
    
    @Test
    void listChangeRequestsForApplication() throws Exception
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        when(changeRequestService.list(appId)).thenReturn(getChangeRequests(Date.from(reportingDate.toInstant())));

        MvcResult result = mockMvc.perform(get("/crs/app1/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        verify(changeRequestService, times(1)).list("app1");
        assertThat(content, is(equalTo("[{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"app1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":false,\"source\":\"test\"},{\"changeRequestId\":\"cr2\",\"description\":\"change request 2\",\"applicationId\":\"app2\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":false,\"source\":\"test\"},{\"changeRequestId\":\"cr3\",\"description\":\"change request 3\",\"applicationId\":\"app3\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":false,\"source\":\"test\"}]")));
        }
    }