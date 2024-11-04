package com.shopping.orderservice.ordering.repository;

import com.shopping.orderservice.ordering.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
