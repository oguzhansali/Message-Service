package com.example.Messaging.Service.api;

import com.example.Messaging.Service.business.abstracts.ActivityLogService;
import com.example.Messaging.Service.entity.ActivityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogContoller {
    private final ActivityLogService activityLogService;

    public ActivityLogContoller(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ActivityLog>> getLogsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(activityLogService.getLogsForUser(username));
    }

}
