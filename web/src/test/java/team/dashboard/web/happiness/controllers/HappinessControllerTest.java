package team.dashboard.web.happiness.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import team.dashboard.web.happiness.services.HappinessService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HappinessController.class)
class HappinessControllerTest
    {
    @Autowired private MockMvc mockMvc;
    @MockBean private HappinessService mockHappinessService;
    
    @Test
    void testLoadHappinessTrend() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of("UTC"));
        
        mockMvc.perform(get("/happiness/load/happiness/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(mockHappinessService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }

    @Test
    void testLoadHappinessTrendWithDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.now().minusDays(10).atStartOfDay(ZoneId.of("UTC"));

        String reportingDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);

        mockMvc.perform(get("/happiness/load/happiness/" + reportingDateString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(mockHappinessService, times(1)).loadAll(Date.from(reportingDate.toInstant()));
        }
    }