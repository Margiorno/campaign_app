package com.pm.tests.campaign_service_tests;

import com.pm.tests.auth_tests.AuthTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CityTests {

    private static final String authURI = "http://localhost:10000/auth";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:10000/api";
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

        String adminToken = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(adminToken,200,authURI);

        System.out.println("MILESTONE");

        return RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/city/add")
                .then()
                .log().all()
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
