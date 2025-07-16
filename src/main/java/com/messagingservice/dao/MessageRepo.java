package com.messagingservice.dao;

import com.messagingservice.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends MongoRepository<Message,String> {
    List<Message> findBySenderUsername(String senderUsername);
    List<Message> findByReceiverUsername(String receiverUsername);
    void deleteBySenderUsername(String senderUsername);
    void deleteByReceiverUsername(String receiverUsername);




}
