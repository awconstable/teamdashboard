package team.dashboard.web.dora.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class SchedulingTasksApplicationTest
    {
    @Autowired
    private ScheduledDoraMetricLoad tasks;

        @Test
        public void contextLoads() {
            // Basic integration test that shows the context starts up properly
            assertThat(tasks, is(notNullValue()));
        }
    }
