package com.example.Messaging.Service.business.concretes;

import com.example.Messaging.Service.business.abstracts.ActivityLogService;
import com.example.Messaging.Service.dao.ActivityLogRepo;
import com.example.Messaging.Service.entity.ActivityLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogManager implements ActivityLogService {
    private final ActivityLogRepo activityLogRepo;

    public ActivityLogManager(ActivityLogRepo activityLogRepo) {
        this.activityLogRepo = activityLogRepo;
    }

    @Override
    public void logAction(String username, String action, String description) {
        ActivityLog log = new ActivityLog();
        log.setUsername(username);
        log.setAction(action);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepo.save(log);
    }

    @Override
    public List<ActivityLog> getLogsForUser(String username) {
        return  activityLogRepo.findByUsername(username);
    }
}
