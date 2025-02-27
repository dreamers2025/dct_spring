package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import com.dreamers2025.dct.dto.response.ClientGeminiResponse;
import com.dreamers2025.dct.service.DreamService;
import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import com.dreamers2025.dct.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
    private final UserService userService;

    @GetMapping("/api/gemini/dream-interpretation")
    public ResponseEntity<Map<String,Object>> getDreamInterpretation(
            @ModelAttribute DreamInterpretationRequest request,
            @AuthenticationPrincipal String id
    ) {
        log.info("/api/gemini/dream-interpretation에서 받은 id : "+id);
        String userGrade ="unknown";
        if(!id.equals("anonymousUser")) {
            userGrade = userService.findMe(id).getUsergrade();
            log.info("유저등급 : "+userGrade);

            // 1. 해몽 결과 가져오기
            ClientGeminiResponse geminiResponse = geminiService.getGeminiResponse(request, userGrade); // summary, content
            InterpreterType interpreterType = request.getInterpreterType(); // interpreterType
            log.info("해몽 결과 가져오기"+geminiService.toString());
            // 2. 회원대상 DB 저장
            dreamService.saveDream(id, geminiResponse, interpreterType);
        }

        return ResponseEntity
                .ok()
                .body(Map.of(
                        "gemini",geminiService.getGeminiResponse(request ,userGrade),
                        "userGrade",userGrade
                ));
    }
}