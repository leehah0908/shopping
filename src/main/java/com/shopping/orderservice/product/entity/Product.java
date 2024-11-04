package com.shopping.orderservice.product.entity;

import com.shopping.orderservice.common.entity.BaseTimeEntity;
import com.shopping.orderservice.product.dto.response.ResProductDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String category;

    private int price;

    private int stockQuantity;

    @NotEmpty
    @Setter
    // 이미지 경로 setter는 직접 작성
    private String imagePath;

    public ResProductDto toProductDto() {
        return ResProductDto.builder()
                .productId(productId)
                .productName(productName)
                .category(category)
                .price(price)
                .stockQuantity(stockQuantity)
                .productImage(imagePath)
                .build();
    }

}
