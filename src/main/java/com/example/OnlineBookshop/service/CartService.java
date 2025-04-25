package com.example.OnlineBookshop.service;

import com.example.OnlineBookshop.model.Book;
import com.example.OnlineBookshop.model.Cart;
import com.example.OnlineBookshop.model.User;
import com.example.OnlineBookshop.repository.BookRepository;
import com.example.OnlineBookshop.repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CartService(CartRepository cartRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    public Cart getCart(HttpSession session) {
        User user = getCurrentUser(session);
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    public void addToCart(Long bookId, int quantity, HttpSession session) {
        User user = getCurrentUser(session);
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStockQuantity() >= quantity) {
            cart.addItem(book, quantity);
            cartRepository.save(cart);
        }
    }

    public void updateCartItem(Long bookId, int quantity, HttpSession session) {
        User user = getCurrentUser(session);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (quantity > 0) {
            cart.updateItem(bookId, quantity);
            cartRepository.save(cart);
        } else {
            removeFromCart(bookId, session);
        }
    }

    public void removeFromCart(Long bookId, HttpSession session) {
        User user = getCurrentUser(session);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.removeItem(bookId);
        cartRepository.save(cart);
    }

    public void clearCart(HttpSession session) {
        User user = getCurrentUser(session);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.clear();
        cartRepository.save(cart);
    }

    public int getCartItemCount(HttpSession session) {
        User user = getCurrentUser(session);
        return cartRepository.findByUser(user)
                .map(cart -> cart.getTotalItems())
                .orElse(0);
    }

    private User getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        return user;
    }
}