package com.example.OnlineBookshop.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
    private String publisher;
    
    @Column(nullable = false)
    private double price;
    
    @Column(nullable = false)
    private String category;
    
    @Column(unique = true, nullable = false)
    private String isbn;
    
    @Column(nullable = false)
    private int stockQuantity;
    
    private String imageUrl;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private int pages;
    
    @Column(nullable = false)
    private String language;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Constructors
    public Book() {
    }

    public Book(String title, String author, String publisher, double price, 
                String category, String isbn, int stockQuantity, String description, 
                int pages, String language) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.category = category;
        this.isbn = isbn;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.pages = pages;
        this.language = language;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Helper methods for reviews
    public void addReview(Review review) {
        reviews.add(review);
        review.setBook(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setBook(null);
    }

    // toString() method
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", isbn='" + isbn + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", pages=" + pages +
                ", language='" + language + '\'' +
                '}';
    }
}