package com.example.OnlineBookshop.controller;

import com.example.OnlineBookshop.model.Book;
import com.example.OnlineBookshop.service.BookService;
import com.example.OnlineBookshop.service.SortStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

    private final BookService bookService;

    @Autowired
    public CustomerDashboardController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/dashboard")
    public String customerDashboard(Model model) {
        return "customer-dashboard";
    }

    @GetMapping("/books")
    public String customerBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        List<Book> books;
        if (query != null && !query.isEmpty()) {
            books = bookService.searchBooks(query);
        } else if (category != null && !category.isEmpty()) {
            books = bookService.getBooksByCategory(category);
        } else {
            books = bookService.getAllBooks();
        }
        
        books = bookService.getSortStrategy(sortBy, sortDir).sort(books);
        
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        model.addAttribute("category", category);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "customer-book-list";
    }

    @GetMapping("/books/details/{id}")
    public String showBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/customer/books";
        }
        model.addAttribute("book", book);
        return "customer-book-details";
    }
}