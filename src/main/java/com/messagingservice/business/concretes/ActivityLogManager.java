package com.messagingservice.business.concretes;

import com.messagingservice.business.abstracts.ActivityLogService;
import com.messagingservice.dao.ActivityLogRepo;
import com.messagingservice.entity.ActivityLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogManager implements ActivityLogService {
    private final ActivityLogRepo activityLogRepo;

    public ActivityLogManager(ActivityLogRepo activityLogRepo) {
        this.activityLogRepo = activityLogRepo;
    }

    //Logs an action performed by a user with the given details.
    @Override
    public void logAction(String username, String action, String description) {
        ActivityLog log = new ActivityLog();
        log.setUsername(username);
        log.setAction(action);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepo.save(log);
    }

    // Retrieves all activity logs for the specified user.
    @Override
    public List<ActivityLog> getLogsForUser(String username) {
        return  activityLogRepo.findByUsername(username);
    }
}
