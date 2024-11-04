package com.shopping.orderservice.user.controller;

import com.shopping.orderservice.common.auth.JwtTokenProvider;
import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.user.dto.request.UserLoginReqDto;
import com.shopping.orderservice.user.dto.request.UserSignupReqDto;
import com.shopping.orderservice.user.dto.response.UserResDto;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 토큰 외에 추가로 전달할 정보가 있다면 Map을 사용
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user_id", loginUser.getUserId());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "로그인 성공", map);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 관리자 회원 정보 요청
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    // controller 파리미터로 pageable을 받으면, 페이징 파라미터 처리를 쉽게 처리할 수 있음
    // 클라이언트에서는 /list?number=1&size=10&sort=name,desc -> 형태로 데이터 보내기
    // 요청시 쿼리스트링이 전돨되지 않으면 기본값 0, 20, unsorted로 설정
    public ResponseEntity<?> userList(Pageable pageable) {

        List<UserResDto> users = userService.userList(pageable);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "회원 정보 조회 성공", users);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 일반 회원의 회원 정보 요청
    @GetMapping("/myinfo")
    public ResponseEntity<?> getMyInfo() {

        UserResDto dto = userService.myInfo();

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "myinfo 조회 성공", dto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
