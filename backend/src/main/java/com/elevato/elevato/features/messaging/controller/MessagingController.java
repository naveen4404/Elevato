package com.elevato.elevato.features.messaging.controller;


import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.messaging.dto.MessageDto;
import com.elevato.elevato.features.messaging.model.Conversation;
import com.elevato.elevato.features.messaging.model.Message;
import com.elevato.elevato.features.messaging.service.MessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messaging")
public class MessagingController {

    private final MessagingService messagingService;


    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getConversations(@RequestAttribute("authenticatedUser")AuthenticationUser user){
        List<Conversation> conversations = messagingService.getConversations(user);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@RequestAttribute("authenticatedUser")AuthenticationUser user, @PathVariable Long conversationId){
        Conversation conversation = messagingService.getConversation(user, conversationId);
        return ResponseEntity.ok(conversation);
    }

    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversationAndMessage(@RequestAttribute("authenticatedUser") AuthenticationUser sender, @RequestBody MessageDto messageDto){
        Conversation conversation = messagingService.createConversationAndMessage(sender, messageDto.getReceiverId(), messageDto.getContent());
        return ResponseEntity.created(null).body(conversation);
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Message> addMessageToConversation(@RequestAttribute("authenticatedUser") AuthenticationUser sender, @RequestBody MessageDto messageDto, @PathVariable Long conversationId){
        Message message = messagingService.addMessageToConversation(sender,messageDto.getReceiverId(),messageDto.getContent(),conversationId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/conversations/messages/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@RequestAttribute("authenticatedUser") AuthenticationUser user, @PathVariable Long messageId){
        messagingService.markMessageAsRead(user,messageId);
        return ResponseEntity.status(200).build();
    }

}
