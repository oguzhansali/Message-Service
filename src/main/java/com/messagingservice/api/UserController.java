package com.messagingservice.api;

import com.messagingservice.business.abstracts.UserService;
import com.messagingservice.dto.LoginRequestDTO;
import com.messagingservice.dto.UserResponseDTO;
import com.messagingservice.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Registers a new user.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "registeredUser", registeredUser
        ));
    }

    //Logs in a user with given username and password.
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        User user = userService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        return ResponseEntity.ok(Map.of(
                "message", "User logged successfully",
                "loggedUser", user
        ));
    }

    //Retrieves a list of all users.
    @GetMapping
    public  ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Deletes a user by their username.
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.ok(Map.of(
                "message", "User deleted",
                "deletedUser", username
        ));

    }

}
