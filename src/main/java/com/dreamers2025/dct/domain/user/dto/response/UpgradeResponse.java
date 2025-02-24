package com.dreamers2025.dct.domain.user.dto.response;

import com.dreamers2025.dct.domain.user.dto.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpgradeResponse {
    private Long id;
    private String email;
    private String username;
    private String usergrade;
    private LocalDateTime gradeExpiry;

    public static UpgradeResponse of(User user){
        return UpgradeResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .usergrade(user.getUsergrade())
                .gradeExpiry(user.getGradeExpiry())
                .build();
    }
}
