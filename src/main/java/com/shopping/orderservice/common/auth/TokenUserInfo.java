package com.shopping.orderservice.common.auth;

import com.shopping.orderservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {
    private String email;
    private Role role;
}
