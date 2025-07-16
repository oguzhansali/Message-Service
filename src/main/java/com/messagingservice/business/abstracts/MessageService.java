package com.messagingservice.business.abstracts;

import com.messagingservice.dto.MessageRequestDTO;
import com.messagingservice.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(MessageRequestDTO dto);
    List<Message> getMessagesBySender(String senderUsername);
    List<Message> getMessagesByReceiver(String receiverUsername);
    List<Message> getAllMessages();
    boolean deleteMessageById(String messageId);
}
