package com.messagingservice.dao;

import com.messagingservice.entity.UserBlock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepo extends MongoRepository<UserBlock, String> {
    List<UserBlock> findByBlockerUsername(String blocker);

    boolean existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(String blockerUsername, String blockedUsername);
}
