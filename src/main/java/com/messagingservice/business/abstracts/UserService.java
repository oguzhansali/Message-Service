package com.messagingservice.business.abstracts;

import com.messagingservice.dto.UserResponseDTO;
import com.messagingservice.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User login(String username, String password);
    List<UserResponseDTO> getAllUsers();
    void deleteUser(String username);
}
