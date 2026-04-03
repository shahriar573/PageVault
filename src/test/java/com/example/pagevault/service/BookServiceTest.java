package com.example.pagevault.service;

import com.example.pagevault.model.Book;
import com.example.pagevault.model.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book saved = bookService.saveBook(book);
        assertNotNull(saved);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> found = bookService.getBookById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAvailableBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book(), new Book());
        when(bookRepository.findByStockQuantityGreaterThanOrderByTitleAsc(0)).thenReturn(books);

        List<Book> result = bookService.getAvailableBooks();

        assertEquals(3, result.size());
        verify(bookRepository).findByStockQuantityGreaterThanOrderByTitleAsc(0);
    }

    @Test
    void testSearchBooksBlankQueryReturnsAvailable() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findByStockQuantityGreaterThanOrderByTitleAsc(0)).thenReturn(books);

        List<Book> result = bookService.searchBooks("   ");

        assertEquals(2, result.size());
        verify(bookRepository).findByStockQuantityGreaterThanOrderByTitleAsc(0);
    }

    @Test
    void testSearchBooksUsesStockAwareLookup() {
        List<Book> books = Arrays.asList(new Book());
        when(bookRepository.findByStockQuantityGreaterThanAndTitleContainingIgnoreCaseOrStockQuantityGreaterThanAndAuthorContainingIgnoreCase(
                0,
                "clean",
                0,
                "clean"
        )).thenReturn(books);

        List<Book> result = bookService.searchBooks(" clean ");

        assertEquals(1, result.size());
        verify(bookRepository)
                .findByStockQuantityGreaterThanAndTitleContainingIgnoreCaseOrStockQuantityGreaterThanAndAuthorContainingIgnoreCase(
                        0,
                        "clean",
                        0,
                        "clean"
                );
    }
}

