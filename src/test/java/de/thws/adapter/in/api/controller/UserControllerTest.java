package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.UserDtos;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
public abstract class UserControllerTest {

    @Test
    @Order(1)
    void getUserById_should_contain_correct_hyperlinks() {
        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .accept(ContentType.JSON)
                .when()
                .pathParam("id", 1)
                .get("/users/{id}")
                .then()
                .statusCode(200)
                // Prüft den Self-Link
                .body("_links.self.href", containsString("/users/1"))
                // Prüft den Link zu den Ratings des Users
                .body("_links.ratings.href", containsString("/ratings?userId=1"))
                // Prüft Admin-spezifische Links (Update/Delete)
                .body("_links.update.href", containsString("/users/1"))
                .body("_links.delete.href", containsString("/users/1"));
    }

    @Test
    @Order(2)
    void getUserById_should_return_304_when_etag_matches() {
        //etag abrufen
        String etag = given()
                .when().get("/users/1")
                .then().extract().header("Etag");

        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .header("If-None-Match", etag)
                .when()
                .get("/users/1")
                .then()
                .statusCode(304); // Not Modified
    }

    @Test
    @Order(3)
    void updateUser_should_prevent_lost_update_with_etag() {
        UserDtos.Update updateDto = new UserDtos.Update("NewName", "test@example.com");

        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .header("If-Match", "\"veralteter-etag\"")
                .body(updateDto)
                .when()
                .put("/users/1")
                .then()
                .statusCode(412); //failed
    }


    @Test
    @Order(4)
    void user_should_not_be_able_to_update_other_user() {
        UserDtos.Update updateDto = new UserDtos.Update("Hack", "hack@example.com");

        given()
                .auth().preemptive().basic("alice", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/users/1") // alice versucht user 1 zu ändern
                .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @Order(5)
    void admin_registration_should_only_be_allowed_for_admins() {
        UserDtos.Create adminDto = new UserDtos.Create("NewAdmin", "Pass@gmail.com", "Pass", "ADMIN");

        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .contentType(ContentType.JSON)
                .body(adminDto)
                .when()
                .post("/users/admins")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(6)
    void registerUser_should_be_publicly_accessible() {
        UserDtos.Create userDto = new UserDtos.Create("NewUser", "user@thws.de", "Pass", "USER");

        given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    @Test
    @Order(8)
    void deleteUser_should_return_204() {
        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .when()
                .delete("/users/1")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(7)
    void getNonExistingUser_should_return_404() {
        given()
                .auth().preemptive().basic("admin", "admin") // Role: ADMIN
                .when()
                .get("/users/9999")
                .then()
                .statusCode(404);
    }
}