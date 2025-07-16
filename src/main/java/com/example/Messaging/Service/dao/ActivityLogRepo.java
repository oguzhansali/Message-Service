package com.example.Messaging.Service.dao;

import com.example.Messaging.Service.entity.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepo extends MongoRepository<ActivityLog,String> {
    List<ActivityLog> findByUsername(String username);

}
