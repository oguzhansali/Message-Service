package com.messagingservice.api;

import com.messagingservice.business.abstracts.ActivityLogService;
import com.messagingservice.entity.ActivityLog;
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

    //Retrieves all activity logs for a given username.
    @GetMapping("/{username}")
    public ResponseEntity<List<ActivityLog>> getLogsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(activityLogService.getLogsForUser(username));
    }

}
