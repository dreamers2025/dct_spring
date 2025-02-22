package com.dreamers2025.dct.controller;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.dreamers2025.dct.service.GeminiService;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/api/gemini/dream-interpretation")
    public ResponseEntity<Map<String,Object>> getDreamInterpretation(
            @ModelAttribute DreamInterpretationRequest request,
            @AuthenticationPrincipal String id
    ) {
        return ResponseEntity
                .ok()
                .body(Map.of(
                        "gemini",geminiService.getGeminiResponse(request),
                        "userGrade","userGrade"
                ));
    }
}