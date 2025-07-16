package com.example.Messaging.Service.business.abstracts;

import com.example.Messaging.Service.dto.UserResponseDTO;
import com.example.Messaging.Service.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);//Kayıt
    User login(String username, String password);//Giriş
    List<UserResponseDTO> getAllUsers();//Tum kullanicileri getir
    void deleteUser(String username);
}
