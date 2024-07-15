package com.study.event.api.auth.filter;

import com.study.event.api.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 클라이언트가 요청에 포함한 토큰 정보를 검사하는 필터

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 요청 메세지에서 토큰을 파싱
            String token = parseVrearerToken(request);

                log.info("토큰 위조 검사 필터 작동!");
            if(token != null) {
                tokenProvider.validateAndGetTokenInfo(token);
            }

        } catch (Exception e) {
            log.warn("토큰이 위조되었습니다.");
            e.printStackTrace();
        }


        // 필터체인에 내가 만든 커스텀필터를 실행하도록 명령
        // 필터체인 : 여러가지 필터들 사이에 만든걸 넣어줌
        // 실행명령 ( 등록은 SecurityConfig 에서 함)
        filterChain.doFilter(request, response);

    }

    private String parseVrearerToken(HttpServletRequest request) {
        /*
            1. 요청 헤더에서 토큰을 가져오기

            -- request header

            {
                'Authorization' : 'Bearer sajkdhgfjkshdf',
                'Content-type' : 'application/json'
            }
         */

        String bearerToken = request.getHeader("Authorization");

        // 토큰에 붙어있는 Bearer 라는 문자열 제거
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
