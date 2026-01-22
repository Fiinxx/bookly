package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.RatingDtos;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
public abstract class RatingControllerTest {

    @Test
    void getAllRatings_should_have_cache_control_header() {
        given()
                .when().get("/ratings")
                .then()
                .statusCode(200)
                .header("Cache-Control", containsString("max-age=60"));
    }

    @Test
    void getRatingById_should_return_304_when_etag_matches() {
        String etag = given()
                .when().get("/ratings/1")
                .then().extract().header("Etag");

        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .header("If-None-Match", etag)
                .when()
                .get("/ratings/1")
                .then()
                .statusCode(304);
    }

    @Test
    void updateRating_should_return_412_on_precondition_failed() {
        RatingDtos.Create updateDto = new RatingDtos.Create(4, "Update", 1L);

        given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .header("If-Match", "\"veralteter-etag\"")
                .body(updateDto)
                .when()
                .put("/ratings/1")
                .then()
                .statusCode(412);
    }


    @Test
    void deleteRating_as_non_owner_should_be_forbidden_or_not_found() {
        given()
                .auth().preemptive().basic("alice", "admin") // Role: ADMIN
                .when()
                .delete("/ratings/99")
                .then()
                .statusCode(anyOf(is(403), is(404)));
    }

    @Test
    void admin_should_be_allowed_to_delete_any_rating() {
        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .when()
                .delete("/ratings/1")
                .then()
                .statusCode(204);
    }

    @Test
    void createRating_should_return_201_and_location() {
        RatingDtos.Create newRating = new RatingDtos.Create(5, "Tolles Buch", 1L);

        given()
                .auth().preemptive().basic("bob", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .body(newRating)
                .when()
                .post("/ratings")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("rating", is(5));
    }

    @Test
    void should_return_400_on_invalid_input() {
        RatingDtos.Create invalidRating = new RatingDtos.Create(99, "", 1L);

        given()
                .auth().preemptive().basic("alice", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .body(invalidRating)
                .when()
                .post("/ratings")
                .then()
                .statusCode(400);
    }

    @Test
    void getRatingById_should_return_404_if_not_exists() {
        given()
                .when()
                .get("/ratings/9999")
                .then()
                .statusCode(404);
    }
}