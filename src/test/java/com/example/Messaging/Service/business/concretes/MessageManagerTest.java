package com.example.Messaging.Service.business.concretes;
import com.example.Messaging.Service.business.abstracts.UserBlockService;
import com.example.Messaging.Service.core.config.GlobalExceptionHandler;
import com.example.Messaging.Service.core.exception.HaveBeenBlockedException;
import com.example.Messaging.Service.core.exception.ReceiverNotFoundException;
import com.example.Messaging.Service.core.exception.SenderNotFoundException;
import com.example.Messaging.Service.dao.MessageRepo;
import com.example.Messaging.Service.dao.UserRepo;
import com.example.Messaging.Service.dto.MessageRequestDTO;
import com.example.Messaging.Service.entity.Message;
import com.example.Messaging.Service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
public class MessageManagerTest {
    @Mock
    private MessageRepo messageRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserBlockService userBlockService;

    @InjectMocks
    private MessageManager messageManager;

    @BeforeEach
     public void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_shouldThrowSenderNotFound() {
        MessageRequestDTO dto = new MessageRequestDTO("unknown", "receiver", "Hi");
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(SenderNotFoundException.class, () -> messageManager.sendMessage(dto));
    }

    @Test
    void sendMessage_shouldThrowReceiverNotFound() {
        MessageRequestDTO dto = new MessageRequestDTO("sender", "unknown", "Hi");
        when(userRepo.findByUsername("sender")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ReceiverNotFoundException.class, () -> messageManager.sendMessage(dto));
    }

//    @Test
//    public void sendMessage_shouldThrowBlockedException() {
//        MessageRequestDTO dto = new MessageRequestDTO("sender", "receiver", "Blocked Message");
//        User sender = new User();
//        sender.setUsername("sender");
//        User receiver = new User();
//        receiver.setUsername("receiver");
//
//        when(userRepo.findByUsername("sender")).thenReturn(Optional.of(sender));
//        when(userRepo.findByUsername("receiver")).thenReturn(Optional.of(receiver));
//        when(userBlockService.isBlocked("sender", "receiver")).thenReturn(true);
//
//        assertThrows(HaveBeenBlockedException.class, () -> messageManager.sendMessage(dto));
//    }

    @Test
    void sendMessage_shouldSucceed() {
        MessageRequestDTO dto = new MessageRequestDTO("sender", "receiver", "Hello");
        when(userRepo.findByUsername("sender")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("receiver")).thenReturn(Optional.of(new User()));
        when(userBlockService.isBlocked(any(), any())).thenReturn(false);

        when(messageRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Message message = messageManager.sendMessage(dto);
        assertEquals("sender", message.getSenderUsername());
        assertEquals("receiver", message.getReceiverUsername());
    }
}
