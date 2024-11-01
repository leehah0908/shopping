package com.shopping.orderservice.user.dto.response;

import com.shopping.orderservice.common.entity.Address;
import com.shopping.orderservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResDto {

    private Long userId;
    private String nickname;
    private String email;
    private Role role;
    private Address address;

}