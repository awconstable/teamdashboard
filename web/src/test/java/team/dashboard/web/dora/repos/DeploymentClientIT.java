package team.dashboard.web.dora.repos;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team.dashboard.web.dora.domain.DeploymentFrequency;
import team.dashboard.web.dora.domain.LeadTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = {
    DeploymentClientIT.FakeFeignConfiguration.class,
    DeploymentClientIT.FakeMessagingRestService.class
},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "spring.cloud.loadbalancer.ribbon.enabled=true" })
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class DeploymentClientIT
    {
    @RestController
    static class FakeMessagingRestService
        {
        @GetMapping(path = "/api/v1/deployment/application/{appId}/frequency", produces = "application/json")
        public String getDeploymentFrequency(@PathVariable final String appId)
            {
            assertThat(appId, is(equalTo("app1")));
            
            return "{\"applicationId\":\"app1\",\"reportingDate\":\"2021-01-26\",\"deploymentCount\":1,\"timePeriod\":\"MONTH\",\"deployFreqLevel\":\"MEDIUM\"}";
            }
        
        @GetMapping(path = "/api/v1/deployment/application/{appId}/frequency/{reportingDate}", produces = "application/json")
        public String getDeploymentFrequencyWithDate(@PathVariable final String appId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate reportingDate)
            {
            assertThat(appId, is(equalTo("app1")));
            String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);
            return "{\"applicationId\":\"app1\",\"reportingDate\":\""+ dateOut + "\",\"deploymentCount\":1,\"timePeriod\":\"MONTH\",\"deployFreqLevel\":\"MEDIUM\"}";
            }

        @GetMapping(path = "/api/v1/deployment/application/{appId}/lead_time", produces = "application/json")
        public String getLeadTime(@PathVariable final String appId)
            {
            assertThat(appId, is(equalTo("app1")));

            return "{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"2021-01-26\",\"leadTimeSeconds\":120,\"leadTimePerfLevel\":\"ELITE\"}";
            }

        @GetMapping(path = "/api/v1/deployment/application/{appId}/lead_time/{reportingDate}", produces = "application/json")
        public String getLeadTimeWithDate(@PathVariable final String appId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate reportingDate)
            {
            assertThat(appId, is(equalTo("app1")));
            String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);
            
            return "{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"leadTimeSeconds\":120,\"leadTimePerfLevel\":\"ELITE\"}";
            }
        }

    @Configuration(proxyBeanMethods = false)
    static class FakeRibbonConfiguration
        {

        @LocalServerPort
        int port;

        @Bean
        public ServerList<Server> serverList()
            {
            return new StaticServerList<>(
                new Server("localhost", port));
            }
        }

    @Configuration(proxyBeanMethods = false)
    @EnableFeignClients(clients = DeploymentClient.class)
    @EnableAutoConfiguration
    @RibbonClient(
        name = "deployment-service",
        configuration =
            DeploymentClientIT.FakeRibbonConfiguration.class)
    static class FakeFeignConfiguration
        {
        }             
    
    @Autowired private DeploymentClient deploymentClient;

    @Test
    void getDeployFrequency()
        {
        DeploymentFrequency freq = deploymentClient.getDeployFrequency("app1");
        assertThat(freq.getApplicationId(), is(equalTo("app1")));
        }

    @Test
    void getDeployFrequencyWithDate()
        {
        
        Date reportingDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.of("UTC")).toInstant());
        DeploymentFrequency freq = deploymentClient.getDeployFrequency("app1", reportingDate);
        assertThat(freq.getApplicationId(), is(equalTo("app1")));
        assertThat(freq.getReportingDate(), is(equalTo(reportingDate)));
        }

    @Test
    void getLeadTime()
        {
        LeadTime lt = deploymentClient.getLeadTime("app1");
        assertThat(lt.getApplicationId(), is(equalTo("app1")));
        }

    @Test
    void getLeadTimeWithDate()
        {
        Date reportingDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.of("UTC")).toInstant());
        LeadTime lt = deploymentClient.getLeadTime("app1", reportingDate);
        assertThat(lt.getApplicationId(), is(equalTo("app1")));
        assertThat(lt.getReportingDate(), is(equalTo(reportingDate)));
        }
    }