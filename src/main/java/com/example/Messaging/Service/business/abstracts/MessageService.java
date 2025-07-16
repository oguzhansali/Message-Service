package com.example.Messaging.Service.business.abstracts;

import com.example.Messaging.Service.dto.MessageRequestDTO;
import com.example.Messaging.Service.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(MessageRequestDTO dto);
    List<Message> getMessagesBySender(String senderUsername);
    List<Message> getMessagesByReceiver(String receiverUsername);
    List<Message> getAllMessages();
    boolean deleteMessageById(String messageId);
}
