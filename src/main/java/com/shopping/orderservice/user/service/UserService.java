package com.shopping.orderservice.user.service;

import com.shopping.orderservice.user.dto.request.UserLoginReqDto;
import com.shopping.orderservice.user.dto.request.UserSignupReqDto;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User userSignup(@Valid UserSignupReqDto dto) {

        // controller으로 빼는게 훨씬 나음 -> 중복확인 버튼으로
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        return userRepository.save(dto.toEntity(passwordEncoder));

    }

    public User login(UserLoginReqDto dto) {
        User loginUser = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new EntityNotFoundException("회원 정보 찾을 수 없음"));

        if (!passwordEncoder.matches(dto.getPassword(), loginUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }
        return loginUser;

    }
}
