package com.dongyang.chatbot.global.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat-proxy")
public class ChatProxyController {
    private final RestTemplate restTemplate;

    @Value("${ai.server.url:http://localhost:8000/api/chat/question}")
    private String aiServerUrl;

    public ChatProxyController(RestTemplate simpleRestTemplate) {
        this.restTemplate = simpleRestTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> chatWithAI(@RequestBody Map<String, Object> payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            // 로그 출력 및 상세 오류 메시지 반환
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버와의 통신 오류: " + e.getMessage(), e);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "ChatController가 정상적으로 등록되었습니다!";
    }
}
