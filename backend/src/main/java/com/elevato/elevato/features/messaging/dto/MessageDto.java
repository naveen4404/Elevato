package com.elevato.elevato.features.messaging.dto;

public class MessageDto {

    private String content;
    private Long receiverId;

    public MessageDto() {
    }

    public MessageDto(String content, Long receiverId) {
        this.content = content;
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
