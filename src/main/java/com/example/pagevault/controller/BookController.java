package com.example.pagevault.controller;

import com.example.pagevault.model.Book;
import com.example.pagevault.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String books(@RequestParam(name = "q", required = false) String query, Model model) {
        List<Book> books = bookService.searchBooks(query);
        model.addAttribute("books", books);
        model.addAttribute("q", query == null ? "" : query.trim());
        model.addAttribute("resultCount", books.size());
        return "books";
    }
}

