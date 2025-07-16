package com.messagingservice.business.abstracts;

import com.messagingservice.dto.BlockedUserDTO;
import com.messagingservice.entity.UserBlock;

import java.util.List;

public interface UserBlockService {
    UserBlock blockUser(String blockerUsername, String blockedUsername);

    boolean isBlocked(String blockerUsername, String blockedUsername);

    List<BlockedUserDTO> getBlockedUsersByBlocker(String blockerUsername);


}
