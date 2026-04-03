package com.example.pagevault.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByStockQuantityGreaterThanOrderByTitleAsc(Integer minimumStock);

	List<Book> findByStockQuantityGreaterThanAndTitleContainingIgnoreCaseOrStockQuantityGreaterThanAndAuthorContainingIgnoreCase(
			Integer minimumStockForTitle,
			String title,
			Integer minimumStockForAuthor,
			String author
	);
}

