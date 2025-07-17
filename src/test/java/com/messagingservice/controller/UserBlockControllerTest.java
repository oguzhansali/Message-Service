package com.messagingservice.controller;

import com.messagingservice.api.UserBlockController;
import com.messagingservice.business.abstracts.UserBlockService;
import com.messagingservice.core.config.GlobalExceptionHandler;
import com.messagingservice.dto.BlockRequestDTO;
import com.messagingservice.dto.BlockedUserDTO;
import com.messagingservice.entity.UserBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    void blockUserShouldReturnSuccessMessage() throws Exception {
        BlockRequestDTO dto = new BlockRequestDTO("alice", "bob");

        UserBlock mockBlock = new UserBlock();
        mockBlock.setId(UUID.randomUUID().toString());

        when(userBlockService.blockUser("alice", "bob")).thenReturn(mockBlock);

        mockMvc.perform(post("/api/blocks/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User blocked successfully"))
                .andExpect(jsonPath("$.blockedUsername").value("bob"))
                .andExpect(jsonPath("$.blockId").value(mockBlock.getId()));
    }

    @Test
    void getBlockedUsersShouldReturnList() throws Exception {
        List<BlockedUserDTO> dtoList = Arrays.asList(
                new BlockedUserDTO("bob"),
                new BlockedUserDTO("charlie")
        );
        when(userBlockService.getBlockedUsersByBlocker("alice")).thenReturn(dtoList);

        mockMvc.perform(get("/api/blocks/blocker/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].blockedUsername").value("bob"))
                .andExpect(jsonPath("$[1].blockedUsername").value("charlie"));
    }
}
