package com.example.Messaging.Service.api;

import com.example.Messaging.Service.business.abstracts.UserService;
import com.example.Messaging.Service.dto.LoginRequestDTO;
import com.example.Messaging.Service.dto.UserResponseDTO;
import com.example.Messaging.Service.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);//created oalrak değişebilr
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        User user = userService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public  ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.ok().build();

    }



}
