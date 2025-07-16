package com.messagingservice.entity;

import com.mongodb.connection.ProxySettings;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "activity_logs")
public class ActivityLog {
    private String id;
    private String username;
    private String action;
    private String description;
    private LocalDateTime timestamp;

    public ActivityLog(String id,
                       String username,
                       String action,
                       String description,
                       LocalDateTime timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.description = description;
        this.timestamp = timestamp;
    }

    public ActivityLog() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
