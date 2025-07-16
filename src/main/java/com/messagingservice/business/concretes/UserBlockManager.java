package com.messagingservice.business.concretes;

import com.messagingservice.business.abstracts.UserBlockService;
import com.messagingservice.core.exception.BlockerUserNotFoundException;
import com.messagingservice.core.exception.UserAlreadyBlockedException;
import com.messagingservice.core.exception.UserCannotBlockThemselfException;
import com.messagingservice.core.exception.UserToBeBlockedNotFoundException;
import com.messagingservice.core.utilies.CustomExceptionMessage;
import com.messagingservice.dao.UserBlockRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.dto.BlockedUserDTO;
import com.messagingservice.entity.UserBlock;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBlockManager implements UserBlockService {
    private final UserBlockRepo userBlockRepo;
    private final UserRepo userRepo;
    private static final Logger logger = LoggerFactory.getLogger(UserBlockManager.class);

    public UserBlockManager(UserBlockRepo userBlockRepo,UserRepo userRepo) {
        this.userBlockRepo = userBlockRepo;
        this.userRepo=userRepo;
    }

    //Blocks a user.
    @Override
    public UserBlock blockUser(String blockerUsername, String blockedUsername) {
        // Prevent users from blocking themselves
        if (blockerUsername.equalsIgnoreCase(blockedUsername)) {
            logger.warn("The user tried to block herself: {}");
            throw new UserCannotBlockThemselfException(CustomExceptionMessage.USER_CANNOT_BLOCK_THEMSELF);
        }
        // Verify that the blocker user exists
        userRepo.findByUsername(blockerUsername)
                .orElseThrow(() -> new BlockerUserNotFoundException(CustomExceptionMessage.BLOCKER_USER_NOT_FOUND));
        // Verify that the user to be blocked exists
        userRepo.findByUsername(blockedUsername)
                .orElseThrow(() -> new UserToBeBlockedNotFoundException(CustomExceptionMessage.USER_TO_BE_BLOCKED_NOT_FOUND));
        // Check if the user is already blocked
        boolean alreadyBlocked = userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(
                blockerUsername.trim(), blockedUsername.trim());

        if (alreadyBlocked) {
            logger.info("User {} has already blocked user {}");
            throw new UserAlreadyBlockedException(CustomExceptionMessage.USER_ALREADY_BLOCKED);
        }

        // Create and save the new UserBlock record
        UserBlock userBlock = new UserBlock();
        userBlock.setBlockerUsername(blockerUsername);
        userBlock.setBlockedUsername(blockedUsername);

        userBlockRepo.save(userBlock);
        logger.info("User {} -> Successfully blocked user {}");

        return userBlock;
    }

    //Checks if one user has blocked another.
    @Override
    public boolean isBlocked(String blockerUsername, String blockedUsername) {
        return userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(
                blockerUsername.trim(), blockedUsername.trim());
    }

    //Retrieves a list of users blocked by a given user.
    @Override
    public List<BlockedUserDTO> getBlockedUsersByBlocker(String blockerUsername) {
        List<UserBlock> blocks = userBlockRepo.findByBlockerUsername(blockerUsername);
        return blocks.stream()
                .map(block -> new BlockedUserDTO(block.getBlockedUsername()))
                .collect(Collectors.toList());
    }
}
