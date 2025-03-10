package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.domain.user.dto.entity.User;
import com.dreamers2025.dct.domain.user.dto.request.LoginRequest;
import com.dreamers2025.dct.domain.user.dto.response.DuplicateCheckResponse;
import com.dreamers2025.dct.domain.user.dto.response.UpgradeResponse;
import com.dreamers2025.dct.jwt.JwtTokenProvider;
import com.dreamers2025.dct.service.UserService;
import com.dreamers2025.dct.domain.user.dto.request.SignUpRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok().body(Map.of(
                "message", "로그아웃이 처리되었습니다."
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser (
            @AuthenticationPrincipal String id
    ){
        if(id.equals("anonymousUser")){
            return ResponseEntity.status(401).build();
        }
        User founduser = userService.findMe(id);
        return ResponseEntity.ok().body(Map.of(
                "message","인증되었습니다",
                "id",founduser.getId(),
                "email",founduser.getEmail(),
                "username",founduser.getUsername(),
                "created_at",founduser.getCreatedAt(),
                "usergrade", founduser.getUsergrade()
        ));
    }

    @PutMapping("/upgrade")
    public ResponseEntity<UpgradeResponse> upgrade(
            @AuthenticationPrincipal String id
    ){
        log.info("업그레이드할 아이디 : "+id);
        UpgradeResponse response=  userService.updateUserGrade(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/check-duplicate")
    public ResponseEntity<DuplicateCheckResponse> checkDuplicate(
            @RequestParam String type,
            @RequestParam String value
    ) {
        log.info("check duplicate type: {}, value: {}", type, value);

        DuplicateCheckResponse responseDto = userService.checkDuplicate(type, value);

        return ResponseEntity.ok().body(responseDto);
    }
    @PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(
            @RequestParam String newname,
            @AuthenticationPrincipal String id
    ){
        log.info("전달받은 닉네임 : "+newname);
        log.info("전달받은 ID : "+id);
        Boolean isUpdated = userService.updateUsername(id,newname);
        if(!isUpdated){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().body(Map.of(
                "update", "success",
                "message","닉네임 변경 완료",
                "username",newname
        ));
    }
}
