package com.dreamers2025.dct.controller;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.dreamers2025.dct.service.GeminiService;


@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/api/gemini/dream-interpretation")
    public String getDreamInterpretation(@ModelAttribute DreamInterpretationRequest request) {
        return geminiService.getGeminiResponse(request);
    }
}