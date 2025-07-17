package com.messagingservice.controller;

import com.messagingservice.api.UserController;
import com.messagingservice.business.abstracts.UserService;
import com.messagingservice.core.config.GlobalExceptionHandler;
import com.messagingservice.core.exception.UserAlreadyExistException;
import com.messagingservice.dto.UserResponseDTO;
import com.messagingservice.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    void registerUserShouldReturnRegisteredUser() throws Exception {
        User user = new User();
        user.setUsername("oguzhan_sali15");
        user.setPassword("sali123");
        user.setEmail("oguzhan_sali@example.com");

        when(userService.registerUser(any())).thenReturn(user);

        String requestBody = """
                {
                  "username": "oguzhan_sali15",
                  "password": "sali123",
                  "email": "oguzhan_sali@example.com"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.registeredUser.username").value("oguzhan_sali15"))
                .andExpect(jsonPath("$.registeredUser.email").value("oguzhan_sali@example.com"))
                .andDo(print());

    }

    @Test
    void registerUserWhenUsernameExistsShouldReturnBadRequest() throws Exception {
        when(userService.registerUser(any()))
                .thenThrow(new UserAlreadyExistException("Username already in use"));

        String requestBody = """
        {
          "username": "existing_user",
          "password": "password123",
          "email": "user@example.com"
        }
        """;

        mockMvc.perform(post("/api/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void loginUserWithValidCredentialsShouldReturnUser() throws Exception {
        User user = new User();
        user.setUsername("oguzhan_sali15");
        user.setPassword("sali123");
        user.setEmail("oguzhan_sali@example.com");

        when(userService.login("oguzhan_sali15", "sali123")).thenReturn(user);

        String requestBody = """
            {
              "username": "oguzhan_sali15",
              "password": "sali123"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void getAllUsersShouldReturnListOfUsers() throws Exception {
        // Arrange
        UserResponseDTO user1 = new UserResponseDTO("1", "oguzhan_sali", "oguzhan@example.com");
        UserResponseDTO user2 = new UserResponseDTO("2", "mehmet_kaya", "mehmet@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("1"))
                .andExpect(jsonPath("$[0].username").value("oguzhan_sali"))
                .andExpect(jsonPath("$[0].email").value("oguzhan@example.com"))
                .andExpect(jsonPath("$[1].userId").value("2"))
                .andExpect(jsonPath("$[1].username").value("mehmet_kaya"))
                .andExpect(jsonPath("$[1].email").value("mehmet@example.com"))
                .andDo(print());
    }
    @Test
    void deleteUserShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("oguzhan");

        mockMvc.perform(delete("/api/users/oguzhan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted"))
                .andExpect(jsonPath("$.deletedUser").value("oguzhan"))
                .andDo(print());

        Mockito.verify(userService).deleteUser("oguzhan");
    }

}
