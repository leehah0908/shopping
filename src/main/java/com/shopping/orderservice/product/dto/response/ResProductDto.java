package com.shopping.orderservice.product.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResProductDto {

    private Long productId;
    private String productName;
    private String category;
    private int price;
    private int stockQuantity;
    private String productImage;

}
