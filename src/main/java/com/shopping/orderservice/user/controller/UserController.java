package com.shopping.orderservice.user.controller;

import com.shopping.orderservice.common.auth.JwtTokenProvider;
import com.shopping.orderservice.common.dto.CommonErrorDto;
import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.user.dto.request.UserLoginReqDto;
import com.shopping.orderservice.user.dto.request.UserSignupReqDto;
import com.shopping.orderservice.user.dto.response.UserResDto;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupReqDto dto) {

        User user = userService.userSignup(dto);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입 완료", user.getUserId());
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginReqDto dto) {
        User loginUser = userService.login(dto);

        // 회원정보 일치 -> 로그인 유지를 위해 JWT 생성 후 클라이언트에게 발급
        String token = jwtTokenProvider.createToken(loginUser.getEmail(), loginUser.getRole().toString());

        System.out.println(token);

        // 토큰 외에 추가로 전달할 정보가 있다면 Map을 사용
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user_id", loginUser.getUserId());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "로그인 성공", map);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 일반 회원의 로그인 요청
    @GetMapping("/myinfo")
    public ResponseEntity<?> getMyInfo() {

        UserResDto dto = userService.myInfo();

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "myinfo 조회 성공", dto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
