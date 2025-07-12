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

    public static Response createCampaign(
            String name,
            String description,
            String productId,
            String keywords,
            String bidAmount,
            String campaignAmount,
            String cityId,
            String radius,
            int expectedStatus,
            String baseUri
    ) {
        RestAssured.baseURI = baseUri;

        String payload = String.format("""
            {
              "name": "%s",
              "description": "%s",
              "product": "%s",
              "keywords": %s,
              "bid_amount": "%s",
              "campaign_amount": "%s",
              "city": "%s",
              "radius": "%s"
            }
            """, name, description, productId, keywords, bidAmount, campaignAmount, cityId, radius);

        return RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/campaign/new")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .response();
    }

    @Test
    public void addCampaign_shouldReturnOkRequest(){

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response campaignResponse = createCampaign(
                uniqueCampaignName,
                "Test description",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI
        );
    }

    @Test
    public void addTwoCampaignWithSameName_shouldReturnBadRequest(){

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response campaignResponse1 = createCampaign(
                uniqueCampaignName,
                "Test description",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI
        );

        Response campaignResponse2 = createCampaign(
                uniqueCampaignName,
                "Test description",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                400,
                RestAssured.baseURI
        );
    }

    @Test
    public void addCampaignWithMissingName_shouldReturnBadRequest() {
        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response response = createCampaign(
                "",
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                400,
                RestAssured.baseURI
        );

        response.then().log().all();
    }
}
