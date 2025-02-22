package com.dreamers2025.dct.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
public class GeminiResponse {

    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

//    @JsonIgnoreProperties(ignoreUnknown = true) // Candidate 클래스에서도 무시하도록 설정
//    public static class Candidate {
//        private Map<String, Object> content;
//
//        public Map<String, Object> getContent() {
//            return content;
//        }
//
//        public void setContent(Map<String, Object> content) {
//            this.content = content;
//        }
//    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private List<Part> parts; // Map이 아닌 명확한 List<Part> 타입 사용

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    // Part 클래스 추가
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text; // `text` 키의 값을 직접 가져옴

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
