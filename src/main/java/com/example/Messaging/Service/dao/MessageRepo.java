package com.example.Messaging.Service.dao;

import com.example.Messaging.Service.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends MongoRepository<Message,String> {
    List<Message> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
    List<Message> findBySenderUsername(String senderUsername);
    List<Message> findByReceiverUsername(String receiverUsername);
    void deleteBySenderUsername(String senderUsername);
    void deleteByReceiverUsername(String receiverUsername);




}
