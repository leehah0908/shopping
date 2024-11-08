package com.shopping.orderservice.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
// 클라이언트가 전송한 토큰을 검사하는 필터
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 필터가 해야 할 일을 작성
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 도착 -> 헤더에서 토큰이 꺼내기
        String token = parseBearToken(request);

        try {
            // 토큰 위조 검사 및 인증 처리
            if (token != null) {
                // 토큰 서명 위조 검사 및 토큰을 파싱해서 클레임을 얻어내는 작업
                TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);

                // spring security에 전달할 인가 정보 리스트를 생성 (권한 범위)
                // 권한이 여러개이면 리스트로 권한 체크에 사용할 필드들을 add
                // 나중에 controller에 권한을 파악하기 위해서 미리 저장
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

                // ROLE_USER or ROLE_ADMIN
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole()));

                // 인증 완료 처리
                // spring security에 인증 정보 전달 -> 전역적으로 사용 가능
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userInfo,
                        "", // 인증된 사용자의 비밀번호 -> null 혹은 ""
                        authorityList // 권한 범위
                );

                // spring security 컨테이너에 인증 정보 객체 등록
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
//            else {
//                // 토큰 전달X or Bearer이 아님
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.setContentType("application/json");
//                response.getWriter().write("토큰이 없거나, 유효하지 않은 토큰");
//                return;
//            }

            // doFilter: 필터를 통과하는 메서드 -> 없으면 동작을 멈춤
            filterChain.doFilter(request, response);

        } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write("토큰에 문제가 있음");
        }
    }

    private String parseBearToken(HttpServletRequest request) {
        // 요청 헤더에서 토큰 꺼내오기
        // -- content-type: application/json
        // -- Authorization: Bearer aslkdblk2dnkln34kl52...

        String bearerToken = request.getHeader("Authorization");

        // Bearer를 제거해서 온전한 토큰값 가져오기
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        // null, 빈값, 공백만 있으면 false 전달
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
