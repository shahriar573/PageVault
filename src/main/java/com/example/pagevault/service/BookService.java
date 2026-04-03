package com.example.pagevault.service;

import com.example.pagevault.model.Book;
import com.example.pagevault.model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByStockQuantityGreaterThanOrderByTitleAsc(0);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.isBlank()) {
            return getAvailableBooks();
        }
        String normalized = query.trim();
        return bookRepository
                .findByStockQuantityGreaterThanAndTitleContainingIgnoreCaseOrStockQuantityGreaterThanAndAuthorContainingIgnoreCase(
                        0,
                        normalized,
                        0,
                        normalized
                );
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

