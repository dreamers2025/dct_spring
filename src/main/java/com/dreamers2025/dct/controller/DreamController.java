package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.domain.dream.dto.request.DreamCreate;
import com.dreamers2025.dct.domain.dream.dto.response.DreamLog;
import com.dreamers2025.dct.domain.dream.entity.Dream;
import com.dreamers2025.dct.jwt.JwtTokenProvider;
import com.dreamers2025.dct.repository.DreamRepository;
import com.dreamers2025.dct.service.DreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DreamController {
    private final DreamService dreamService;
    private final JwtTokenProvider jwtTokenProvider;

    // 내 꿈 기록 조회 (로그인 상태에서만 가능)
    @GetMapping("/mydreams")
    public ResponseEntity<List<DreamLog>> getMyDreams(@RequestHeader("Authorization") String token) {

        log.info("토큰: {}", token);
        // 토큰에서 'Bearer ' 부분 제거
        String accessToken = token.replace("Bearer ", "");

        log.info("컨트롤러에서 Bear 제거한 토큰: {}", accessToken);

        // JWT에서 사용자 id 정보 추출 getCurrentLoginUsername 메서드명 변경 예정이라고 함
        String userId = jwtTokenProvider.getCurrentLoginUserId(accessToken);

        log.info("조회된 회원 id: {}", userId);

        // 서비스 계층에서 꿈 기록 조회
        List<DreamLog> dreamLogs = dreamService.getDreamsByUserId(userId);

        dreamLogs.stream().forEach(dream -> {log.info(dream.getSummary());});

        // 결과 반환
        return ResponseEntity.ok(dreamLogs);
    }

    @DeleteMapping("/mydreams/delete")
    public ResponseEntity<?> deleteDream(
            @RequestParam Long dreamId,
            @AuthenticationPrincipal String id
    ){
        dreamService.deleteDream(dreamId);
        return ResponseEntity.ok().body(Map.of(
                "message","삭제성공"
        ));
    }
}
