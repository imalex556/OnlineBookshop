package com.example.OnlineBookshop.service;

import com.example.OnlineBookshop.model.Book;
import com.example.OnlineBookshop.model.Cart;
import com.example.OnlineBookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class CartService {
    private Cart cart = new Cart();
    private final BookRepository bookRepository;

    @Autowired
    public CartService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Cart getCart() {
        return cart;
    }

    public void addToCart(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null && book.getStockQuantity() >= quantity) {
            cart.addItem(book, quantity);
        }
    }

    public void updateCartItem(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null && quantity > 0) {
            cart.updateItem(bookId, quantity);
        } else if (quantity <= 0) {
            removeFromCart(bookId);
        }
    }

    public void removeFromCart(Long bookId) {
        cart.removeItem(bookId);
    }

    public void clearCart() {
        cart.clear();
    }

    public int getCartItemCount() {
        return cart.getTotalItems();
    }
}