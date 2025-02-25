package com.dreamers2025.dct.domain.dream.dto.request;

import com.dreamers2025.dct.domain.dream.entity.Dream;
import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record DreamCreate(
        String summary,
        String content,
        String interpreter
) {
    // DTO를 엔터티로 변경하는 편의 메서드
    public Dream toEntity() {
        InterpreterType interpreterType = InterpreterType.valueOf(this.interpreter.toUpperCase());

        return Dream.builder()
                .summary(this.summary)
                .content(this.content)
                .interpreter(interpreterType)
                .build();
    }
}