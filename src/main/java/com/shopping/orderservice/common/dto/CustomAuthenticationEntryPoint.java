package com.shopping.orderservice.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");

        // map 생성 및 데이터 추가
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "NO_LOGIN");
        responseMap.put("code", "401");

        // Map을 JSON으로 변환해주는 라이브러리
        String jsonString = new ObjectMapper().writeValueAsString(responseMap);

        // json 데이터를 응답 객체에 실어서 클라이언트에 응답
        response.getWriter().write(jsonString);

    }
}
