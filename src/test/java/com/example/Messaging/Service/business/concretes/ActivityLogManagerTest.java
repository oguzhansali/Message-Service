package com.example.Messaging.Service.business.concretes;

import com.example.Messaging.Service.dao.ActivityLogRepo;
import com.example.Messaging.Service.entity.ActivityLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivityLogManagerTest {
    @Mock
    private ActivityLogRepo activityLogRepo;

    @InjectMocks
    private ActivityLogManager activityLogManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logActionShouldSaveActivityLog() {
        activityLogManager.logAction("alice", "LOGIN", "User logged in");
        verify(activityLogRepo, times(1)).save(any(ActivityLog.class));
    }

    @Test
    void getLogsForUserShouldReturnUserLogs() {
        ActivityLog log1 = new ActivityLog();
        log1.setUsername("alice");
        ActivityLog log2 = new ActivityLog();
        log2.setUsername("alice");

        when(activityLogRepo.findByUsername("alice")).thenReturn(List.of(log1, log2));

        List<ActivityLog> logs = activityLogManager.getLogsForUser("alice");

        assertEquals(2, logs.size());
        assertEquals("alice", logs.get(0).getUsername());
    }
}
