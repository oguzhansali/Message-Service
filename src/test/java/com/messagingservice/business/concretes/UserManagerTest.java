package com.messagingservice.business.concretes;

import com.messagingservice.dao.MessageRepo;
import com.messagingservice.dao.UserBlockRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.dto.UserResponseDTO;
import com.messagingservice.entity.User;
import com.messagingservice.business.abstracts.ActivityLogService;
import com.messagingservice.core.exception.InvalidCredentialsException;
import com.messagingservice.core.exception.UserAlreadyExistException;
import com.messagingservice.core.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserManagerTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private MessageRepo messageRepo;

    @Mock
    private UserBlockRepo userBlockRepo;

    @Mock
    private ActivityLogService activityLogService;

    @InjectMocks
    private UserManager userManager;

    @Test
    void registerUserSuccessfully() {
        User user = new User(null, "oguzhan", "pass123", "mail@mail.com");
        when(userRepo.existsByUsername("oguzhan")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenReturn(user);

        User result = userManager.registerUser(user);

        assertEquals("oguzhan", result.getUsername());
        verify(activityLogService).logAction("oguzhan", "REGISTER_SUCCESS", "User registered successfully.");
    }

    @Test
    void registerUserAlreadyExistsThrowsException() {
        User user = new User(null, "oguzhan", "pass", "mail@mail.com");
        when(userRepo.existsByUsername("oguzhan")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userManager.registerUser(user));
    }

    @Test
    void loginSuccess() {
        User user = new User("1", "oguzhan", new BCryptPasswordEncoder().encode("123456"), "mail");
        when(userRepo.findByUsername("oguzhan")).thenReturn(Optional.of(user));

        User result = userManager.login("oguzhan", "123456");
        assertEquals("oguzhan", result.getUsername());
    }

    @Test
    void loginWrongPasswordThrowsException() {
        User user = new User("1", "oguzhan", new BCryptPasswordEncoder().encode("realpass"), "mail");
        when(userRepo.findByUsername("oguzhan")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> userManager.login("oguzhan", "wrongpass"));
    }

    @Test
    void getAllUsersReturnsListOfDTOs() {
        List<User> users = List.of(
                new User("1", "oguzhan", "pass", "mail"),
                new User("2", "mehmet", "pass", "mail2")
        );
        when(userRepo.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userManager.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("oguzhan", result.get(0).getUsername());
    }

    @Test
    void deleteUserSuccess() {
        User user = new User("1", "oguzhan", "pass", "mail");
        when(userRepo.findByUsername("oguzhan")).thenReturn(Optional.of(user));

        userManager.deleteUser("oguzhan");

        verify(userRepo).delete(user);
    }

    @Test
    void deleteUserNotFoundThrowsException() {
        when(userRepo.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userManager.deleteUser("missing"));
    }
}


