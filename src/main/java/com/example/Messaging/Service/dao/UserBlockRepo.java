package com.example.Messaging.Service.dao;

import com.example.Messaging.Service.entity.UserBlock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepo extends MongoRepository<UserBlock, String> {
    Optional<UserBlock> findByBlockerUsernameAndBlockedUsername(String blocker, String blocked);

    List<UserBlock> findByBlockerUsername(String blocker);

    boolean existsByBlockerUsernameAndBlockedUsername(String blocker, String blocked);

    void deleteByBlockerUsernameAndBlockedUsername(String blocker, String blocked);
    boolean existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(String blockerUsername, String blockedUsername);
    void deleteByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(String blockerUsername, String blockedUsername);
}
