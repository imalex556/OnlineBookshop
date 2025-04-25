package com.example.OnlineBookshop.repository;

import com.example.OnlineBookshop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}