package com.shopping.orderservice.common.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrfConfig -> csrfConfig.disable());

        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers("/user/signup").permitAll()
                    .anyRequest().authenticated();

        });

        return http.build();
    }
}
