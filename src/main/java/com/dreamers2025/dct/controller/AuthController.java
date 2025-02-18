package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.service.UserService;
import com.dreamers2025.dct.domain.user.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
            ){
        log.info("request for signup: {}", signUpRequest.getUsername());
        userService.signUp(signUpRequest);

        return ResponseEntity
                .ok()
                .body(Map.of(
                        "message","회원가입이 완료되었습니다.",
                        "username",signUpRequest.getUsername()
                ));
    }


}
