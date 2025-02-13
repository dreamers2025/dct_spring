package com.dreamers2025.dct.service;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeminiService {

    private final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.endpoint}")
    private String apiEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getGeminiResponse(DreamInterpretationRequest request) {
        // 1. Prompt 생성
        String prompt = createPrompt(request);

        // 2. Request Body 생성
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt))); // prompt를 "parts"에 text로 전달
        requestBody.put("contents", List.of(content));

        // 3. Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. API 호출
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String url = apiEndpoint + "?key=" + apiKey; // API 키를 URL 파라미터로 전달
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);

        // 5. 응답 처리
        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.containsKey("candidates")) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (!candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                if (candidate.containsKey("content")) {
                    Map<String, Object> contentResponse = (Map<String, Object>) candidate.get("content");
                    if (contentResponse.containsKey("parts")) {
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) contentResponse.get("parts");
                        if (!parts.isEmpty()) {
                            String result = (String) parts.get(0).get("text");
                            logger.debug("Gemini API 응답: {}", result); // 응답 로깅
                            return result; // 첫 번째 part의 text 반환
                        }
                    }
                }
            }
        }

        logger.error("Gemini API 응답 처리 실패");
        return "Gemini API 응답 처리 실패"; // 에러 발생 시 메시지 반환
    }

    private String createPrompt(DreamInterpretationRequest request) {
        // Prompt 템플릿
        String promptTemplate = "당신은 {interpreterType}입니다. 사용자는 현재 {experience} 어려움을 겪고 있으며, {dreamContent} 꿈을 꾸었습니다. 이 꿈을 해몽해주시고 대화하듯이 500자정도 만들어서 주세요, 1점에서 10점 사이의 점수를 부여해주세요.\n\n해몽 결과 (해석 + 점수):";

        // Prompt 생성
        String prompt = promptTemplate.replace("{interpreterType}", request.getInterpreterType())
                .replace("{experience}", request.getExperience())
                .replace("{dreamContent}", request.getDreamContent());

        logger.debug("생성된 Prompt: {}", prompt);
        return prompt;
    }
}