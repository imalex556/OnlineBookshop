package com.example.OnlineBookshop.repository;

import com.example.OnlineBookshop.model.Order;
import com.example.OnlineBookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}