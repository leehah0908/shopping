package com.shopping.orderservice.ordering.dto.response;

import com.shopping.orderservice.ordering.entity.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResOrderListDto {

    private Long orderId;
    private String userEmail;
    private OrderStatus orderStatus;
    private List<OrderDetailDto> orderDetail;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class OrderDetailDto {
        private Long detailId;
        private String productName;
        private int quantity;
    }
}
