package com.example.OnlineBookshop.repository;

import com.example.OnlineBookshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}