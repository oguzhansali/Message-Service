package com.messagingservice.business.concretes;
import com.messagingservice.business.abstracts.UserBlockService;
import com.messagingservice.core.config.GlobalExceptionHandler;
import com.messagingservice.core.exception.HaveBeenBlockedException;
import com.messagingservice.core.exception.ReceiverNotFoundException;
import com.messagingservice.core.exception.SenderNotFoundException;
import com.messagingservice.dao.MessageRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.dto.MessageRequestDTO;
import com.messagingservice.entity.Message;
import com.messagingservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void sendMessageShouldThrowSenderNotFound() {
        MessageRequestDTO dto = new MessageRequestDTO("unknown", "receiver", "Hi");
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(SenderNotFoundException.class, () -> messageManager.sendMessage(dto));
    }

    @Test
    void sendMessageShouldThrowReceiverNotFound() {
        MessageRequestDTO dto = new MessageRequestDTO("sender", "unknown", "Hi");
        when(userRepo.findByUsername("sender")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ReceiverNotFoundException.class, () -> messageManager.sendMessage(dto));
    }

    @Test
    public void sendMessageShouldThrowBlockedException() {
        MessageRequestDTO dto = new MessageRequestDTO("sender", "receiver", "Blocked Message");
        User sender = new User();
        sender.setUsername("sender");
        User receiver = new User();
        receiver.setUsername("receiver");

        when(userRepo.findByUsername("sender")).thenReturn(Optional.of(sender));
        when(userRepo.findByUsername("receiver")).thenReturn(Optional.of(receiver));
        when(userBlockService.isBlocked("sender", "receiver")).thenReturn(true);

        assertThrows(HaveBeenBlockedException.class, () -> messageManager.sendMessage(dto));
    }

    @Test
    void sendMessageShouldSucceed() {
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
