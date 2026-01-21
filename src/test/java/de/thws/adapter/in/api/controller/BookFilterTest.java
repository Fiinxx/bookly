package de.thws.adapter.in.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookFilterTest {

    @Test
    @Order(1)
    void filter_by_author_martin_should_return_clean_code() {
        given()
                .queryParam("author", "Martin")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books.title", hasItem("Clean Code"))
                .body("_embedded.books.author", everyItem(containsString("Martin")))
                //hateoas
                .body("_links.self.href", containsString("author=Martin"));
    }

    @Test
    @Order(2)
    void filter_by_genre_scifi_should_return_the_martian() {
        given()
                .queryParam("genre", "Sci-Fi")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books.title", hasItem("The Martian"))
                .body("_embedded.books.genre", everyItem(is("Sci-Fi")));
    }

    @Test
    @Order(3)
    void filter_by_isbn_should_return_the_hobbit() {
        String hobbitIsbn = "978-0261102217";

        given()
                .queryParam("isbn", hobbitIsbn)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books.title", contains("The Hobbit"))
                .body("_embedded.books.isbn", everyItem(is(hobbitIsbn)));
    }

    @Test
    @Order(4)
    void filter_by_publisher_addison_wesley_should_return_ddd() {
        given()
                .queryParam("publisher", "Addison-Wesley")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("_embedded.books.title", hasItem("Domain-Driven Design"))
                .body("_embedded.books.publisher", everyItem(is("Addison-Wesley")));
    }
}