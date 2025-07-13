package com.pm.tests.communication_tests;

import com.pm.tests.auth_tests.AuthTests;
import com.pm.tests.campaign_service_tests.CampaignTests;
import com.pm.tests.campaign_service_tests.CityTests;
import com.pm.tests.campaign_service_tests.ProductTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CommunicationTests {

    private static final String authURI = "http://localhost:10000/auth";
    private static final String URI = "http://localhost:10000/api";

    @Test
    public void createCampaign_shouldCreateStats() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_Stats_" + System.currentTimeMillis();
        Response cityResponse = CityTests.createCity(uniqueCityName, 52.5200, 13.4050, 200, URI);
        String cityId = cityResponse.jsonPath().getString("id");

        String uniqueProductName = "Product_Stats_" + System.currentTimeMillis();
        Response productResponse = ProductTests.createProduct(uniqueProductName, "Test desc", 200, URI, token);
        String productId = productResponse.jsonPath().getString("id");

        String uniqueCampaignName = "Campaign_Stats_" + System.currentTimeMillis();

        Response campaignResponse = CampaignTests.createCampaign(
                uniqueCampaignName,
                "Test description",
                productId,
                "[\"stats\", \"test\"]",
                "2.50",
                "250.00",
                cityId,
                "20.0",
                200,
                URI,
                token
        );
        String campaignId = campaignResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/stats/" + campaignId)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(campaignId))
                .body("clicks", equalTo(0))
                .body("spentAmount", equalTo(0.0f));
    }

    @Test
    public void registerClick_shouldUpdateStats() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String cityId = CityTests.createCity("City_Click_" + System.currentTimeMillis(), 50.0, 20.0, 200, URI).jsonPath().getString("id");
        String productId = ProductTests.createProduct("Product_Click_" + System.currentTimeMillis(), "desc", 200, URI, token).jsonPath().getString("id");
        String campaignName = "Campaign_Click_" + System.currentTimeMillis();
        float bidAmount = 1.25f;


        Response campaignResponse = CampaignTests.createCampaign(
                campaignName,
                "desc",
                productId,
                "[]",
                String.valueOf(bidAmount),
                "100.0", cityId,
                "5.0",
                200,
                URI,
                token
        );
        String campaignId = campaignResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .post("/stats/{id}/click", campaignId)
                .then()
                .log().all()
                .statusCode(200);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/stats/" + campaignId)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(campaignId))
                .body("clicks", equalTo(1))
                .body("spentAmount", equalTo(bidAmount));
    }

    @Test
    public void registerClick_withInsufficientFunds_shouldStopCampaignAndReturnError() {
        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String cityId = CityTests.createCity("City_Funds_" + System.currentTimeMillis(), 48.8, 2.3, 200, URI).jsonPath().getString("id");
        String productId = ProductTests.createProduct("Product_Funds_" + System.currentTimeMillis(), "desc", 200, URI, token).jsonPath().getString("id");
        String campaignName = "Campaign_Funds_" + System.currentTimeMillis();
        String bidAmount = "5.00";
        String campaignAmount = "5.00";

        Response campaignResponse = CampaignTests.createCampaign(
                campaignName, "desc", productId, "[]", bidAmount, campaignAmount, cityId, "1.0", 200, URI, token);
        String campaignId = campaignResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/campaign/" + campaignId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("active", equalTo("true"));

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .post("/stats/{id}/click", campaignId)
                .then()
                .statusCode(200);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .post("/stats/{id}/click", campaignId)
                .then()
                .log().all()
                .statusCode(400);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/campaign/" + campaignId)
                .then()
                .log().all()
                .statusCode(200)
                .body("active", equalTo("false"));
    }

    @Test
    public void deleteCampaign_shouldAlsoDeleteStats() {
        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_Stats_" + System.currentTimeMillis();
        Response cityResponse = CityTests.createCity(uniqueCityName, 52.5200, 13.4050, 200, URI);
        String cityId = cityResponse.jsonPath().getString("id");

        String uniqueProductName = "Product_Stats_" + System.currentTimeMillis();
        Response productResponse = ProductTests.createProduct(uniqueProductName, "Test desc", 200, URI, token);
        String productId = productResponse.jsonPath().getString("id");

        String uniqueCampaignName = "Campaign_Stats_" + System.currentTimeMillis();

        Response campaignResponse = CampaignTests.createCampaign(
                uniqueCampaignName,
                "Test description",
                productId,
                "[\"stats\", \"test\"]",
                "2.50",
                "250.00",
                cityId,
                "20.0",
                200,
                URI,
                token
        );
        String campaignId = campaignResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI).get("/stats/" + campaignId)
                .then()
                .statusCode(200);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .delete("/campaign/delete/" + campaignId)
                .then()
                .statusCode(204);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/stats/" + campaignId)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void getStatsForNonExistentCampaign_shouldReturnNotFound() {
        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String nonExistentId = "00000000-0000-0000-0000-000000000000";

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .baseUri(URI)
                .when()
                .get("/stats/" + nonExistentId)
                .then()
                .log().all()
                .statusCode(400);
    }
}
