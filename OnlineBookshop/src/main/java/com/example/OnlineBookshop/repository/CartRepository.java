package com.example.OnlineBookshop.repository;

import com.example.OnlineBookshop.model.Cart;
import com.example.OnlineBookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}