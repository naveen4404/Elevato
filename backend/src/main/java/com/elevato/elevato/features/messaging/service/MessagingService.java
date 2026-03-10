package com.elevato.elevato.features.messaging.service;


import com.elevato.elevato.exception.BadRequestException;
import com.elevato.elevato.exception.ForbiddenException;
import com.elevato.elevato.exception.ResourceNotFoundException;
import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.authentication.service.AuthenticationService;
import com.elevato.elevato.features.messaging.model.Conversation;
import com.elevato.elevato.features.messaging.model.Message;
import com.elevato.elevato.features.messaging.repository.ConversationRepository;
import com.elevato.elevato.features.messaging.repository.MessageRepository;
import com.elevato.elevato.features.notifications.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessagingService {
    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    public MessagingService(ConversationRepository conversationRepository, MessageRepository messageRepository, AuthenticationService authenticationService, NotificationService notificationService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.authenticationService = authenticationService;
        this.notificationService = notificationService;
    }


    public List<Conversation> getConversations(AuthenticationUser user) {
        return conversationRepository.findByAuthorOrRecipient(user,user);
    }

    public Conversation getConversation(AuthenticationUser user, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                                    .orElseThrow(
                                            ()-> new ResourceNotFoundException("Conversation not found")
                                    );
        if(!conversation.getAuthor().getId().equals(user.getId()) &&
            !conversation.getRecipient().getId().equals(user.getId())
        ){
            throw new ForbiddenException("User not allowed to view this conversation");
        }
        return conversation;
    }

    public Conversation createConversationAndMessage(AuthenticationUser sender, Long receiverId, String content) {

        AuthenticationUser receiver = authenticationService.getUserById(receiverId);
        conversationRepository.findByAuthorAndRecipient(sender, receiver)
                .ifPresentOrElse((conversation)-> {
                    throw new BadRequestException("Conversation already exists.");
                }, ()->{});
        conversationRepository.findByAuthorAndRecipient( receiver, sender)
                .ifPresentOrElse((conversation)-> {
                    throw new BadRequestException("Conversation already exists.");
                }, ()->{});

        Conversation conversation = conversationRepository.save(new Conversation(sender,receiver));
        Message message = new Message(sender,receiver,conversation,content);
        messageRepository.save(message);
        conversation.getMessages().add(message);
        notificationService.sendConversationToUsers(sender.getId(), receiver.getId(),conversation);
        return conversation;

    }

    public Message addMessageToConversation(AuthenticationUser sender, Long receiverId, String content, Long conversationId) {
        AuthenticationUser receiver = authenticationService.getUserById(receiverId);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Conversation not found")
                );
        if (!conversation.getAuthor().getId().equals(sender.getId()) && !conversation.getRecipient().getId().equals(sender.getId())) {
            throw new IllegalArgumentException("User not authorized to send message to this conversation");
        }

        if (!conversation.getAuthor().getId().equals(receiver.getId()) && !conversation.getRecipient().getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Receiver is not part of this conversation");
        }


        Message message = new Message(sender, receiver, conversation, content);
        messageRepository.save(message);
        conversation.getMessages().add(message);
        notificationService.sendMessageToConversation(conversation.getId(), message);
        notificationService.sendConversationToUsers(sender.getId(), receiver.getId(), conversation);
        return message;
    }

    public void markMessageAsRead(AuthenticationUser user, Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Message not found")
                );
        if(!message.getReceiver().getId().equals(user.getId())){
            throw new ForbiddenException("User forbidden from marking message as read.");
        }

        if(!message.isRead()){
            message.setRead(true);
            messageRepository.save(message);
        }

    }
}
