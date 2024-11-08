package com.shopping.orderservice.product.repository;

import com.shopping.orderservice.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 더미 데이터 추가")
    void bulkInsert() {
        for (int i = 1; i < 101; i++) {
            Product p = Product.builder()
                    .productName("productName " + i)
                    .price(i * 1000)
                    .category("dummy " + 1)
                    .stockQuantity(i)
                    .imagePath("imgpath" + i)
                    .build();
            productRepository.save(p);
        }
    }

}