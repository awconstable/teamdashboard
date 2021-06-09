package team.dashboard.web;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(initializers = {BaseMongoTest.Initializer.class})
public class BaseMongoTest
    {

    @ClassRule
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:3.6.21"));

    @BeforeAll
    static void setUpAll() {
    mongoDBContainer
        .withReuse(true)
        .start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
        {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext)
            {
            TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
            }
        }
    }
