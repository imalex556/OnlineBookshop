package com.example.OnlineBookshop.service;

import com.example.OnlineBookshop.model.Book;
import java.util.List;

public interface SortStrategy {
    List<Book> sort(List<Book> books);
}