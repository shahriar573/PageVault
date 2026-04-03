package com.example.pagevault.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JPA slice tests backed by PostgreSQL Testcontainers.
 * <p>
 * These tests verify entity mappings, constraints, and repository query methods.
 * Full end-to-end integration tests remain under {@code com.example.pagevault.it}.
 */
@Testcontainers
@DataJpaTest
@ActiveProfiles("tc")
@EnabledIf(expression = "#{systemProperties['testcontainers'] == 'true'}", reason = "Requires Docker/Testcontainers")
class PageRepositoryTest {

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
	void userRepository_findByUsername_returnsUser() {
		User user = new User();
		user.setUsername("repo_user");
		user.setEmail("repo_user@example.com");
		user.setPassword("encoded");
		user.setRole(User.Role.USER);
		userRepository.saveAndFlush(user);

		User found = userRepository.findByUsername("repo_user");
		assertThat(found).isNotNull();
		assertThat(found.getEmail()).isEqualTo("repo_user@example.com");
	}

	@Test
	void userRepository_uniqueUsername_isEnforced() {
		User u1 = new User();
		u1.setUsername("dup");
		u1.setEmail("dup1@example.com");
		u1.setPassword("encoded");
		u1.setRole(User.Role.USER);
		userRepository.saveAndFlush(u1);

		User u2 = new User();
		u2.setUsername("dup");
		u2.setEmail("dup2@example.com");
		u2.setPassword("encoded");
		u2.setRole(User.Role.USER);

		assertThrows(Exception.class, () -> userRepository.saveAndFlush(u2));
	}

	@Test
	void bookRepository_stockAwareQuery_returnsOnlyInStock_sortedByTitle() {
		Book b1 = new Book();
		b1.setTitle("B Title");
		b1.setAuthor("A1");
		b1.setPrice(10.0);
		b1.setStockQuantity(5);
		bookRepository.save(b1);

		Book b2 = new Book();
		b2.setTitle("A Title");
		b2.setAuthor("A2");
		b2.setPrice(20.0);
		b2.setStockQuantity(1);
		bookRepository.save(b2);

		Book b3 = new Book();
		b3.setTitle("Out of stock");
		b3.setAuthor("A3");
		b3.setPrice(30.0);
		b3.setStockQuantity(0);
		bookRepository.saveAndFlush(b3);

		var available = bookRepository.findByStockQuantityGreaterThanOrderByTitleAsc(0);
		assertThat(available).extracting(Book::getTitle)
				.containsExactly("A Title", "B Title");
	}
}
