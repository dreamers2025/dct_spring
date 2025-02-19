package com.dreamers2025.dct.domain.user.dto.request;

import com.dreamers2025.dct.domain.user.dto.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이메일 정보를 입력해 주세요")
    private String email;

    @NotBlank(message = "사용자 이름을 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문과 숫자 조합이어야 합니다")
    private String password;

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .build();
    }

}
