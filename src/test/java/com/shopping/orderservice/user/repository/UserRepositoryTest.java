package com.shopping.orderservice.user.repository;

import com.shopping.orderservice.user.dto.response.UserResDto;
import com.shopping.orderservice.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("페이징 처리 -> 1 페이지 요청, 한 화면에 보여줄 회원 수 : 6명")
    void allPageTest() {

        // given
        int pageNo = 1;
        int pageSize = 6;

        // when
        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<User> pageList = userRepository.findAll(page);
        List<User> userList = pageList.getContent();

        // UserResDto 리스트로 리턴
        List<UserResDto> result = userList.stream()
                .map(User::toUserResDto)
                .collect(Collectors.toList());

        // 총 페이지 수
        int totalPages = pageList.getTotalPages();

        // 총 힉생 수
        long totalElements = pageList.getTotalElements();

        // 다음 페이지, 이전 페이지 여부
        boolean next = pageList.hasNext();
        boolean prev = pageList.hasPrevious();

        // then
        System.out.println("총 페이지 수: " + totalPages);
        System.out.println("총 회원 수: " + totalElements);
        System.out.println("다음 페이지 여부: " + next);
        System.out.println("다음 페이지 여부: " + prev);
        result.forEach(System.out::println);
        // then
    }

}