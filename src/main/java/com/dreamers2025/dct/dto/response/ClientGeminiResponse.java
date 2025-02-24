package com.dreamers2025.dct.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientGeminiResponse {
    private String summary;
    private String content;
}
