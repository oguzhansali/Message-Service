package com.example.Messaging.Service.business.abstracts;

import com.example.Messaging.Service.entity.ActivityLog;

import java.util.List;

public interface ActivityLogService {
    void logAction(String username, String action, String description);
    List<ActivityLog> getLogsForUser(String username);
}
