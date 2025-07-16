package com.messagingservice.business.concretes;

import com.messagingservice.business.abstracts.MessageService;
import com.messagingservice.business.abstracts.UserBlockService;
import com.messagingservice.core.exception.HaveBeenBlockedException;
import com.messagingservice.core.exception.MessageNotFoundException;
import com.messagingservice.core.exception.ReceiverNotFoundException;
import com.messagingservice.core.exception.SenderNotFoundException;
import com.messagingservice.core.utilies.CustomExceptionMessage;
import com.messagingservice.dao.MessageRepo;
import com.messagingservice.dao.UserRepo;
import com.messagingservice.dto.MessageRequestDTO;
import com.messagingservice.entity.Message;
import com.messagingservice.entity.User;
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

    /*
    * Sends a message from a sender to a receiver.
    * Validates sender and receiver existence, checks if users have blocked each other.
    * */
    @Override
    public Message sendMessage(MessageRequestDTO dto) {
        logger.info("Request to send new message received: Sender={}, Recipient={}",
                dto.getSenderUsername(), dto.getReceiverUsername());

        // Verify sender exists
        User sender = userRepo.findByUsername(dto.getSenderUsername())
                .orElseThrow(()->{
                    logger.warn("Sender not found: {}", dto.getSenderUsername());
                    return new SenderNotFoundException(CustomExceptionMessage.SENDER_NOT_FOUND);
                });
        // Verify receiver exists
        User receiver = userRepo.findByUsername(dto.getReceiverUsername())
                .orElseThrow(()->{
                    logger.warn("Receiver not found: {}", dto.getReceiverUsername());
                    return new ReceiverNotFoundException(CustomExceptionMessage.RECEIVER_NOT_FOUND);
                });
        // Check if either user has blocked the other
        if (userBlockService.isBlocked(sender.getUsername(), receiver.getUsername()) ||
                userBlockService.isBlocked(receiver.getUsername(), sender.getUsername())) {

            logger.warn("Message could not be sent. Sender '{}' or receiver '{}' has blocked the other.",
                    sender.getUsername(), receiver.getUsername());
            throw new HaveBeenBlockedException(CustomExceptionMessage.HAVE_BEEN_BLOCKED);
        }

        // Create new message entity and set fields
        Message message = new Message();
        message.setSenderUsername(dto.getSenderUsername());
        message.setReceiverUsername(dto.getReceiverUsername());
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());

        // Save message in the repository
        Message savedMessage= messageRepo.save(message);
        logger.info("Message sent successfully. Sender={}, Recipient={}",
                savedMessage.getSenderUsername(), savedMessage.getReceiverUsername());
        return savedMessage;
    }

    //Retrieves all messages sent by the specified sender username.
    @Override
    public List<Message> getMessagesBySender(String senderUsername) {
        logger.info("Fetching messages from user '{}'.", senderUsername);
        return messageRepo.findBySenderUsername(senderUsername);
    }

    // Retrieves all messages received by the specified receiver username.
    @Override
    public List<Message> getMessagesByReceiver(String receiverUsername) {
        logger.info("Fetching messages from user '{}'.", receiverUsername);
        return messageRepo.findByReceiverUsername(receiverUsername);
    }

    //Retrieves all messages in the system.
    @Override
    public List<Message> getAllMessages() {
        //Fetches all messages and logs the total number of messages
        logger.info("Fetching all messages. Total number of messages: {}", messageRepo.count());
        return messageRepo.findAll();
    }

    //Deletes a message by its ID if it exists.
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
