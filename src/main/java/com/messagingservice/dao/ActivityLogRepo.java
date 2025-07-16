package com.messagingservice.dao;

import com.messagingservice.entity.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepo extends MongoRepository<ActivityLog,String> {
    List<ActivityLog> findByUsername(String username);

}
