package team.dashboard.web.metrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeamMetricsControllerTest
    {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMetricRepository mockTeamMetricRepository;

    private LocalDate now;

    private TeamMetric metric;

    @Before
    public void init()
        {
        now = LocalDate.now();
        metric = new TeamMetric("team1", TeamMetricType.CYCLE_TIME, new Double(12), now);
        when(mockTeamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now)).thenReturn(Optional.of(metric));
        }

    @Test
    public void metricIngest() throws Exception
        {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = now.format(formatter);

        mockMvc.perform(get("/metrics/" + TeamMetricType.CYCLE_TIME.getKey() + "/team1/" + formattedString + "/12"))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(mockTeamMetricRepository, times(1)).findByTeamIdAndTeamMetricTypeAndDate("team1", TeamMetricType.CYCLE_TIME, now);
        verify(mockTeamMetricRepository, times(1)).save(metric);

        }
    }
