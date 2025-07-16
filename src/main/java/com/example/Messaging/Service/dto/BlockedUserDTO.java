package com.example.Messaging.Service.dto;

public class BlockedUserDTO {
    private String blockedUsername;

    public BlockedUserDTO() {
    }

    public BlockedUserDTO(String blockedUsername) {
        this.blockedUsername = blockedUsername;
    }

    public String getBlockedUsername() {
        return blockedUsername;
    }

    public void setBlockedUsername(String blockedUsername) {
        this.blockedUsername = blockedUsername;
    }
}
