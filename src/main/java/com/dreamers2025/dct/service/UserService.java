package com.dreamers2025.dct.service;

import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.domain.user.dto.request.LoginRequest;
import com.dreamers2025.dct.domain.user.dto.request.SignUpRequest;
import com.dreamers2025.dct.domain.user.dto.response.DuplicateCheckResponse;
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
        Boolean gradeExpiry = expireGrade(founduser.getId());
        return Map.of(
                "message","로그인에 성공했습니다.",
                "username",founduser.getUsername(),
                "gradeExpire",gradeExpiry,
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

    public DuplicateCheckResponse checkDuplicate(String type, String value) {
        switch (type) {
            case "email":
                // 중복된 경우를 클라이언트에게 알려야 함
                return userRepository.findByEmail(value)
                        .map(m -> DuplicateCheckResponse.unavailable("이미 사용 중인 이메일입니다."))
                        .orElse(DuplicateCheckResponse.available());
            case "username":
                return userRepository.findByUsername(value)
                        .map(m -> DuplicateCheckResponse.unavailable("이미 사용 중인 사용자 이름입니다."))
                        .orElse(DuplicateCheckResponse.available());
            default:
                throw new UserException(ErrorCode.INVALID_SIGNUP_DATA);
        }
    }

    public UpgradeResponse updateUserGrade(String id) {
        userRepository.updateUserGrade(Long.valueOf(id));
        User founduser = userRepository.findById(Long.valueOf(id))
                .orElseThrow(()-> new UserException((ErrorCode.USER_NOT_FOUND))
                );
        return UpgradeResponse.of(founduser);
    }

    public Boolean expireGrade(Long id){
        int expiry = (int)userRepository.expireGrade(id);
        boolean expired =  expiry== 1;
        return expired;
    }

}
