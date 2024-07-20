package com.study.event.api.config;

import com.study.event.api.auth.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

// 스프링 시큐리티 설정 파일
// 인터셉터, 필터 처리
// 세션검증, 토큰인증
// 권한처리
// OAuth2 - SNS 로그인
@EnableWebSecurity
// 컨트롤러에서 사전, 사후에 권한 정보를 캐치해서 막을건지 설정
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // 비밀번호 암호화 객체 컨테이너에 등록 (스프링에 주입받는 설정)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정 (스프링 부트 2.7 버전 이전 -> 인터페이스를 통해 오버라이딩)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable() // 필터설정 off
                .httpBasic().disable() // 베이직 인증 off
                .formLogin().disable() // 로그인창 off

                // 세션 인증은 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests() // 요청별 인가 설정
                .antMatchers(HttpMethod.DELETE,"/events/*").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.PUT, "auth/promote").hasAuthority("COMMON")
                .antMatchers("/", "/auth/**").permitAll() // 인증(로그인) 없이도 들어갈 수 있는 경로 설정
                // 나머지는 전부 인증(로그인) 후 들어갈 수 있음
                .anyRequest().authenticated(); // 인가 설정 on

        // 토큰 위조 검사 커스텀 필터 필터체인에 연결
        // CorsFilter(Spring) 뒤에 커스텀 필터를 연결
        http.addFilterAfter(jwtAuthFilter, CorsFilter.class);

        return http.build();
    }

}
