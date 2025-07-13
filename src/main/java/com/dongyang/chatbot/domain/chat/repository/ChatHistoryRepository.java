package com.dongyang.chatbot.domain.chat.repository;

import com.dongyang.chatbot.domain.chat.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
} 