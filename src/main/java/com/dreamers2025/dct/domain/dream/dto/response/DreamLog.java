package com.dreamers2025.dct.domain.dream.dto.response;

import com.dreamers2025.dct.domain.dream.entity.Dream;
import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DreamLog {

    private Long dreamId;
    private String summary;   // 꿈 요약
    private String content;   // 해몽 내용
    private LocalDate date;   // 꿈 기록일
    private InterpreterType interpreter; // 해몽가 유형

    public static DreamLog from(Dream dream) {
        return DreamLog.builder()
                .dreamId(dream.getId())
                .summary(dream.getSummary())
                .content(dream.getContent())
                .date(dream.getDate())
                .interpreter(dream.getInterpreter())
                .build();
    }
}