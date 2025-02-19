package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.domain.user.dto.request.LoginRequest;
import com.dreamers2025.dct.service.UserService;
import com.dreamers2025.dct.domain.user.dto.request.SignUpRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/signup")
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

    @PostMapping("login")
    public ResponseEntity<?> login(
        @RequestBody @Valid LoginRequest loginRequest
        , HttpServletResponse response
    ){
        log.info("request for authentication user : {}", loginRequest.getUsername());

        Map<String, Object> responseMap = userService.authenticate(loginRequest);

        Cookie cookie = new Cookie("accessToken",(String)responseMap.get("accessToken"));

        cookie.setMaxAge(60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok().body(responseMap);
    }


}
