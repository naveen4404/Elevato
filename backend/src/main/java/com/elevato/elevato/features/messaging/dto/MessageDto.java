package com.elevato.elevato.features.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class MessageDto {

    @NotNull
    private String content;

    @NotNull
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
