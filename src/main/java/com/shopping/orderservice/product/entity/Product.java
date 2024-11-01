package com.shopping.orderservice.product.entity;

import com.shopping.orderservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
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
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    private String category;

    private Integer price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "image_path")
    private String imagePath;
}
