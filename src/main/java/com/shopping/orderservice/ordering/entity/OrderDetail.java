package com.shopping.orderservice.ordering.entity;

import com.shopping.orderservice.ordering.dto.response.ResOrderListDto;
import com.shopping.orderservice.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    public ResOrderListDto.OrderDetailDto toOrderDetailDto() {
        return ResOrderListDto.OrderDetailDto.builder()
                .detailId(detailId)
                .productName(product.getProductName())
                .quantity(quantity)
                .build();
    }
}
