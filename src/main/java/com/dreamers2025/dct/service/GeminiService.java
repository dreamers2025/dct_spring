package com.dreamers2025.dct.service;

import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import com.dreamers2025.dct.dto.response.ClientGeminiResponse;
import com.dreamers2025.dct.dto.response.GeminiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
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
@ToString
public class GeminiService {

    private final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.endpoint}")
    private String apiEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public ClientGeminiResponse getGeminiResponse(DreamInterpretationRequest request, String userGrade) {
        String prompt = createPrompt(request);
        log.info("Gemini에 보내는 프롬프트 : " + prompt);

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = apiEndpoint + "?key=" + apiKey;
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        int maxRetries = 3; // 최대 3번 재시도
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                String responseText = responseEntity.getBody();

                if (responseText != null) {
                    responseText = removeMarkdown(responseText);
                    GeminiResponse responseBody = parseJsonResponse(responseText);

                    if (responseBody != null && responseBody.getCandidates() != null && !responseBody.getCandidates().isEmpty()) {
                        GeminiResponse.Candidate candidate = responseBody.getCandidates().get(0);
                        if (candidate.getContent() != null && candidate.getContent().getParts() != null) {
                            List<GeminiResponse.Part> parts = candidate.getContent().getParts();
                            if (!parts.isEmpty()) {
                                String result = parts.get(0).getText();
                                logger.debug("Gemini API 응답: {}", result);
                                return parseStringToCGR(result, userGrade);
                            }
                        }
                    }
                }
                logger.error("Gemini API 응답이 비어 있음. 재시도 {}/{}", attempt, maxRetries);
            } catch (Exception e) {
                logger.error("Gemini API 요청 실패 ({}차 시도): {}", attempt, e.getMessage());
            }

            if (attempt < maxRetries) {
                try {
                    Thread.sleep(2000); // 2초 대기 후 재시도
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        logger.error("Gemini API 요청 재시도 후 실패");
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
    private ClientGeminiResponse parseStringToCGR(String result, String userGrade) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        String summary = jsonNode.get("summary").asText();
        String content = jsonNode.get("content").asText();
        ClientGeminiResponse response = new ClientGeminiResponse();
        if(userGrade.equals("premium")||userGrade.equals("free")){
            response.setSummary(summary);
            response.setContent(content);
            log.info("회원 응답 : "+response);
            return response;
        }
        String placeholder = "국회의원과 정부는 법률안을 제출할 수 있다. 연소자의 근로는 특별한 보호를 받는다. 국방상 또는 국민경제상 긴절한 필요로 인하여 법률이 정하는 경우를 제외하고는, 사영기업을 국유 또는 공유로 이전하거나 그 경영을 통제 또는 관리할 수 없다. 국무총리는 국회의 동의를 얻어 대통령이 임명한다. 이 헌법에 의한 최초의 대통령의 임기는 이 헌법시행일로부터 개시한다."
                +"\n\n이 헌법시행 당시의 대법원장과 대법원판사가 아닌 법관은 제1항 단서의 규정에 불구하고 이 헌법에 의하여 임명된 것으로 본다. 제1항의 탄핵소추는 국회재적의원 3분의 1 이상의 발의가 있어야 하며, 그 의결은 국회재적의원 과반수의 찬성이 있어야 한다. 다만, 대통령에 대한 탄핵소추는 국회재적의원 과반수의 발의와 국회재적의원 3분의 2 이상의 찬성이 있어야 한다."
                +"\n\n모든 국민은 법률이 정하는 바에 의하여 납세의 의무를 진다. 대한민국의 주권은 국민에게 있고, 모든 권력은 국민으로부터 나온다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 재의의 요구가 있을 때에는 국회는 재의에 붙이고, 재적의원과반수의 출석과 출석의원 3분의 2 이상의 찬성으로 전과 같은 의결을 하면 그 법률안은 법률로서 확정된다.";
        response.setSummary(summary);
        response.setContent(placeholder);
        log.info("비회원 응답 : "+response);
        return response;
    }

    private String createPrompt(DreamInterpretationRequest request) {
        // Prompt 템플릿
        String promptTemplate = "당신은 해몽가입니다. 당신의 역할은 {interpreterType}입니다. " +
                " 사용자는 현재 {experience}라는 고민을 겪고 있으며, 최근에 {dreamContent}라는 꿈을 꾸었습니다." +
                " 이 꿈을 반드시 500자 안에서 {interpreterType}역할에 어울리는 말투로 해몽해 주세요. " +
                " 해몽한 내용은 content라는 key값에 담아주시고," +
                " content를 반드시 25자 이내로 한줄로 요약한 텍스트를 summary라는 key값에 담아서 결과는 JSON 객체로 보내주세요."+
                " content, summary 외의 다른 내용은 제거해주세요"
                ;

        // Prompt 생성
        String prompt = promptTemplate.replace("{interpreterType}", request.getInterpreterType().name())  // name()을 사용
                .replace("{experience}", request.getExperience())
                .replace("{dreamContent}", request.getDreamContent());
        System.out.println("prompt = " + prompt);
        logger.debug("생성된 Prompt: {}", prompt);
        return prompt;
    }
}
