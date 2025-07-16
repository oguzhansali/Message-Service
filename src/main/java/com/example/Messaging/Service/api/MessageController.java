package com.example.Messaging.Service.api;

import com.example.Messaging.Service.business.abstracts.MessageService;
import com.example.Messaging.Service.core.exception.MessageNotFoundException;
import com.example.Messaging.Service.dto.MessageRequestDTO;
import com.example.Messaging.Service.entity.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageRequestDTO messageRequestDTO){
        Message message = messageService.sendMessage(messageRequestDTO);
        return ResponseEntity.ok(message);

    }

    @GetMapping("/sender/{senderUsername}")
    public  ResponseEntity<List<Message>> getMessagesBySender(@PathVariable String senderUsername){
        List<Message> messages = messageService.getMessagesBySender(senderUsername);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/receiver/{receiverUsername}")
    public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable String receiverUsername){
        List<Message> messages = messageService.getMessagesByReceiver(receiverUsername);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String>deleteMessage(@PathVariable String messageId){
        messageService.deleteMessageById(messageId);
       return ResponseEntity.ok().build();
    }

}
