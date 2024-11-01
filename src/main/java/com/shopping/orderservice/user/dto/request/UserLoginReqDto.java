package com.shopping.orderservice.user.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginReqDto {
    private String email;
    private String password;
}
