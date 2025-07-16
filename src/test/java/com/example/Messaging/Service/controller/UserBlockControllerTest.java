package com.example.Messaging.Service.controller;

import com.example.Messaging.Service.api.UserBlockController;
import com.example.Messaging.Service.business.abstracts.UserBlockService;
import com.example.Messaging.Service.core.config.GlobalExceptionHandler;
import com.example.Messaging.Service.dto.BlockRequestDTO;
import com.example.Messaging.Service.dto.BlockedUserDTO;
import com.example.Messaging.Service.entity.UserBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserBlockController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class UserBlockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserBlockService userBlockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void blockUser_shouldReturnSuccessMessage() throws Exception {
        BlockRequestDTO dto = new BlockRequestDTO("alice", "bob");

        when(userBlockService.blockUser("alice", "bob")).thenReturn(new UserBlock());

        mockMvc.perform(post("/api/block/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User blocked successfully."));
    }

    @Test
    void getBlockedUsers_shouldReturnList() throws Exception {
        List<BlockedUserDTO> dtoList = Arrays.asList(
                new BlockedUserDTO("bob"),
                new BlockedUserDTO("charlie")
        );

        when(userBlockService.getBlockedUsersByBlocker("alice")).thenReturn(dtoList);

        mockMvc.perform(get("/api/block/alice/blocked-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].blockedUsername").value("bob"))
                .andExpect(jsonPath("$[1].blockedUsername").value("charlie"));
    }
}
