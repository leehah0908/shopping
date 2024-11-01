package com.shopping.orderservice.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Slf4j
// 토큰 발급 및 서명 위조 검사를 하는 클래스
public class JwtTokenProvider {

    // 서명에 사용할 값 (512비트 이상의 랜덤 문자열을 권장)
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    public String createToken(String email, String role) {
         /*
            {
                "iss": "서비스 이름(발급자)",
                "exp": "2023-12-27(만료일자)",
                "iat": "2023-11-27(발급일자)",
                "email": "로그인한 사람 이메일",
                "role": "Premium"
                ...
                == 서명
            }
         */

        // payload에 들어갈 정보
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 100L * 100L)) // 현재 시간 밀리초에 30분을 더해서 세팅
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


    }

}
