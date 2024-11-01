package com.shopping.orderservice.common.configs;

import com.shopping.orderservice.common.auth.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 권한 검사를 컨트롤러의 메서드에서 전역적으로 수행하기 위한 설정
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrfConfig -> csrfConfig.disable());

        // 더 이상 세션으로 관리 상태를 유지하지 않겠다.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> {
            auth
//                    .requestMatchers("/user/list").hasAnyRole("ADMIN")
                    .requestMatchers("/user/signup", "/user/login").permitAll()
                    .anyRequest().authenticated();

            // 필터 등록 (requestMatchers에 지정된 요청 이외의 모든 요청은 jwtAuthFilter를 통과해야 함)
        }).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
