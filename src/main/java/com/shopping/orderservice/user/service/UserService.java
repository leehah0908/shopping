package com.shopping.orderservice.user.service;

import com.shopping.orderservice.common.auth.TokenUserInfo;
import com.shopping.orderservice.user.dto.request.UserLoginReqDto;
import com.shopping.orderservice.user.dto.request.UserSignupReqDto;
import com.shopping.orderservice.user.dto.response.UserResDto;
import com.shopping.orderservice.user.entity.User;
import com.shopping.orderservice.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserResDto myInfo() {
        TokenUserInfo userInfo =
                // 필터에서 세팅한 토큰 정보를 불러오는 메서드
                (TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다.")
        );

        return user.toUserResDto();
    }

    public List<UserResDto> userList(Pageable pageable) {
        // 페이징 처리 -> 1 페이지 요청, 한 화면에 보여줄 회원 수 : 6명
//        Pageable page = PageRequest.of(0, 6);
//        Page<User> pageList = userRepository.findAll(page);

        // pageable 파라미터 사용(JPA 기능)
        Page<User> pageList = userRepository.findAll(pageable);

        List<User> userList = pageList.getContent();

        // UserResDto 리스트로 리턴
        return userList.stream()
                .map(User::toUserResDto)
                .collect(Collectors.toList());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
    }
}
