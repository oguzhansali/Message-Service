package com.example.Messaging.Service.business.concretes;

import com.example.Messaging.Service.business.abstracts.MessageService;
import com.example.Messaging.Service.business.abstracts.UserBlockService;
import com.example.Messaging.Service.core.exception.HaveBeenBlockedException;
import com.example.Messaging.Service.core.exception.MessageNotFoundException;
import com.example.Messaging.Service.core.exception.ReceiverNotFoundException;
import com.example.Messaging.Service.core.exception.SenderNotFoundException;
import com.example.Messaging.Service.core.utilies.CustomExceptionMessage;
import com.example.Messaging.Service.dao.MessageRepo;
import com.example.Messaging.Service.dao.UserRepo;
import com.example.Messaging.Service.dto.MessageRequestDTO;
import com.example.Messaging.Service.entity.Message;
import com.example.Messaging.Service.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageManager implements MessageService {

    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final UserBlockService userBlockService;
    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    public MessageManager(MessageRepo messageRepo, UserRepo userRepo,UserBlockService userBlockService) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.userBlockService=userBlockService;
    }

    @Override
    public Message sendMessage(MessageRequestDTO dto) {
        logger.info("Request to send new message received: Sender={}, Recipient={}",
                dto.getSenderUsername(), dto.getReceiverUsername());

        //Check if there is a sender
        User sender = userRepo.findByUsername(dto.getSenderUsername())
                .orElseThrow(()->{
                    logger.warn("Sender not found: {}", dto.getSenderUsername());
                    return new SenderNotFoundException(CustomExceptionMessage.SENDER_NOT_FOUND);
                });

        //Check if there is a receiver
        User receiver = userRepo.findByUsername(dto.getReceiverUsername())
                .orElseThrow(()->{
                    logger.warn("Receiver not found: {}", dto.getReceiverUsername());
                    return new ReceiverNotFoundException(CustomExceptionMessage.RECEIVER_NOT_FOUND);
                });

        //Blocked user control
        if (userBlockService.isBlocked(sender.getUsername(), receiver.getUsername()) ||
                userBlockService.isBlocked(receiver.getUsername(), sender.getUsername())) {

            logger.warn("Message could not be sent. Sender '{}' or receiver '{}' has blocked the other.",
                    sender.getUsername(), receiver.getUsername());
            throw new HaveBeenBlockedException(CustomExceptionMessage.HAVE_BEEN_BLOCKED);
        }

        //Creating and saving messages
        Message message = new Message();//Builder metodu çalışmadaı JDK ile sorun var kontrol et!!!!
        message.setSenderUsername(dto.getSenderUsername());
        message.setReceiverUsername(dto.getReceiverUsername());
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage= messageRepo.save(message);
        logger.info("Message sent successfully. Sender={}, Recipient={}",
                savedMessage.getSenderUsername(), savedMessage.getReceiverUsername());
        return savedMessage;
    }

    @Override
    public List<Message> getMessagesBySender(String senderUsername) {
        //Returns all messages from the specified sender username.
        logger.info("Fetching messages from user '{}'.", senderUsername);
        return messageRepo.findBySenderUsername(senderUsername);
    }

    @Override
    public List<Message> getMessagesByReceiver(String receiverUsername) {
        //Returns all messages from the specified recipient username.
        logger.info("Fetching messages from user '{}'.", receiverUsername);
        return messageRepo.findByReceiverUsername(receiverUsername);
    }

    @Override
    public List<Message> getAllMessages() {
        //Fetches all messages and logs the total number of messages
        logger.info("Fetching all messages. Total number of messages: {}", messageRepo.count());
        return messageRepo.findAll();
    }

    @Override
    public boolean deleteMessageById(String messageId) {
        //If there is a message with the given ID, it deletes it
        if (!messageRepo.existsById(messageId)){
            logger.warn("The message you wanted to delete was not found. ID={}", messageId);
            throw new MessageNotFoundException(CustomExceptionMessage.MESSAGE_NOT_FOUND);
        }
        messageRepo.deleteById(messageId);
        logger.info("Message deleted successfully. ID={}", messageId);
        return true;
    }
}
