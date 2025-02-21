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

    @JsonIgnoreProperties(ignoreUnknown = true) // Candidate 클래스에서도 무시하도록 설정
    public static class Candidate {
        private Map<String, Object> content;

        public Map<String, Object> getContent() {
            return content;
        }

        public void setContent(Map<String, Object> content) {
            this.content = content;
        }
    }
}
