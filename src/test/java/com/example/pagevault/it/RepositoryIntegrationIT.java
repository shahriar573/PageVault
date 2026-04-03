package com.example.pagevault.it;

import com.example.pagevault.model.Book;
import com.example.pagevault.model.BookRepository;
import com.example.pagevault.model.User;
import com.example.pagevault.model.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class RepositoryIntegrationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("pagevault_db")
            .withUsername("pagevault_user")
            .withPassword("strong_password");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "never");
        registry.add("spring.docker.compose.enabled", () -> "false");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void canPersistAndQueryUserAndBook() {
        User user = new User();
        user.setUsername("it_user");
        user.setEmail("it_user@example.com");
        user.setPassword("encoded");
        user.setRole(User.Role.USER);
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setPrice(499.0);
        book.setStockQuantity(10);
        book = bookRepository.save(book);

        User foundUser = userRepository.findByUsername("it_user");
        assertNotNull(foundUser);
        assertEquals("it_user@example.com", foundUser.getEmail());

        assertFalse(bookRepository.findAll().isEmpty());
        assertEquals("Clean Code", bookRepository.findAll().get(0).getTitle());
    }
}

