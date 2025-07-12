package com.pm.tests.campaign_service_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProductTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    public static Response createProduct(String name, String description, int expectedStatus, String baseUri) {
        RestAssured.baseURI = baseUri;

        String payload = String.format("""
        {
          "name": "%s",
          "description": "%s"
        }
        """, name, description);

        return RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/product/add")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .response();
    }

    @Test
    public void addProduct_shouldReturnOkRequest() {

        String uniqueProductName = "Product_" + System.currentTimeMillis();
        createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI);
    }
}
