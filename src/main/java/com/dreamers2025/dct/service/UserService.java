package com.dreamers2025.dct.service;

import com.dreamers2025.dct.domain.user.entity.User;
import com.dreamers2025.dct.domain.user.request.SignUpRequest;
import com.dreamers2025.dct.exception.ErrorCode;
import com.dreamers2025.dct.exception.UserException;
import com.dreamers2025.dct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

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
}
