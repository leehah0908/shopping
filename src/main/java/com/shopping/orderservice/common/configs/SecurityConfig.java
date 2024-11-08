package com.shopping.orderservice.common.configs;

import com.shopping.orderservice.common.auth.JwtAuthFilter;
import com.shopping.orderservice.common.dto.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrfConfig -> csrfConfig.disable())
                .cors(Customizer.withDefaults()) // 직접 커스텀한 CORS 설정을 적용

                // 더 이상 세션으로 관리 상태를 유지하지 않겠다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> {
                    auth
                            //                    .requestMatchers("/user/list").hasAnyRole("ADMIN")
                            .requestMatchers(
                                    "/user/signup",
                                    "/user/login",
                                    "/user/refresh",
                                    "product/list"
                            )
                            .permitAll()
                            .anyRequest().authenticated();

                })
                // 필터 등록 (requestMatchers에 지정된 요청 이외의 모든 요청은 jwtAuthFilter를 통과해야 함)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                    // 인증 과정에서 예외가 발생한 경우 그 예외를 핸들링 할 객체를 등록 (ex. 로그인 안한 유저가 myPage 요청)
                    exception.authenticationEntryPoint(customAuthenticationEntryPoint);
                });

        return http.build();
    }
}
