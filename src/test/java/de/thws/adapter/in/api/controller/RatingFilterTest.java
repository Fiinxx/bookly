package de.thws.adapter.in.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class RatingFilterTest {

    @Test
    void filter_by_bookId_1_should_return_clean_code_rating() {
        given()
                .queryParam("bookId", 1)
                .when()
                .get("/ratings")
                .then()
                .statusCode(200)
                .body("_embedded.ratings.comment", hasItem("Essential reading for any developer."));
    }

    @Test
    void filter_by_userId_2_should_return_ratings_from_alice() {
        given()
                .queryParam("userId", 2)
                .when()
                .get("/ratings")
                .then()
                .statusCode(200)
                .body("_embedded.ratings.size()", is(2))
                .body("_embedded.ratings._links.user.href", everyItem(containsString("/users/2")))
                .body("_embedded.ratings.comment", hasItems("Essential reading for any developer.", "Great suspense, but a bit technical."));
    }

    @Test
    void filter_by_score_4_should_return_only_one_rating() {
        given()
                .queryParam("rating", 4)
                .when()
                .get("/ratings")
                .then()
                .statusCode(200)
                .body("_embedded.ratings.size()", is(1))
                .body("_embedded.ratings.rating", everyItem(is(4)));
    }

    @Test
    void filter_by_date_range_should_work_with_import_data() {
        given()
                .queryParam("createdAfter", "2023-09-01T00:00:00Z")
                .queryParam("createdBefore", "2023-10-31T23:59:59Z")
                .when()
                .get("/ratings")
                .then()
                .statusCode(200)
                .body("ratings", not(empty()));
    }

}