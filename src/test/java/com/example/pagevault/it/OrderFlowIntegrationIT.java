package com.example.pagevault.it;

import com.example.pagevault.model.Book;
import com.example.pagevault.model.BookRepository;
import com.example.pagevault.model.Purchase;
import com.example.pagevault.model.PurchaseRepository;
import com.example.pagevault.model.User;
import com.example.pagevault.model.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class OrderFlowIntegrationIT {

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
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Test
    @WithMockUser(username = "buyer", roles = {"USER"})
    void createOrder_createsPurchaseAndReducesStock() throws Exception {
        // Arrange: create DB user and book
        User user = new User();
        user.setUsername("buyer");
        user.setEmail("buyer@example.com");
        user.setPassword("encoded");
        user.setRole(User.Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPrice(650.0);
        book.setStockQuantity(5);
        book = bookRepository.save(book);

        // Act + Assert: submit order form
        mockMvc.perform(post("/orders/new")
                        .param("bookId", String.valueOf(book.getId()))
                        .param("quantity", "2")
                        .param("paymentMethod", "CASH")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/purchases"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(flash().attributeExists("orderId"));

        // Assert: purchase created & stock reduced
        Book reloaded = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(reloaded.getStockQuantity()).isEqualTo(3);

        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(1);
        Purchase p = purchases.get(0);
        assertThat(p.getUser().getUsername()).isEqualTo("buyer");
        assertThat(p.getBook().getTitle()).isEqualTo("Effective Java");
        assertThat(p.getQuantity()).isEqualTo(2);
        assertThat(p.getTotalAmount()).isEqualTo(1300.0);
        assertThat(p.getPaymentStatus()).isEqualTo(Purchase.PaymentStatus.PENDING);
        assertThat(p.getPaymentMethod()).isEqualTo("CASH");
    }
}

