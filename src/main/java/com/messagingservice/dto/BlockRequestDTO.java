package com.messagingservice.dto;

import lombok.Data;

public class BlockRequestDTO {
    private String blockerUsername;
    private String blockedUsername;

    public BlockRequestDTO(String blockerUsername, String blockedUsername) {
        this.blockerUsername = blockerUsername;
        this.blockedUsername = blockedUsername;
    }

    public String getBlockerUsername() {
        return blockerUsername;
    }

    public void setBlockerUsername(String blockerUsername) {
        this.blockerUsername = blockerUsername;
    }

    public String getBlockedUsername() {
        return blockedUsername;
    }

    public void setBlockedUsername(String blockedUsername) {
        this.blockedUsername = blockedUsername;
    }
}
