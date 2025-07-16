package com.messagingservice.business.abstracts;

import com.messagingservice.entity.ActivityLog;

import java.util.List;

public interface ActivityLogService {
    void logAction(String username, String action, String description);
    List<ActivityLog> getLogsForUser(String username);
}
