package com.dreamers2025.dct.service;

import com.dreamers2025.dct.controller.DreamController;
import com.dreamers2025.dct.domain.dream.dto.request.DreamCreate;
import com.dreamers2025.dct.domain.dream.dto.response.DreamLog;
import com.dreamers2025.dct.domain.dream.entity.Dream;
import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.dto.request.DreamInterpretationRequest;
import com.dreamers2025.dct.dto.response.ClientGeminiResponse;
import com.dreamers2025.dct.dto.response.GeminiResponse;
import com.dreamers2025.dct.repository.DreamRepository;
import com.dreamers2025.dct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamService {
    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;

    public Dream saveDream(String userId, ClientGeminiResponse geminiResponse, InterpreterType interpreterType) {
        Long userIdLong = convertToLong(userId);
        User user = userRepository.findById(userIdLong)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Dream newDream = Dream.builder()
                .summary(geminiResponse.getSummary())
                .content(geminiResponse.getContent())
                .interpreter(interpreterType)
                .user(user)
                .build();

        return dreamRepository.save(newDream);
    }

    public List<DreamLog> getDreamsByUserId(String userId) {
        Long userIdLong = convertToLong(userId);
        User user = userRepository.findById(userIdLong)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Dream> dreamLogs = dreamRepository.findByUser(user); // 꿈 기록 가져오기
        // Dream 엔티티를 DreamLog DTO로 변환
        List<DreamLog> dreamlogs = dreamLogs.stream()
                .map(DreamLog::from)
                .collect(Collectors.toList());
        return dreamlogs;
    }

    private Long convertToLong(String userId) {
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid userId format");
        }
    }
    public void deleteDream(Long dreamId){
        dreamRepository.deleteById(dreamId);
        return;
    }

}