-- Seed a richer in-stock catalog without duplicating records on restart.

-- Seed an initial ADMIN account.
-- Login: username = admin, password = Admin@1234
-- Note: Spring Security requires BCrypt hashes, so the hash is stored below instead of the plain password.
INSERT INTO users (username, email, password, role)
SELECT 'admin', 'admin@pagevault.com', '$2a$10$a6IPMSqs3WPwagJeWb2p4eUnd7VcZPsUxJT.hMxsgDXh6mO6VIa2m', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Clean Code', 'Robert C. Martin', 31.99, 18
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Clean Code' AND author = 'Robert C. Martin');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Clean Architecture', 'Robert C. Martin', 33.50, 14
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Clean Architecture' AND author = 'Robert C. Martin');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Effective Java', 'Joshua Bloch', 45.00, 12
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Effective Java' AND author = 'Joshua Bloch');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Java Concurrency in Practice', 'Brian Goetz', 42.25, 9
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Java Concurrency in Practice' AND author = 'Brian Goetz');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Spring in Action', 'Craig Walls', 39.99, 11
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Spring in Action' AND author = 'Craig Walls');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Head First Design Patterns', 'Eric Freeman', 37.75, 10
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Head First Design Patterns' AND author = 'Eric Freeman');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Designing Data-Intensive Applications', 'Martin Kleppmann', 48.99, 8
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Designing Data-Intensive Applications' AND author = 'Martin Kleppmann');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Refactoring', 'Martin Fowler', 44.99, 10
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Refactoring' AND author = 'Martin Fowler');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'The Pragmatic Programmer', 'Andrew Hunt', 36.40, 15
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Pragmatic Programmer' AND author = 'Andrew Hunt');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Domain-Driven Design', 'Eric Evans', 46.30, 7
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Domain-Driven Design' AND author = 'Eric Evans');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Microservices Patterns', 'Chris Richardson', 41.00, 13
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Microservices Patterns' AND author = 'Chris Richardson');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Building Microservices', 'Sam Newman', 40.20, 9
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Building Microservices' AND author = 'Sam Newman');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Test-Driven Development: By Example', 'Kent Beck', 34.90, 16
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Test-Driven Development: By Example' AND author = 'Kent Beck');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Working Effectively with Legacy Code', 'Michael Feathers', 43.10, 6
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Working Effectively with Legacy Code' AND author = 'Michael Feathers');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Grokking Algorithms', 'Aditya Bhargava', 29.95, 20
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Grokking Algorithms' AND author = 'Aditya Bhargava');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Introduction to Algorithms', 'Thomas H. Cormen', 59.00, 5
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Introduction to Algorithms' AND author = 'Thomas H. Cormen');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Cracking the Coding Interview', 'Gayle Laakmann McDowell', 35.50, 17
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Cracking the Coding Interview' AND author = 'Gayle Laakmann McDowell');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Kotlin in Action', 'Dmitry Jemerov', 38.25, 9
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Kotlin in Action' AND author = 'Dmitry Jemerov');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'Code Complete', 'Steve McConnell', 47.80, 7
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Code Complete' AND author = 'Steve McConnell');

INSERT INTO books (title, author, price, stock_quantity)
SELECT 'You Don''t Know JS Yet', 'Kyle Simpson', 32.15, 11
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'You Don''t Know JS Yet' AND author = 'Kyle Simpson');

