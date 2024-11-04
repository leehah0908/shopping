package com.shopping.orderservice.ordering.repository;

import com.shopping.orderservice.ordering.entity.Orders;
import com.shopping.orderservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<List<Orders>> findByUser(User user);
}
