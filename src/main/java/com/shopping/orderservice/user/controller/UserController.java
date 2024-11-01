package com.shopping.orderservice.user.controller;

import com.shopping.orderservice.common.dto.CommonResDto;
import com.shopping.orderservice.user.dto.UserLoginReqDto;
import com.shopping.orderservice.user.dto.request.UserSignupReqDto;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupReqDto dto) {

        User user = userService.userSignup(dto);

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "회원가입 완료", user.getUserId());
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginReqDto dto) {
        User loginUser = userService.login(dto);

        return null;
    }
}
