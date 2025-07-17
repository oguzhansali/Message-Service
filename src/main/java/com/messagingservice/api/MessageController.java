package com.messagingservice.api;

import com.messagingservice.business.abstracts.MessageService;
import com.messagingservice.dto.MessageRequestDTO;
import com.messagingservice.entity.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //Sends a message from one user to another.
    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageRequestDTO messageRequestDTO){
        Message message = messageService.sendMessage(messageRequestDTO);
        return ResponseEntity.ok(Map.of(
                "message", "Message sent successfully",
                "createdMessage",message
        ));
    }

    //Retrieves all messages sent by a specific user.
    @GetMapping("/sender/{senderUsername}")
    public  ResponseEntity<List<Message>> getMessagesBySender(@PathVariable String senderUsername){
        List<Message> messages = messageService.getMessagesBySender(senderUsername);
        return ResponseEntity.ok(messages);
    }

    // Retrieves all messages received by a specific user.
    @GetMapping("/receiver/{receiverUsername}")
    public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable String receiverUsername){
        List<Message> messages = messageService.getMessagesByReceiver(receiverUsername);
        return ResponseEntity.ok(messages);
    }

    //Deletes a message by its ID.
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?>deleteMessage(@PathVariable String messageId){
        messageService.deleteMessageById(messageId);
       return ResponseEntity.ok(Map.of(
               "message", "Message deleted successfully"
       ));
    }

}
