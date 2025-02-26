package com.study.event.api.event.dto.response;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String email;
    private String role; // 권한정보

    private String token; // 인증 토큰

}
