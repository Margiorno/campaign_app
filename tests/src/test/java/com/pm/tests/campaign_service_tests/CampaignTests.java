package com.pm.tests.campaign_service_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CampaignTests {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:9000";
    }

    @Test
    public void addCampaign_shouldReturnOkRequest(){

        String uniqueCityName = "City_" + System.currentTimeMillis();

        String cityPayload = """
            {
              "name": "%s",
              "latitude": 52.2297,
              "longitude": 21.0122
            }
            """.formatted(uniqueCityName);

        Response cityResponse = RestAssured.given()
                .contentType("application/json")
                .body(cityPayload)
                .when()
                .post("/city/add")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String uniqueCampaignName = "campaign_" + System.currentTimeMillis();

        // Product id from database
        // TODO when Product controller will be created, then in the test instead of hardcoded product id, make product first
        String campaignPayload = """
            {
              "name": "%s",
              "description": "Test description",
              "product": "23232323-2323-2323-2323-232323232324",
              "keywords": ["java", "test"],
              "bid_amount": "1.50",
              "campaign_amount": "100.00",
              "city": "%s",
              "radius": "10.0"
            }
            """.formatted(uniqueCampaignName, cityResponse.jsonPath().getString("id"));

        RestAssured.given()
                .contentType("application/json")
                .body(campaignPayload)
                .when()
                .post("/campaign/new")
                .then()
                .statusCode(200);
    }
}
