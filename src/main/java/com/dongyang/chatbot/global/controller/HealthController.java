package com.dongyang.chatbot.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "동양생명 보험 상담 백엔드 API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "서버가 정상적으로 작동 중입니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "dongyang-backend");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> apiHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "dongyang-backend-api");
        response.put("timestamp", LocalDateTime.now());
        response.put("endpoints", new String[]{
            "/api/chat-proxy - AI 서버 프록시",
            "/api/insurance - 보험 정보 API",
            "/api/chat - 채팅 API"
        });
        return ResponseEntity.ok(response);
    }
}
