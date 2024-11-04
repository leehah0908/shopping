package com.shopping.orderservice.product.dto.request;

import com.shopping.orderservice.product.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqProductSaveDto {

    @NotEmpty
    private String productName;

    @NotEmpty
    private String category;

    private int price;

    private int stockQuantity;

    private MultipartFile productImage;

    public Product toProduct() {
        return Product.builder()
                .productName(productName)
                .category(category)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();

    }
}
