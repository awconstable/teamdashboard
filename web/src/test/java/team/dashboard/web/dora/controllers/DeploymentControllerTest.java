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
import team.dashboard.web.dora.domain.Change;
import team.dashboard.web.dora.domain.Deployment;
import team.dashboard.web.dora.services.DeploymentService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DeploymentController.class)
class DeploymentControllerTest
    {
    
    @Autowired private MockMvc mockMvc;
    @MockBean private DeploymentService deploymentService;

    private List<Deployment> getDeployments(Date reportingDate)
        {
        Deployment d1 = new Deployment("id1", "d1","", "app1", "rfc1", reportingDate, "", new HashSet<Change>());
        Deployment d2 = new Deployment("id1","d2","","app2", "rfc2", reportingDate, "", new HashSet<Change>());
        Deployment d3 = new Deployment("id1","d3","","app3", "rfc3", reportingDate, "", new HashSet<Change>());

        List<Deployment> deploys = new ArrayList<>();
        deploys.add(d1);
        deploys.add(d2);
        deploys.add(d3);
        return deploys;
        }
    
    @Test
    void listDeploymentsForApplication() throws Exception
        {
        String appId = "app1";
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        when(deploymentService.listDeployments(appId)).thenReturn(getDeployments(Date.from(reportingDate.toInstant())));

        MvcResult result = mockMvc.perform(get("/deploys/app1/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        String dateOut = DateTimeFormatter.ISO_DATE_TIME.format(reportingDate);
        verify(deploymentService, times(1)).listDeployments("app1");
        assertThat(content, is(equalTo("[{\"id\":\"id1\",\"deploymentId\":\"d1\",\"deploymentDesc\":\"\",\"applicationId\":\"app1\",\"rfcId\":\"rfc1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"\",\"changes\":[],\"leadTimeSeconds\":0,\"leadTimePerfLevel\":null},{\"id\":\"id1\",\"deploymentId\":\"d2\",\"deploymentDesc\":\"\",\"applicationId\":\"app2\",\"rfcId\":\"rfc2\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"\",\"changes\":[],\"leadTimeSeconds\":0,\"leadTimePerfLevel\":null},{\"id\":\"id1\",\"deploymentId\":\"d3\",\"deploymentDesc\":\"\",\"applicationId\":\"app3\",\"rfcId\":\"rfc3\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"source\":\"\",\"changes\":[],\"leadTimeSeconds\":0,\"leadTimePerfLevel\":null}]")));
        }
    }