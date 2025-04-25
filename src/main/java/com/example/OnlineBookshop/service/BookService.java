
package com.example.OnlineBookshop.service;

import com.example.OnlineBookshop.model.Book;
import com.example.OnlineBookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static class SortByTitleAsc implements SortStrategy {
        @Override
        public List<Book> sort(List<Book> books) {
            books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
            return books;
        }
    }

    public static class SortByTitleDesc implements SortStrategy {
        @Override
        public List<Book> sort(List<Book> books) {
            books.sort((b1, b2) -> b2.getTitle().compareToIgnoreCase(b1.getTitle()));
            return books;
        }
    }

    public static class SortByPriceAsc implements SortStrategy {
        @Override
        public List<Book> sort(List<Book> books) {
            books.sort((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()));
            return books;
        }
    }

    public static class SortByPriceDesc implements SortStrategy {
        @Override
        public List<Book> sort(List<Book> books) {
            books.sort((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()));
            return books;
        }
    }

    public SortStrategy getSortStrategy(String sortBy, String sortDir) {
        if ("title".equalsIgnoreCase(sortBy)) {
            return "asc".equalsIgnoreCase(sortDir) ? new SortByTitleAsc() : new SortByTitleDesc();
        } else if ("price".equalsIgnoreCase(sortBy)) {
            return "asc".equalsIgnoreCase(sortDir) ? new SortByPriceAsc() : new SortByPriceDesc();
        }
        return new SortByTitleAsc();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryContainingIgnoreCase(category);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateStock(Long id, int quantity) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setStockQuantity(book.getStockQuantity() + quantity);
            return bookRepository.save(book);
        }
        return null;
    }
}
