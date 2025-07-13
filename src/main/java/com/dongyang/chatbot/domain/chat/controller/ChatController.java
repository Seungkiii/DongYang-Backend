package com.dongyang.chatbot.domain.chat.controller;

import com.dongyang.chatbot.domain.chat.dto.ChatRequest;
import com.dongyang.chatbot.domain.chat.dto.ChatResponse;
import com.dongyang.chatbot.domain.chat.entity.ChatHistory;
import com.dongyang.chatbot.domain.chat.service.AiClientService;
import com.dongyang.chatbot.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat API", description = "보험설계사 챗봇 API")
public class ChatController {
    
    private final ChatService chatService;
    private final AiClientService aiClientService;
    
    @GetMapping("/test")
    @Operation(summary = "테스트 엔드포인트")
    public ResponseEntity<String> test() {
        log.info("테스트 엔드포인트 호출됨");
        return ResponseEntity.ok("ChatController가 정상적으로 등록되었습니다!");
    }
    
    @PostMapping
    @Operation(
        summary = "챗봇 질문 처리",
        description = "사용자 질문을 AI 백엔드로 전달하고 답변을 반환합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "성공적으로 답변을 받았습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponse.class),
                examples = @ExampleObject(
                    value = "{\"answer\":\"유방암은 일반적으로 보장 대상에 포함됩니다.\",\"contexts\":[\"관련 약관 내용\"],\"confidence\":0.95}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "AI 서버 연결 실패 또는 처리 오류"
        )
    })
    public ResponseEntity<ChatResponse> askQuestion(
        @Parameter(
            description = "사용자 질문",
            required = true,
            example = "{\"question\":\"유방암은 보장 대상입니까?\"}"
        )
        @RequestBody ChatRequest request
    ) {
        log.info("질문 수신: {}", request.getQuestion());
        
        // AI 백엔드로 질문 전달
        ChatResponse aiResponse = aiClientService.sendQuestionToAi(request);
        
        // 채팅 이력 저장
        chatService.saveChatHistory(request.getQuestion(), aiResponse.getAnswer());
        
        log.info("응답 반환 완료");
        return ResponseEntity.ok(aiResponse);
    }
    
    @GetMapping("/history")
    @Operation(
        summary = "채팅 이력 조회",
        description = "저장된 채팅 이력을 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "채팅 이력 조회 성공"
    )
    public ResponseEntity<List<ChatHistory>> getChatHistory() {
        return ResponseEntity.ok(chatService.getChatHistory());
    }
} 