package com.pm.tests.campaign_service_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CityTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    public static Response createCity(String name, Double latitude, Double longitude, int expectedStatus, String baseUri) {
        RestAssured.baseURI = baseUri;

        String payload = String.format("""
        {
          "name": "%s",
          "latitude": %s,
          "longitude": %s
        }
        """, name, latitude != null ? latitude.toString() : "null", longitude != null ? longitude.toString() : "null");

        return RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/city/add")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .response();
    }

    @Test
    public void addCity_shouldReturnOkRequest() {
        String uniqueCityName = "City_" + System.currentTimeMillis();
        createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
    }
}
