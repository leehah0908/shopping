package com.shopping.orderservice.user.repository;

import com.shopping.orderservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
