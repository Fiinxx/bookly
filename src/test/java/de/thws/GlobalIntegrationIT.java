package de.thws;

import de.thws.adapter.in.api.controller.*;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;

@QuarkusIntegrationTest // This is the ONLY place this annotation should exist
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class GlobalIntegrationIT {

    @Nested
    @org.junit.jupiter.api.Order(4)
    class Books extends BookControllerTest {}

    @Nested
    @org.junit.jupiter.api.Order(1)
    class Filters extends BookFilterTest {}

    @Nested
    @org.junit.jupiter.api.Order(2)
    class FiltersRatings extends RatingFilterTest {}

    @Nested
    @org.junit.jupiter.api.Order(3)
    class Ratings extends RatingControllerTest {}

    @Nested
    @org.junit.jupiter.api.Order(5)
    class Users extends UserControllerTest {}
}