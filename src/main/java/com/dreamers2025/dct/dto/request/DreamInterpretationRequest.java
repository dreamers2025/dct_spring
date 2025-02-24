package com.dreamers2025.dct.dto.request;
import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import lombok.Data;

@Data
public class DreamInterpretationRequest {
    private InterpreterType interpreterType; // 해설가 유형 (예: "서양 문화 전문가")
    private String experience;   // 겪은 일 (예: "취업에 어려움")
    private String dreamContent; // 꿈 내용 (예: "뱀이 나무에서 내려오는 꿈")
}