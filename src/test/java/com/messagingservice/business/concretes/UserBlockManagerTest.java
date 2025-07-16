package com.messagingservice.business.concretes;

import com.messagingservice.core.exception.BlockerUserNotFoundException;
import com.messagingservice.core.exception.UserToBeBlockedNotFoundException;
import com.messagingservice.dao.UserBlockRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.entity.User;
import com.messagingservice.entity.UserBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserBlockManagerTest {
    @Mock
    private UserBlockRepo userBlockRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserBlockManager userBlockManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void blockUser_shouldThrowException_whenBlockerUserNotFound() {
        when(userRepo.findByUsername("blocker")).thenReturn(Optional.empty());
        assertThrows(BlockerUserNotFoundException.class,
                () -> userBlockManager.blockUser("blocker", "blocked"));
    }

    @Test
    void blockUser_shouldThrowException_whenBlockedUserNotFound() {
        when(userRepo.findByUsername("blocker")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("blocked")).thenReturn(Optional.empty());

        assertThrows(UserToBeBlockedNotFoundException.class,
                () -> userBlockManager.blockUser("blocker", "blocked"));
    }

    @Test
    void blockUser_shouldReturnAlreadyBlockedMessage() {
        when(userRepo.findByUsername("blocker")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("blocked")).thenReturn(Optional.of(new User()));
        when(userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase("blocker",
                "blocked"))
                .thenReturn(true);

        UserBlock result = userBlockManager.blockUser("blocker", "blocked");
        assertEquals("User was already blocked.", result);
    }

    @Test
    void blockUser_shouldBlockSuccessfully() {
        when(userRepo.findByUsername("blocker")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("blocked")).thenReturn(Optional.of(new User()));
        when(userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase("blocker",
                "blocked"))
                .thenReturn(false);

        UserBlock result = userBlockManager.blockUser("blocker", "blocked");
        assertEquals("blocked", result.getBlockedUsername());
        verify(userBlockRepo, times(1)).save(any(UserBlock.class));
    }

    @Test
    void isBlocked_shouldReturnTrueIfExists() {
        when(userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase("a",
                "b"))
                .thenReturn(true);
        assertTrue(userBlockManager.isBlocked("a", "b"));
    }

    @Test
    void isBlocked_shouldReturnFalseIfNotExists() {
        when(userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase("a",
                "b"))
                .thenReturn(false);
        assertFalse(userBlockManager.isBlocked("a", "b"));
    }
}
