package com.pm.tests.auth_tests;

import com.pm.tests.campaign_service_tests.CampaignTests;
import com.pm.tests.campaign_service_tests.CityTests;
import com.pm.tests.campaign_service_tests.ProductTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class PermissionsTests {

    private static final String authURI = "http://localhost:10000/auth";

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:10000/api";
    }

    @Test
    public void getAllCampaigns_asAdmin_shouldReturnAllCampaigns() {
        String adminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);
        int initialCount = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/campaign/all")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$")
                .size();

        String user1Email = "user1_" + System.currentTimeMillis() + "@test.com";
        String user1Token = AuthTests.getRegisterToken(user1Email, "password", authURI);
        String productId1 = ProductTests.createProduct("P1_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user1Token).jsonPath().getString("id");
        String cityId1 = CityTests.createCity("C1_" + System.currentTimeMillis(), 1.0, 1.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        CampaignTests.createCampaign("CampaignByUser1_" + System.currentTimeMillis(), "d", productId1, "[]", "1", "1", cityId1, "1", 200, RestAssured.baseURI, user1Token);

        String user2Email = "user2_" + System.currentTimeMillis() + "@test.com";
        String user2Token = AuthTests.getRegisterToken(user2Email, "password", authURI);
        String productId2 = ProductTests.createProduct("P2_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user2Token).jsonPath().getString("id");
        String cityId2 = CityTests.createCity("C2_" + System.currentTimeMillis(), 2.0, 2.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        CampaignTests.createCampaign("CampaignByUser2_" + System.currentTimeMillis(), "d", productId2, "[]", "1", "1", cityId2, "1", 200, RestAssured.baseURI, user2Token);

        int finalCount = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/campaign/all")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$")
                .size();

        Assertions.assertEquals(initialCount + 2, finalCount);
    }

    @Test
    public void getAllCampaigns_asUser_shouldReturnOnlyOwnedCampaigns() {

        String user1Email = "user1_" + System.currentTimeMillis() + "@test.com";
        String user1Token = AuthTests.getRegisterToken(user1Email, "password", authURI);
        String productId1 = ProductTests.createProduct("P1_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user1Token).jsonPath().getString("id");
        String cityId1 = CityTests.createCity("C1_" + System.currentTimeMillis(), 1.0, 1.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        String name = "MyCampaign_" + System.currentTimeMillis();
        CampaignTests.createCampaign(name, "d", productId1, "[]", "1", "1", cityId1, "1", 200, RestAssured.baseURI, user1Token);

        String user2Email = "user2_" + System.currentTimeMillis() + "@test.com";
        String user2Token = AuthTests.getRegisterToken(user2Email, "password", authURI);
        String productId2 = ProductTests.createProduct("P2_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user2Token).jsonPath().getString("id");
        String cityId2 = CityTests.createCity("C2_" + System.currentTimeMillis(), 2.0, 2.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        CampaignTests.createCampaign("NotMyCampaign_" + System.currentTimeMillis(), "d", productId2, "[]", "1", "1", cityId2, "1", 200, RestAssured.baseURI, user2Token);

        RestAssured.given()
                .header("Authorization", "Bearer " + user1Token)
                .when()
                .get("/campaign/all")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", equalTo(name));
    }

    @Test
    public void getOrDeleteCampaign_asNonOwner_shouldBeForbidden() {
        String user1Email = "user1_" + System.currentTimeMillis() + "@test.com";
        String user1Token = AuthTests.getRegisterToken(user1Email, "password", authURI);
        String productId1 = ProductTests.createProduct("P1_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user1Token).jsonPath().getString("id");
        String cityId1 = CityTests.createCity("C1_" + System.currentTimeMillis(), 1.0, 1.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        Response createdCampaign = CampaignTests.createCampaign("OwnedCampaign_" + System.currentTimeMillis(), "d", productId1, "[]", "1", "1", cityId1, "1", 200, RestAssured.baseURI, user1Token);
        String campaignId = createdCampaign.jsonPath().getString("id");

        String user2Email = "user2_" + System.currentTimeMillis() + "@test.com";
        String user2Token = AuthTests.getRegisterToken(user2Email, "password", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .when()
                .get("/campaign/" + campaignId)
                .then()
                .log().all()
                .statusCode(400);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .contentType("application/json")
                .body("{\"name\": \"hacked\"}")
                .when()
                .patch("/campaign/update/" + campaignId)
                .then()
                .log().all()
                .statusCode(400);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .when()
                .delete("/campaign/delete/" + campaignId)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void getOrDeleteCampaign_asAdmin_shouldBeAllowed() {
        String user1Email = "user1_" + System.currentTimeMillis() + "@test.com";
        String user1Token = AuthTests.getRegisterToken(user1Email, "password", authURI);
        String productId1 = ProductTests.createProduct("P1_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, user1Token).jsonPath().getString("id");
        String cityId1 = CityTests.createCity("C1_" + System.currentTimeMillis(), 1.0, 1.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        String name = "SomeUserCampaign_" + System.currentTimeMillis();
        Response createdCampaign = CampaignTests.createCampaign(name, "d", productId1, "[]", "1", "1", cityId1, "1", 200, RestAssured.baseURI, user1Token);
        String campaignId = createdCampaign.jsonPath().getString("id");

        String adminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/campaign/" + campaignId)
                .then()
                .statusCode(200)
                .body("name", equalTo(name));

        String updatedByAdmin = "updatedByAdmin_" + System.currentTimeMillis();
        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body("{\"name\": \"" + updatedByAdmin + "\"}")
                .when()
                .patch("/campaign/update/" + campaignId)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedByAdmin));

        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/campaign/delete/" + campaignId)
                .then()
                .statusCode(204);
    }
}
