package com.messagingservice.business.concretes;

import com.messagingservice.business.abstracts.ActivityLogService;
import com.messagingservice.business.abstracts.UserService;
import com.messagingservice.core.utilies.CustomExceptionMessage;
import com.messagingservice.dao.MessageRepo;
import com.messagingservice.dao.UserBlockRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.dto.UserResponseDTO;
import com.messagingservice.entity.User;
import com.messagingservice.core.exception.InvalidCredentialsException;
import com.messagingservice.core.exception.UserAlreadyExistException;
import com.messagingservice.core.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager implements UserService {

    private final UserRepo userRepo;
    private final MessageRepo messageRepo;
    private final UserBlockRepo userBlockRepo;
    private final ActivityLogService activityLogService;
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserManager(UserRepo userRepo,
                       MessageRepo messageRepo,
                       UserBlockRepo userBlockRepo,
                       ActivityLogService activityLogService) {
        this.userRepo = userRepo;
        this.messageRepo=messageRepo;
        this.userBlockRepo=userBlockRepo;
        this.activityLogService=activityLogService;
    }

    /*
    * Registers a new user.
    * If the username has already been used,
    * the registration will fail and a special exception will be thrown.
    */
    @Override
    public User registerUser(User user) {
        if (userRepo.existsByUsername(user.getUsername())){
            logger.warn("Registration failed: Username already in use-> {}",user.getUsername());
            activityLogService.logAction(user.getUsername(),
                    "REGISTER_FAILED", "Username already in use.");
            throw  new UserAlreadyExistException(CustomExceptionMessage.USER_ALREADY_EXIST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("New user registration successful -> {}",user.getUsername());
        activityLogService.logAction(user.getUsername(),
                "REGISTER_SUCCESS", "User registered successfully.");
        return userRepo.save(user);
    }

    /*
    * Logs in with the specified username and password.
    * */
    @Override
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(()-> {
                    logger.warn("Login failed: User not found -> {}", username);
                    activityLogService.logAction(username, "LOGIN_FAILED", "User not found.");
                    throw new UserNotFoundException(CustomExceptionMessage.USER_NOT_FOUND);
                });
        //Check if the password is correct
        if (!passwordEncoder.matches(password,user.getPassword())){
            logger.warn("Login Failed: Password is incorrect -> {}",username);
            activityLogService.logAction(username, "LOGIN_FAILED", "Incorrect password.");
            throw new InvalidCredentialsException(CustomExceptionMessage.INVALID_CREDENTIALS);
        }
        logger.info("Login Successful: {}",username);
        activityLogService.logAction(username, "LOGIN_SUCCESS", "User logged in successfully.");
        return user;
    }

    /*
    * Retrieves all users in the database and
    * returns each user as a User Response DTO.
    */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        logger.info("Fetching all users. Total number of users: {}", userRepo.count());
        return userRepo.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .toList();
    }

    @Override
    public void deleteUser(String username) {
        logger.info("User deletion process initiated: {}", username);
        //User control
        User user = userRepo.findByUsername(username)
                .orElseThrow(()->{
                    logger.warn("The user to be deleted could not be found.: {}", username);
                     return new  UserNotFoundException("User not found:" + username);
                });

        //Remove from other users block list
        userBlockRepo.deleteAll(userBlockRepo.findByBlockerUsername(username));
        logger.info("Deleted block records where user '{}' was the blocker.", username);

        userBlockRepo.deleteAll(
                userBlockRepo.findAll().stream()
                        .filter(block -> block.getBlockedUsername().equalsIgnoreCase(username))
                        .toList()
        );
        logger.info("Deleted block records where user '{}' was the blocked one.", username);

        //Delete messages sent and received by the user
        messageRepo.deleteBySenderUsername(username);
        logger.info("Messages sent by {} have been deleted.", username);

        messageRepo.deleteByReceiverUsername(username);
        logger.info("Messages received by {} have been deleted.", username);

        //Delete user
        userRepo.delete(user);
        logger.info("User successfully deleted: {}", username);

    }
}
