package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.Book;
import com.example.OnlineBookshop.model.Review;
import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.repository.BookRepository;
import com.example.OnlineBookshop.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping("/submit/{bookId}")
    public String submitReview(@PathVariable Long bookId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return "redirect:/customer/books";
        }

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);

        return "redirect:/customer/books/details/" + bookId;
    }
}