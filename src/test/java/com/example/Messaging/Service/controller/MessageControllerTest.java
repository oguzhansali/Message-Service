package com.example.Messaging.Service.controller;

import com.example.Messaging.Service.api.MessageController;
import com.example.Messaging.Service.business.abstracts.MessageService;
import com.example.Messaging.Service.core.config.GlobalExceptionHandler;
import com.example.Messaging.Service.core.exception.MessageNotFoundException;
import com.example.Messaging.Service.dto.MessageRequestDTO;
import com.example.Messaging.Service.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private Message sampleMessage;

    @BeforeEach
    void setup() {
        sampleMessage = new Message();
        sampleMessage.setMessageId("1");
        sampleMessage.setSenderUsername("senderUser");
        sampleMessage.setReceiverUsername("receiverUser");
        sampleMessage.setContent("Hello there!");
        sampleMessage.setTimestamp(LocalDateTime.now());
    }

    @Test
    void sendMessage_shouldReturnSavedMessage() throws Exception {
        MessageRequestDTO requestDTO = new MessageRequestDTO("senderUser", "receiverUser", "Hello there!");

        when(messageService.sendMessage(any())).thenReturn(sampleMessage);

        mockMvc.perform(post("/api/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderUsername").value("senderUser"))
                .andExpect(jsonPath("$.receiverUsername").value("receiverUser"))
                .andExpect(jsonPath("$.content").value("Hello there!"))
                .andDo(print());
    }

    @Test
    void getMessagesBySender_shouldReturnMessageList() throws Exception {
        List<Message> messages = Arrays.asList(sampleMessage);
        when(messageService.getMessagesBySender("senderUser")).thenReturn(messages);

        mockMvc.perform(get("/api/messages/senderUser/sender"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderUsername").value("senderUser"))
                .andDo(print());
    }

    @Test
    void getMessagesByReceiver_shouldReturnMessageList() throws Exception {
        List<Message> messages = Arrays.asList(sampleMessage);
        when(messageService.getMessagesByReceiver("receiverUser")).thenReturn(messages);

        mockMvc.perform(get("/api/messages/receiverUser/receiver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiverUsername").value("receiverUser"))
                .andDo(print());
    }

    @Test
    void getAllMessages_shouldReturnAllMessages() throws Exception {
        List<Message> messages = Arrays.asList(sampleMessage);
        when(messageService.getAllMessages()).thenReturn(messages);

        mockMvc.perform(get("/api/messages/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageId").value("1"))
                .andDo(print());
    }

    @Test
    void deleteMessage_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/api/messages/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    void deleteMessage_whenNotFound_shouldReturn404() throws Exception {
        doThrow(new MessageNotFoundException("Message not found")).when(messageService).deleteMessageById("999");

        mockMvc.perform(delete("/api/messages/999/delete"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Message not found"))
                .andDo(print());
    }
}
