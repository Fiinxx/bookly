package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDtos;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
public class BookControllerTest {


    @Test
    @Order(1)
    void getAllBooks_should_return_list_with_caching_and_links() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .header("Cache-Control", containsString("max-age=60"))
                .body("_embedded.books", hasSize(greaterThan(0)))
                // HATEOAS Links
                .body("_links.self.href", containsString("/books"))
                .body("_links.search.href", containsString("%7B?title,isbn,author,publisher,genre%7D"));//encoded{}
    }

    @Test
    @Order(2)
    void getAllBooks_with_title_filter_should_return_matching_book() {
        // Test Case-Insensitive Partial Match
        given()
                .queryParam("title", "martian")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", hasSize(1))
                .body("_embedded.books[0].title", equalTo("The Martian"))
                .body("_embedded.books[0].author", equalTo("Andy Weir"));
    }

    @Test
    @Order(3)
    void getAllBooks_with_genre_filter_should_return_multiple_books() {
        given()
                .queryParam("genre", "Education")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", hasSize(2))
                .body("_embedded.books.title", hasItems("Clean Code", "Domain-Driven Design"));
    }

    @Test
    @Order(4)
    void getAllBooks_with_combined_filters_should_narrow_results() {
        // Filter by Genre 'Education' AND Author 'Martin'
        // Should exclude 'Domain-Driven Design' (Eric Evans)
        given()
                .queryParam("genre", "Education")
                .queryParam("author", "Martin")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", hasSize(1))
                .body("_embedded.books[0].title", equalTo("Clean Code"));
    }

    @Test
    @Order(5)
    void getAllBooks_with_isbn_filter_should_work() {
        // isbn partial match
        given()
                .queryParam("isbn", "978-0261") // The Hobbit
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", hasSize(1))
                .body("_embedded.books[0].title", equalTo("The Hobbit"));
    }

    @Test
    @Order(6)
    void getAllBooks_with_no_matches_should_return_empty_list() {
        given()
                .queryParam("title", "Harry Potter and the Missing Test Data")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", anyOf(nullValue(), hasSize(0)));
    }

    @Test
    @Order(7)
    void getAllBooks_pagination_should_limit_results() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 2)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books", hasSize(2))
                .body("_links.next", notNullValue())
                .body("_links.prev", nullValue());

        // fetch page 2
        given()
                .queryParam("page", 2)
                .queryParam("size", 2)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_links.prev", notNullValue());
    }


    @Test
    @Order(8)
    void getBookById_should_return_book_with_etag_and_links() {
        given()
                .pathParam("id", 1)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .body("id", equalTo(1))
                .body("title", equalTo("Clean Code"))
                //HATEOAS links
                .body("_links.self.href", containsString("/books/1"))
                .body("_links.collection.href", containsString("/books"))
                .body("_links.rate.href", containsString("bookId=1"));
    }

    @Test
    @Order(9)
    void getBookById_should_return_304_when_etag_matches() {
        // get etag
        String etag = given()
                .pathParam("id", 1)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(200)
                .extract().header("ETag");

        //send etag
        given()
                .pathParam("id", 1)
                .header("If-None-Match", etag)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(304); // Not Modified
    }

    @Test
    @Order(10)
    void getBookById_should_return_404_when_not_found() {
        given()
                .pathParam("id", 9999)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(404);
    }


    @Test
    @Order(11)
    void createBook_as_admin_should_succeed() {
        BookDtos.Create newBook = new BookDtos.Create();
        newBook.setIsbn("978-0000000001");
        newBook.setTitle("Integration Testing with Quarkus");
        newBook.setAuthor("Test Bot");
        newBook.setPagecount(100);
        newBook.setPrice(29.99);
        newBook.setGenre("Technology");
        newBook.setLanguage("English");
        newBook.setPublisher("TestPub");
        newBook.setDescription("A comprehensive guide.");

        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/books")
                .then()
                .statusCode(201)
                .header("Location", containsString("/books/"))
                .body("title", equalTo("Integration Testing with Quarkus"))
                .body("_links.self.href", notNullValue());
    }

    @Test
    @Order(12)
    void createBook_as_user_should_fail_forbidden() {
        BookDtos.Create newBook = new BookDtos.Create();
        newBook.setIsbn("978-0000000002");
        newBook.setTitle("Forbidden Book");

        given()
                .auth().preemptive().basic("alice", "admin") // Role: USER
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/books")
                .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @Order(13)
    void createBook_should_fail_validation_with_invalid_data() {
        BookDtos.Create invalidBook = new BookDtos.Create();
        // Missing ISBN (required)
        invalidBook.setTitle("Invalid Book");
        invalidBook.setPagecount(-10); // Negative page count

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(invalidBook)
                .when()
                .post("/books")
                .then()
                .statusCode(400); // Bad Request (Constraint Violation)
    }

    @Test
    @Order(14)
    void createBook_should_fail_conflict_on_duplicate_isbn() {
        BookDtos.Create duplicateBook = new BookDtos.Create();
        duplicateBook.setIsbn("978-0132350884"); // Existing ISBN from import-test.sql
        duplicateBook.setTitle("Duplicate");

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(duplicateBook)
                .when()
                .post("/books")
                .then()
                .statusCode(409); // Conflict
    }


    @Test
    @Order(15)
    void updateBook_as_admin_should_succeed_with_correct_etag() {
        long bookId = 2; // Domain-Driven Design

        //get etag
        String etag = given()
                .pathParam("id", bookId)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(200)
                .extract().header("ETag");

        //prepare
        BookDtos.Create updateDto = new BookDtos.Create();
        updateDto.setIsbn("978-0321125217");
        updateDto.setTitle("Domain-Driven Design (Updated)");
        updateDto.setAuthor("Eric Evans");
        updateDto.setPagecount(560);

        // update
        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .header("If-Match", etag)
                .pathParam("id", bookId)
                .body(updateDto)
                .when()
                .put("/books/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Domain-Driven Design (Updated)"))
                // Verify new ETag is generated
                .header("ETag", not(equalTo(etag)));
    }

    @Test
    @Order(16)
    void updateBook_should_fail_precondition_with_wrong_etag() {
        long bookId = 3;

        BookDtos.Create updateDto = new BookDtos.Create();
        updateDto.setIsbn("978-0553109535");
        updateDto.setTitle("Hacked Title");

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .header("If-Match", "\"invalid-etag\"") // Wrong ETag
                .pathParam("id", bookId)
                .body(updateDto)
                .when()
                .put("/books/{id}")
                .then()
                .statusCode(412); // Precondition Failed
    }


    @Test
    @Order(17)
    void deleteBook_as_user_should_return_404_or_forbidden() {
        given()
                .auth().preemptive().basic("alice", "admin")
                .pathParam("id", 4)
                .when()
                .delete("/books/{id}")
                .then()
                .statusCode(anyOf(is(404), is(403)));
    }

    @Test
    @Order(18)
    void deleteBook_as_admin_should_succeed() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .pathParam("id", 4) // The Hobbit
                .when()
                .delete("/books/{id}")
                .then()
                .statusCode(204); // No Content

        // Verify it's gone
        given()
                .pathParam("id", 4)
                .when()
                .get("/books/{id}")
                .then()
                .statusCode(404);
    }
}