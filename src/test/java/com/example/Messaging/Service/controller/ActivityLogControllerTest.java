package com.example.Messaging.Service.controller;

import com.example.Messaging.Service.api.ActivityLogContoller;
import com.example.Messaging.Service.business.abstracts.ActivityLogService;
import com.example.Messaging.Service.core.config.GlobalExceptionHandler;
import com.example.Messaging.Service.entity.ActivityLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityLogContoller.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class ActivityLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityLogService activityLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getLogsByUsername_shouldReturnLogs() throws Exception {
        ActivityLog log = new ActivityLog();
        log.setUsername("alice");
        log.setAction("LOGIN");
        log.setDescription("User logged in");
        log.setTimestamp(LocalDateTime.now());

        when(activityLogService.getLogsForUser("alice")).thenReturn(List.of(log));

        mockMvc.perform(get("/api/activity-logs/alice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("alice"))
                .andExpect(jsonPath("$[0].action").value("LOGIN"));
    }
}
