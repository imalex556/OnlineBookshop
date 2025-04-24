package com.example.OnlineBookshop.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice;
    private int totalItems;

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    private void calculateTotals() {
        totalPrice = items.stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
        totalItems = items.stream()
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
        items.add(new CartItem(book, quantity));
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