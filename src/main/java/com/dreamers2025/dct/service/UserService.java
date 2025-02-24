package com.dreamers2025.dct.service;

import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.domain.user.dto.request.LoginRequest;
import com.dreamers2025.dct.domain.user.dto.request.SignUpRequest;
import com.dreamers2025.dct.domain.user.dto.response.UpgradeResponse;
import com.dreamers2025.dct.exception.ErrorCode;
import com.dreamers2025.dct.exception.UserException;
import com.dreamers2025.dct.jwt.JwtTokenProvider;
import com.dreamers2025.dct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public void signUp(SignUpRequest signUpRequest){

        //Race Condition 방지용 검증
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(m -> {
                    throw new UserException(ErrorCode.DUPLICATE_EMAIL);
                });

        userRepository.findByUsername(signUpRequest.getUsername())
                .ifPresent(m -> {
                    throw new UserException(ErrorCode.DUPLICATE_USERNAME);
                });

        String rawPassword = signUpRequest.getPassword();
        String encodedPassword =passwordEncoder.encode(rawPassword);

        User newUser = signUpRequest.toEntity();
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

    }

    @Transactional(readOnly = true)
    public Map<String ,Object> authenticate(LoginRequest loginRequest){

        //이메일과 닉네임으로 로그인
        String username = loginRequest.getUsername();

        User founduser = userRepository.findByEmail(username)
                .orElseGet(()-> userRepository.findByUsername(username)
                                .orElseThrow(()-> new UserException((ErrorCode.USER_NOT_FOUND)))
                        );

        String inputPassword = loginRequest.getPassword();
        String storedPassword = founduser.getPassword();

        if(!passwordEncoder.matches(inputPassword,storedPassword)){
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }

        return Map.of(
                "message","로그인에 성공했습니다.",
                "username",founduser.getUsername(),
                "accessToken",jwtTokenProvider.createAccessToken(String.valueOf(founduser.getId()))
        );
    }

    @Transactional(readOnly = true)
    public User findMe(String id){
        User founduser = userRepository.findById(Long.valueOf(id))
                        .orElseThrow(()-> new UserException((ErrorCode.USER_NOT_FOUND))
                );
        return founduser;
    }

    public UpgradeResponse updateUserGrade(String id) {
        userRepository.updateUserGrade(Long.valueOf(id));
        User founduser = userRepository.findById(Long.valueOf(id))
                .orElseThrow(()-> new UserException((ErrorCode.USER_NOT_FOUND))
                );
        return UpgradeResponse.of(founduser);
    }
}
