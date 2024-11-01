package com.shopping.orderservice.user.entity;

import com.shopping.orderservice.common.entity.Address;
import com.shopping.orderservice.user.dto.response.UserResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Embedded // @Embeddable로 선언한 값 대입 (기본 생성자 필수 -> @NoArgsConstructor)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Builder.Default // build시 초기화된 값으로 세팅하기 위한 아노테이션
    private Role role = Role.USER;

    public UserResDto toUserResDto() {
        return UserResDto.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .address(address)
                .role(role)
                .build();
    }

}
