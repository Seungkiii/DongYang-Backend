# 동양생명 보험 챗봇 - Backend API 서버

## 프로젝트 개요
동양생명 보험 챗봇의 백엔드 API 서버입니다. Spring Boot 기반으로 구축되었으며, 프론트엔드와 AI 서버 간의 중계 역할을 담당합니다.

## 주요 기능
- **REST API 제공**: 프론트엔드의 채팅 요청을 처리
- **AI 서버 연동**: FastAPI 기반 AI 서버와 HTTP 통신
- **세션 관리**: 사용자 채팅 세션 및 상태 관리
- **로깅 및 모니터링**: 요청/응답 로깅 및 시스템 모니터링
- **에러 핸들링**: 견고한 예외 처리 및 사용자 친화적 에러 응답

## 기술 스택
- **Java 17**
- **Spring Boot 3.x**
- **Spring Web** (REST API)
- **Spring Data JPA** (데이터베이스 연동)
- **PostgreSQL** (데이터베이스)
- **Docker** (컨테이너화)
- **Maven** (빌드 도구)

## API 명세

### 1. 채팅 API
```http
POST /api/chat
Content-Type: application/json

{
  "question": "무배당엔젤상해보험에 대해 알려주세요",
  "context_count": 8
}
```

**응답 예시:**
```json
{
  "answer": "**상품명**: 무배당엔젤상해보험\n**주요 특징**: 상해로 인한 사망·후유장해...",
  "confidence": 0.85,
  "contexts": [
    {
      "content": "상해보험 관련 문서 내용...",
      "source": "무배당엔젤상해보험.pdf",
      "page": 1
    }
  ]
}
```

### 2. 헬스체크 API
```http
GET /api/health
```

**응답:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-27T12:00:00Z",
  "services": {
    "ai_server": "UP",
    "database": "UP"
  }
}
```

## 환경 설정

### 필수 환경변수
```bash
# AI 서버 연동
AI_SERVER_URL=http://ai:8000

# 데이터베이스
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/chatbot
SPRING_DATASOURCE_USERNAME=chatbot_user
SPRING_DATASOURCE_PASSWORD=secure_password

# JPA 설정
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
```

## 로컬 개발 환경 설정

### 1. 사전 요구사항
- Java 17+
- Maven 3.6+
- Docker (선택사항)

### 2. 프로젝트 실행
```bash
# 저장소 클론
git clone https://github.com/your-username/DongYang-backend.git
cd DongYang-backend

# 환경변수 설정
cp .env.example .env
# .env 파일 편집하여 실제 값 입력

# 의존성 설치 및 빌드
mvn clean install

# 애플리케이션 실행
mvn spring-boot:run
```

### 3. Docker로 실행
```bash
# Docker 이미지 빌드
docker build -t dongyang-backend .

# 컨테이너 실행
docker run -p 8080:8080 --env-file .env dongyang-backend
```

## 테스트

### 단위 테스트 실행
```bash
mvn test
```

### API 테스트
```bash
# 헬스체크
curl http://localhost:8080/api/health

# 채팅 테스트
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"question": "상해보험에 대해 알려주세요", "context_count": 5}'
```

## CI/CD 파이프라인

### GitHub Actions Workflow
`.github/workflows/deploy.yml` 파일을 통해 자동 배포가 구성되어 있습니다.

**배포 트리거:**
- `main` 브랜치에 push 시 자동 배포
- Pull Request 시 테스트 실행

**배포 단계:**
1. **빌드**: Maven을 통한 JAR 파일 생성
2. **Docker 이미지 빌드**: Dockerfile 기반 이미지 생성
3. **EC2 배포**: SSH를 통한 원격 서버 배포
4. **서비스 재시작**: Docker Compose를 통한 무중단 배포
5. **헬스체크**: 배포 후 서비스 상태 확인

**환경변수 및 시크릿 관리:**
- GitHub Secrets를 통한 민감 정보 관리
- `EC2_HOST`, `EC2_USERNAME`, `EC2_PRIVATE_KEY`
- `AI_SERVER_URL`, `DATABASE_URL` 등

### 배포 순서
1. AI 서버 배포 완료 대기
2. Backend 서비스 빌드 및 배포
3. Frontend와의 연동 테스트
4. 전체 시스템 헬스체크

## 통합 및 연동

### AI 서버와의 통신
```java
// src/main/java/com/dongyang/chatbot/service/AIServerClient.java
@Service
@Slf4j
public class AIServerClient {
    
