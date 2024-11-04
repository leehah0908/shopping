package com.shopping.orderservice.ordering.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReqOrderSaveDto {

    // 사용자는 토큰에서 가지고 올 수 있기 때문에 유저 정보는 받아올 필요가 없음.
    private Long productId;
    private int quantity;
}
