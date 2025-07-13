package com.dongyang.chatbot.domain.chat.service;

import com.dongyang.chatbot.domain.chat.entity.ChatHistory;
import com.dongyang.chatbot.domain.chat.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {
    private final ChatHistoryRepository chatHistoryRepository;
    
    @Transactional
    public void saveChatHistory(String question, String answer) {
        try {
            ChatHistory chatHistory = new ChatHistory(question, answer);
            chatHistoryRepository.save(chatHistory);
            log.info("채팅 이력 저장 완료: 질문={}, 답변 길이={}", question, answer.length());
        } catch (Exception e) {
            log.error("채팅 이력 저장 실패: {}", e.getMessage(), e);
            // 저장 실패해도 사용자에게는 답변을 제공
        }
    }
    
    public List<ChatHistory> getChatHistory() {
        return chatHistoryRepository.findAll();
    }
} 