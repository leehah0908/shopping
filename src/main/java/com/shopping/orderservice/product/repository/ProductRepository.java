package com.shopping.orderservice.product.repository;

import com.shopping.orderservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
