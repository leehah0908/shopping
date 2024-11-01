package com.shopping.orderservice.common.auth;

import com.shopping.orderservice.user.entity.Role;
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

    // 서명 위조 검사
    public TokenUserInfo validateAndGetTokenUserInfo(String token) throws Exception {
        /**
         * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조 여부를 확인
         * 토큰을 json으로 파싱해서 클레임(토큰 정보)을 리턴
         *
         * @param token - 필터가 전달해 준 토큰
         * @return - 토큰 안에 있는 인증된 유저 정보를 반환
         */

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return TokenUserInfo.builder()
                .email(claims.getSubject())
                // 클레임이 get할 수 있는 타입이 정해져 있기 때문에 Role을 꺼내지 못함
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }
}
