package com.pm.tests.campaign_service_tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CityTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    @Test
    public void addCity_shouldReturnOkRequest() {
        String uniqueCityName = "City_" + System.currentTimeMillis();

        String payload = """
            {
              "name": "%s",
              "latitude": 52.2297,
              "longitude": 21.0122
            }
            """.formatted(uniqueCityName);

        RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/city/add")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
