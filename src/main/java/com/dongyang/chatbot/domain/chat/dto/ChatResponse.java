package com.dongyang.chatbot.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponse {
    private String answer;
    private List<String> contexts;
    private Double confidence;
    
    @JsonProperty("processing_time")
    private Integer processingTime;
    
    private String intent;
    private java.util.Map<String, Object> parameters;
    
    public ChatResponse(String answer) {
        this.answer = answer;
    }
    
    public ChatResponse(String answer, List<String> contexts, Double confidence) {
        this.answer = answer;
        this.contexts = contexts;
        this.confidence = confidence;
    }
} 