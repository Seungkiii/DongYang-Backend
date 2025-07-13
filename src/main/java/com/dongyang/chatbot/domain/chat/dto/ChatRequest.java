package com.dongyang.chatbot.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequest {
    private String question;
    
    @JsonProperty("context_count")
    private Integer contextCount = 5; // 기본값 5개
    
    public ChatRequest(String question) {
        this.question = question;
    }
    
    public ChatRequest(String question, Integer contextCount) {
        this.question = question;
        this.contextCount = contextCount;
    }
} 