package com.elevato.elevato.features.messaging.model;


import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AuthenticationUser sender;

    @ManyToOne
    private AuthenticationUser receiver;

    @JsonIgnore
    @ManyToOne
    private Conversation conversation;

    @Lob
    private String content;

    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Message() {
    }

    public Message(AuthenticationUser sender, AuthenticationUser receiver, Conversation conversation, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.conversation = conversation;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthenticationUser getSender() {
        return sender;
    }

    public void setSender(AuthenticationUser sender) {
        this.sender = sender;
    }

    public AuthenticationUser getReceiver() {
        return receiver;
    }

    public void setReceiver(AuthenticationUser receiver) {
        this.receiver = receiver;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
