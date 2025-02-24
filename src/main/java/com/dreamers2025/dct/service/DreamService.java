package com.dreamers2025.dct.service;

import com.dreamers2025.dct.domain.dream.dto.response.DreamLog;
import com.dreamers2025.dct.domain.dream.entity.Dream;
import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.repository.DreamRepository;
import com.dreamers2025.dct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DreamService {
    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;

    public Dream saveDream(Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Dream dream = new Dream();
        dream.setContent(content);
        dream.setUser(user);

        return dreamRepository.save(dream);
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
            return Long.parseLong(userId);  // String을 Long으로 변환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid userId format");
        }
    }
}