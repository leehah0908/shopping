package com.shopping.orderservice.user.dto.request;

import com.shopping.orderservice.common.entity.Address;

import com.shopping.orderservice.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupReqDto {

    @NotEmpty(message = "이메일 필수")
    private String email;

    @NotEmpty(message = "비밀번호 필수")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상")
    private String password;

    @NotEmpty(message = "닉네임 필수")
    @Size(min = 2, message = "닉네임은 최소 2자 이상")
    private String nickname;

    private Address address;

    public User toEntity(PasswordEncoder passwordEncoder){
        return User.builder()
                .nickname(this.nickname)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .address(this.address)
                .build();
    }

}
