package com.example.Messaging.Service.business.abstracts;

import com.example.Messaging.Service.dto.BlockedUserDTO;
import com.example.Messaging.Service.entity.UserBlock;

import java.util.List;

public interface UserBlockService {
    UserBlock blockUser(String blockerUsername, String blockedUsername);

    String unblockUser(String blockerUsername, String blockedUsername);

    boolean isBlocked(String blockerUsername, String blockedUsername);

    List<BlockedUserDTO> getBlockedUsersByBlocker(String blockerUsername);


}
