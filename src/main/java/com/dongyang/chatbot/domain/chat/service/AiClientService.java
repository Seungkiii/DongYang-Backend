package com.dongyang.chatbot.domain.chat.service;

import com.dongyang.chatbot.domain.chat.dto.ChatRequest;
import com.dongyang.chatbot.domain.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiClientService {
    
    @Qualifier("simpleRestTemplate")
    private final RestTemplate restTemplate;
    
    @Value("${AI_SERVICE_URL:http://ai:8000}")
    private String aiServerUrl;
    
    public ChatResponse sendQuestionToAi(ChatRequest request) {
        try {
            String url = aiServerUrl + "/api/chat/question";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // AI 서비스에 맞는 요청 형식으로 변환
            java.util.Map<String, Object> aiRequest = new java.util.HashMap<>();
            aiRequest.put("question", request.getQuestion());
            aiRequest.put("context_count", request.getContextCount() != null ? request.getContextCount() : 5);
            
            HttpEntity<java.util.Map<String, Object>> entity = new HttpEntity<>(aiRequest, headers);
            
            log.info("AI 서버로 요청 전송: {}", url);
            log.info("요청 데이터: {}", aiRequest);
            log.info("AI 서버 URL: {}", aiServerUrl);
            
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(
                url, entity, ChatResponse.class
            );
            
            ChatResponse chatResponse = response.getBody();
            if (chatResponse != null) {
                log.info("AI 서버 응답 수신 완료");
                return chatResponse;
            } else {
                log.warn("AI 서버에서 빈 응답 수신");
                return createErrorResponse("AI 서버에서 빈 응답을 받았습니다.");
            }
            
        } catch (RestClientException e) {
            log.error("AI 서버 통신 오류: {}", e.getMessage(), e);
            return createErrorResponse("AI 서버가 실행되지 않았거나 연결할 수 없습니다. AI 서버를 시작해주세요.");
        } catch (Exception e) {
            log.error("예상치 못한 오류: {}", e.getMessage(), e);
            return createErrorResponse("처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }
    
    private ChatResponse createErrorResponse(String errorMessage) {
        ChatResponse errorResponse = new ChatResponse();
        errorResponse.setAnswer(errorMessage);
        errorResponse.setContexts(Collections.emptyList());
        errorResponse.setConfidence(0.0);
        return errorResponse;
    }
} 