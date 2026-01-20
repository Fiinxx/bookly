-- import only for integration tests
-- USERS
-- Password for all users is admin
INSERT INTO users (id, password, role, username, email)
VALUES (1, '$2y$10$jUHAszra9BeurVZ4P9GN0O3nYME.AkM3DcZdLDtEuFf4BD.SufdHu', 'ADMIN', 'admin', 'admin@bookly.com');

INSERT INTO users (id, password, role, username, email)
VALUES (2, '$2y$10$jUHAszra9BeurVZ4P9GN0O3nYME.AkM3DcZdLDtEuFf4BD.SufdHu', 'USER', 'alice', 'alice@example.com');

INSERT INTO users (id, password, role, username, email)
VALUES (3, '$2y$10$jUHAszra9BeurVZ4P9GN0O3nYME.AkM3DcZdLDtEuFf4BD.SufdHu', 'USER', 'bob', 'bob@example.com');


-- BOOKS
INSERT INTO books (id, isbn, title, author, pagecount, publisher, genre, price, language, description, publishingDate)
VALUES (1, '978-0132350884', 'Clean Code', 'Robert C. Martin', 464, 'Prentice Hall', 'Education', 30.50, 'English', 'A Handbook of Agile Software Craftsmanship', '2008-08-01');

INSERT INTO books (id, isbn, title, author, pagecount, publisher, genre, price, language, description, publishingDate)
VALUES (2, '978-0321125217', 'Domain-Driven Design', 'Eric Evans', 560, 'Addison-Wesley', 'Education', 45.00, 'English', 'Tackling Complexity in the Heart of Software', '2003-08-30');

INSERT INTO books (id, isbn, title, author, pagecount, publisher, genre, price, language, description, publishingDate)
VALUES (3, '978-0553109535', 'The Martian', 'Andy Weir', 369, 'Crown', 'Sci-Fi', 12.99, 'English', 'Six days ago, astronaut Mark Watney became one of the first people to walk on Mars.', '2014-02-11');

INSERT INTO books (id, isbn, title, author, pagecount, publisher, genre, price, language, description, publishingDate)
VALUES (4, '978-0261102217', 'The Hobbit', 'J.R.R. Tolkien', 310, 'HarperCollins', 'Fantasy', 15.20, 'English', 'In a hole in the ground there lived a hobbit.', '1937-09-21');


-- RATINGS
INSERT INTO ratings (id, rating, comment, creationTime, user_id, book_id)
VALUES (1, 5, 'Essential reading for any developer.', '2023-10-01', 2, 1);

INSERT INTO ratings (id, rating, comment, creationTime, user_id, book_id)
VALUES (2, 4, 'Great suspense, but a bit technical.', '2023-10-05', 2, 3);

INSERT INTO ratings (id, rating, comment, creationTime, user_id, book_id)
VALUES (3, 5, 'Changed the way I think about software architecture.', '2023-11-12', 3, 2);

INSERT INTO ratings (id, rating, comment, creationTime, user_id, book_id)
VALUES (4, 5, 'A timeless classic.', '2023-12-01', 3, 4);


-- SEQUENCE RESETS
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
ALTER TABLE books ALTER COLUMN id RESTART WITH 5;
ALTER TABLE ratings ALTER COLUMN id RESTART WITH 5;