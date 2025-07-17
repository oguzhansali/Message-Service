package com.messagingservice.controller;

import com.messagingservice.api.MessageController;
import com.messagingservice.business.abstracts.MessageService;
import com.messagingservice.core.config.GlobalExceptionHandler;
import com.messagingservice.core.exception.MessageNotFoundException;
import com.messagingservice.dto.MessageRequestDTO;
import com.messagingservice.entity.Message;
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
    void sendMessageShouldReturnSavedMessage() throws Exception {
        MessageRequestDTO requestDTO = new MessageRequestDTO("senderUser", "receiverUser", "Hello there!");

        when(messageService.sendMessage(any())).thenReturn(sampleMessage);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Message sent successfully"))
                .andExpect(jsonPath("$.createdMessage.senderUsername").value("senderUser"))
                .andExpect(jsonPath("$.createdMessage.receiverUsername").value("receiverUser"))
                .andExpect(jsonPath("$.createdMessage.content").value("Hello there!"))
                .andDo(print());
    }

    @Test
    void getMessagesBySenderShouldReturnMessageList() throws Exception {
        List<Message> messages = Arrays.asList(sampleMessage);
        when(messageService.getMessagesBySender("senderUser")).thenReturn(messages);

        mockMvc.perform(get("/api/messages/sender/senderUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderUsername").value("senderUser"))
                .andDo(print());
    }

    @Test
    void getMessagesByReceiverShouldReturnMessageList() throws Exception {
        List<Message> messages = Arrays.asList(sampleMessage);
        when(messageService.getMessagesByReceiver("receiverUser")).thenReturn(messages);

        mockMvc.perform(get("/api/messages/receiver/receiverUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiverUsername").value("receiverUser"))
                .andDo(print());
    }

    @Test
    void deleteMessageShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Message deleted successfully"))
                .andDo(print());
    }

    @Test
    void deleteMessageWhenNotFoundShouldReturn404() throws Exception {
        Mockito.doThrow(new MessageNotFoundException("Message not found")).when(messageService).deleteMessageById("999");

        mockMvc.perform(delete("/api/messages/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Message not found"))
                .andDo(print());
    }
}