    @Value("${ai.server.url}")
    private String aiServerUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public AIServerClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * AI 서버에 채팅 요청 전송
     * @param request 사용자 질문 및 컨텍스트 수
     * @return AI 서버 응답 (답변, 신뢰도, 컨텍스트)
     */
    public ChatResponse sendChatRequest(ChatRequest request) {
        try {
            log.info("AI 서버 요청: {}", request.getQuestion());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(
                aiServerUrl + "/chat", 
                entity, 
                ChatResponse.class
            );
            
            ChatResponse chatResponse = response.getBody();
            log.info("AI 서버 응답 신뢰도: {}", chatResponse.getConfidence());
            
            return chatResponse;
            
        } catch (RestClientException e) {
            log.error("AI 서버 통신 실패: {}", e.getMessage());
            throw new AIServerException("AI 서버와의 통신에 실패했습니다.", e);
        }
    }
    
    /**
     * AI 서버 헬스체크
     * @return 서버 상태 정보
     */
    public AIHealthResponse checkHealth() {
        try {
            return restTemplate.getForObject(
                aiServerUrl + "/", 
                AIHealthResponse.class
            );
        } catch (RestClientException e) {
            log.warn("AI 서버 헬스체크 실패: {}", e.getMessage());
            return AIHealthResponse.builder()
                .status("DOWN")
                .message("AI 서버에 연결할 수 없습니다.")
                .build();
        }
    }
}

// DTO 클래스들
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String question;
    private Integer contextCount = 8;  // 기본값
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String answer;
    private Double confidence;
    private List<ContextInfo> contexts;
    private String intent;
    private Map<String, Object> metadata;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextInfo {
    private String content;
    private String source;
    private Integer page;
    private Double score;
}
```

### 프론트엔드와의 API 계약
- **요청 형식**: JSON 기반 RESTful API
- **응답 형식**: 구조화된 JSON (answer, confidence, contexts)
- **에러 처리**: HTTP 상태 코드 + 에러 메시지
- **CORS 설정**: 프론트엔드 도메인 허용

## 모니터링 및 로깅

### 로그 레벨 설정
```yaml
logging:
  level:
    com.dongyang.chatbot: INFO
    org.springframework: WARN
    org.hibernate: ERROR
```

### 주요 로그 포인트
- API 요청/응답 로깅
- AI 서버 통신 로깅
- 데이터베이스 쿼리 로깅 (개발 환경)
- 에러 및 예외 상황 로깅

## 트러블슈팅

### 일반적인 문제들

1. **AI 서버 연결 실패**
   ```bash
   # AI 서버 상태 확인
   curl http://ai:8000/
   
   # 네트워크 연결 확인
   docker network ls
   docker network inspect dongyang-chatbot_chatbot-network
   ```

2. **데이터베이스 연결 실패**
   ```bash
   # PostgreSQL 컨테이너 상태 확인
   docker-compose ps postgres
   
   # 데이터베이스 연결 테스트
   docker-compose exec postgres psql -U chatbot_user -d chatbot
   ```

3. **메모리 부족**
   ```bash
   # JVM 힙 메모리 설정
   export JAVA_OPTS="-Xmx1g -Xms512m"
   ```

## 개발 가이드라인

### 코드 스타일
- Google Java Style Guide 준수
- 클래스/메서드 단위 Javadoc 작성
- 단위 테스트 커버리지 80% 이상 유지

### 브랜치 전략
- `main`: 프로덕션 배포 브랜치
- `develop`: 개발 통합 브랜치
- `feature/*`: 기능 개발 브랜치
- `hotfix/*`: 긴급 수정 브랜치

## 라이센스
MIT License

## 기여하기
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
