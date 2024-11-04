package com.shopping.orderservice.ordering.entity;

import com.shopping.orderservice.ordering.dto.response.ResOrderListDto;
import com.shopping.orderservice.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    // user 정보 얻을 때만 조인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Setter
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    public ResOrderListDto toResOrderListDto() {
        List<OrderDetail> orderDetails = this.getOrderDetails();
        List<ResOrderListDto.OrderDetailDto> orderDetailDtos = new ArrayList<>();

        // orderDetail -> orderDetailDtos
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailDtos.add(orderDetail.toOrderDetailDto());
        }

        return ResOrderListDto.builder()
                .orderId(orderId)
                .userEmail(user.getEmail())
                .orderStatus(orderStatus)
                .orderDetail(orderDetailDtos)
                .build();
    }
}
