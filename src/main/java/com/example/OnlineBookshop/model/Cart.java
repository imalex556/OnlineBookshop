package com.example.OnlineBookshop.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_items")
    private int totalItems;

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void calculateTotals() {
        this.totalPrice = items.stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
        this.totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void addItem(Book book, int quantity) {
        for (CartItem item : items) {
            if (item.getBook().getBookId().equals(book.getBookId())) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotals();
                return;
            }
        }
        CartItem newItem = new CartItem(this, book, quantity);
        items.add(newItem);
        calculateTotals();
    }

    public void updateItem(Long bookId, int quantity) {
        items.stream()
                .filter(item -> item.getBook().getBookId().equals(bookId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    calculateTotals();
                });
    }

    public void removeItem(Long bookId) {
        items.removeIf(item -> item.getBook().getBookId().equals(bookId));
        calculateTotals();
    }

    public void clear() {
        items.clear();
        totalPrice = 0;
        totalItems = 0;
    }
}