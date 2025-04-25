package com.example.OnlineBookshop.repository;

import com.example.OnlineBookshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}