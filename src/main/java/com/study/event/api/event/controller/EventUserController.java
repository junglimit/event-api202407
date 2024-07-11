package com.study.event.api.event.controller;

import com.study.event.api.event.dto.request.EventUserSaveDto;
import com.study.event.api.event.service.EventUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // react 사용하면 무조건 RestController 임
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class EventUserController {

    private final EventUserService eventUserService;

    // 이메일 중복확인 API
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(String email) {
        boolean isDuplicate = eventUserService.checkEmailDuplicate(email);

        return ResponseEntity.ok().body(isDuplicate);
    }

    // 인증 코드 검증 API
    @GetMapping("/code")
    public ResponseEntity<?> verifyCode(String email, String code) {
        log.info ("{}'s verify code is [ {} ], email, code");

        boolean isMatch = eventUserService.isMatchCode(email,code);

        return ResponseEntity.ok().body(isMatch);
    }

    // 회원가입 마무리 처리
    @PostMapping("/join")
    public ResponseEntity<?> join (@RequestBody EventUserSaveDto dto) {

        log.info("save User Info - {", dto);
        try {
            eventUserService.confirmSignUp(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Service 에서 Throw 한거 여기서 캐치하기
        }

        return ResponseEntity.ok().body("saved success");
    }


}
