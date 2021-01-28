package team.dashboard.web.hierarchy.repos;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team.dashboard.web.hierarchy.domain.HierarchyEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    FeignHierarchyClientIT.FakeFeignConfiguration.class,
    FeignHierarchyClientIT.FakeMessagingRestService.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "spring.cloud.loadbalancer.ribbon.enabled=true" })
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class FeignHierarchyClientIT
    {
    @RestController
    static class FakeMessagingRestService
        {
        @GetMapping(path = "/v2/hierarchy/relatives/{slug}", produces = "application/json")
        public String getRelatives(@PathVariable final String slug)
            {
            assertThat(slug).isEqualTo("test1");
            return "{\"id\":\"testid12345\",\"slug\":\"test1\",\"entityType\":{\"key\":\"TEAM\",\"name\":\"Team\"},\"name\":\"test1\",\"parentSlug\":\"test2\",\"ancestors\":[{\"slug\":\"test\",\"entityType\":{\"key\":\"COMPANY\",\"name\":\"Company\"},\"name\":\"test\",\"parentSlug\":\"\",\"children\":null},{\"slug\":\"test2\",\"entityType\":{\"key\":\"APPLICATION\",\"name\":\"Application\"},\"name\":\"test2\",\"parentSlug\":\"test\",\"children\":null}],\"children\":[],\"workTrackingTools\":[],\"members\":[],\"applicationIds\":[]}";
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
    @EnableFeignClients(clients = HierarchyClient.class)
    @EnableAutoConfiguration
    @RibbonClient(
        name = "team-service",
        configuration =
            FeignHierarchyClientIT.FakeRibbonConfiguration.class)
    static class FakeFeignConfiguration
        {
        }
    
    @Autowired
    HierarchyClient hierarchyClient;
    
    @Test
    public void testGetRelatives(){
        HierarchyEntity entity = hierarchyClient.findEntityBySlug("test1");
        assertThat(entity.getId()).isEqualTo("testid12345");
    }
    }
