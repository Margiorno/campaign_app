package com.pm.tests.campaign_service_tests;

import com.pm.tests.auth_tests.AuthTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProductTests {

    private static final String authURI = "http://localhost:10000/auth";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:10000/api";
    }

    public static Response createProduct(String name, String description, int expectedStatus, String baseUri, String token) {
        RestAssured.baseURI = baseUri;

        String payload = String.format("""
        {
          "name": "%s",
          "description": "%s"
        }
        """, name, description);

        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/product/new")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .response();
    }

    @Test
    public void addProduct_shouldReturnOkRequest() {
        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueProductName = "Product_" + System.currentTimeMillis();
        createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI, token);
    }
}
