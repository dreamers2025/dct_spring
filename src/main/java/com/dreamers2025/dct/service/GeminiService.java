package com.dreamers2025.dct.service;

import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import com.dreamers2025.dct.dto.response.ClientGeminiResponse;
import com.dreamers2025.dct.dto.response.GeminiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
public class GeminiService {

    private final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.endpoint}")
    private String apiEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public ClientGeminiResponse getGeminiResponse(DreamInterpretationRequest request) {
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
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 5. 응답 처리
        String responseText = responseEntity.getBody();


        if (responseText != null) {
            // 6. 응답에서 마크다운 제거
            responseText = removeMarkdown(responseText);

            // 7. 응답을 JSON으로 파싱
            try {
                GeminiResponse responseBody = parseJsonResponse(responseText);

                log.info("parsedResponseText : "+ responseText);
                if (responseBody != null && responseBody.getCandidates() != null && !responseBody.getCandidates().isEmpty()) {
                    GeminiResponse.Candidate candidate = responseBody.getCandidates().get(0);
                    if (candidate.getContent() != null && candidate.getContent().getParts() != null) {
                        List<GeminiResponse.Part> parts = candidate.getContent().getParts();
                        log.info(parts.toString());
                        if (!parts.isEmpty()) {
                            String result = parts.get(0).getText();
                            logger.debug("Gemini API 응답: {}", result); // 응답 로깅
                            log.info("JSON : "+ result);

                            ClientGeminiResponse clientResponse = parseStringToCGR(result);;

                            return clientResponse; // 첫 번째 part의 text 반환
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("응답 파싱 오류: {}", e.getMessage());
            }
        }

        logger.error("Gemini API 응답 처리 실패");
//        return "Gemini API 응답 처리 실패"; // 에러 발생 시 메시지 반환
        return null;
    }

    // 마크다운 제거 메소드
    private String removeMarkdown(String responseText) {
        // 마크다운 구문을 제거
        responseText = responseText.replace("```json", "").replace("```", "");
        return responseText;
    }

    // 응답을 JSON으로 파싱
    private GeminiResponse parseJsonResponse(String responseText) throws Exception {
        // JSON 파싱 로직 (예: ObjectMapper 사용)
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseText, GeminiResponse.class);
    }
    private ClientGeminiResponse parseStringToCGR(String result) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        String summary = jsonNode.get("summary").asText();
        String content = jsonNode.get("content").asText();
        ClientGeminiResponse response = new ClientGeminiResponse();
        response.setSummary(summary);
        response.setContent(content);
        return response;
    }

    private String createPrompt(DreamInterpretationRequest request) {
        // Prompt 템플릿
        String promptTemplate = "당신은 해몽가입니다. 당신의 역할은 {interpreterType}입니다. " +
                " 사용자는 현재 {experience}라는 고민을 겪고 있으며, 최근에 {dreamContent}라는 꿈을 꾸었습니다." +
                " 이 꿈을 500자 안에서 {interpreterType}역할에 어울리는 말투로 해몽해 주세요. " +
                " 해몽한 내용은 content라는 key값에 담아주시고," +
                " content를 한줄로 짧게 요약한 내용을 summary라는 key값에 담아서 결과는 JSON 객체로 보내주세요."
                ;

        // Prompt 생성
        String prompt = promptTemplate.replace("{interpreterType}", request.getInterpreterType().toString())
                .replace("{experience}", request.getExperience())
                .replace("{dreamContent}", request.getDreamContent());
        System.out.println("prompt = " + prompt);
        logger.debug("생성된 Prompt: {}", prompt);
        return prompt;
    }
}
