package com.dreamers2025.dct.controller;
import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import com.dreamers2025.dct.dto.response.ClientGeminiResponse;
import com.dreamers2025.dct.service.DreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.dreamers2025.dct.service.GeminiService;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;
    private final DreamService dreamService;

    @GetMapping("/api/gemini/dream-interpretation")
    public ResponseEntity<Map<String,Object>> getDreamInterpretation(
            @ModelAttribute DreamInterpretationRequest request,
            @AuthenticationPrincipal String id
    ) {
        // 1. 해몽 결과 가져오기
        ClientGeminiResponse geminiResponse = geminiService.getGeminiResponse(request); // summary, content
        InterpreterType interpreterType = request.getInterpreterType(); // interpreterType

        // log.info("Gemini Controller userId:", id);

        // 2. 회원대상 DB 저장
        if (id == null || id.trim().isEmpty() || !"anonymousUser".equals(id)) {
            log.info("Gemini Controller userId: {}", id);
            dreamService.saveDream(id, geminiResponse, interpreterType);
        }

        // 3. 응답 반환
        return ResponseEntity.ok(Map.of(
                "gemini", geminiResponse,
                "userGrade", "userGrade"
        ));
    }
}