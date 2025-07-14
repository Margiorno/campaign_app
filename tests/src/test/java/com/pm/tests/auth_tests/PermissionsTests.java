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

    @Test
    public void getAllProducts_asAdmin_shouldReturnAllProducts() {
        String adminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);

        int initialCount = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/product/get")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$")
                .size();

        String user1Token = AuthTests.getRegisterToken("user1_" + System.currentTimeMillis() + "@test.com", "password", authURI);
        ProductTests.createProduct("Product1_" + System.currentTimeMillis(), "desc", 200, RestAssured.baseURI, user1Token);

        String user2Token = AuthTests.getRegisterToken("user2_" + System.currentTimeMillis() + "@test.com", "password", authURI);
        ProductTests.createProduct("Product2_" + System.currentTimeMillis(), "desc", 200, RestAssured.baseURI, user2Token);

        int finalCount = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/product/get")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$")
                .size();

        Assertions.assertEquals(initialCount + 2, finalCount);
    }

    @Test
    public void getAllProducts_asUser_shouldReturnOnlyOwnedProducts() {
        String userToken = AuthTests.getRegisterToken("user_" + System.currentTimeMillis() + "@test.com", "password", authURI);
        String uniqueName = "MyProduct_" + System.currentTimeMillis();
        ProductTests.createProduct(uniqueName, "desc", 200, RestAssured.baseURI, userToken);

        RestAssured.given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get("/product/get")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", equalTo(uniqueName));
    }

    @Test
    public void getOrDeleteProduct_asNonOwner_shouldBeForbidden() {
        String user1Token = AuthTests.getRegisterToken("user1_" + System.currentTimeMillis() + "@test.com", "password", authURI);
        String productName = "OwnedProduct_" + System.currentTimeMillis();
        String productId = ProductTests.createProduct(productName, "desc", 200, RestAssured.baseURI, user1Token)
                .jsonPath().getString("id");

        String user2Token = AuthTests.getRegisterToken("user2_" + System.currentTimeMillis() + "@test.com", "password", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .when()
                .get("/product/get/" + productId)
                .then()
                .statusCode(400);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .contentType("application/json")
                .body("{\"name\": \"hacked\", \"description\": \"hacked\"}")
                .when()
                .patch("/product/update/" + productId)
                .then()
                .statusCode(400);

        RestAssured.given()
                .header("Authorization", "Bearer " + user2Token)
                .when()
                .delete("/product/delete/" + productId)
                .then()
                .statusCode(400);
    }

    @Test
    public void getOrDeleteProduct_asAdmin_shouldBeAllowed() {
        String userToken = AuthTests.getRegisterToken("user_" + System.currentTimeMillis() + "@test.com", "password", authURI);
        String productName = "UserProduct_" + System.currentTimeMillis();
        String productId = ProductTests.createProduct(productName, "desc", 200, RestAssured.baseURI, userToken)
                .jsonPath().getString("id");

        String adminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/product/get/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo(productName));

        String updatedName = "UpdatedByAdmin_" + System.currentTimeMillis();
        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .body("{\"name\": \"" + updatedName + "\", \"description\": \"updated desc\"}")
                .when()
                .patch("/product/update/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName));

        RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/product/delete/" + productId)
                .then()
                .log().all()
                .statusCode(204);
    }


    @Test
    public void userCannotAccess_UserController_AdminEndpoints() {
        String userEmail = "regular_user_" + System.currentTimeMillis() + "@test.com";
        String userToken = AuthTests.getRegisterToken(userEmail, "password123", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + userToken)
                .baseUri(authURI)
                .when()
                .get("/all")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("User not authorized"));

        String dummyId = "some-random-id";
        String payload = "{\"email\": \"new@email.com\", \"password\": \"newpassword\", \"role\": \"USER\"}";

        RestAssured.given()
                .header("Authorization", "Bearer " + userToken)
                .baseUri(authURI)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/edit/" + dummyId)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("User not authorized"));
    }


    @Test
    public void promotedAdminGainsAccessToOtherUsersCampaigns() {
        String ownerEmail = "campaign_owner_" + System.currentTimeMillis() + "@test.com";
        String ownerToken = AuthTests.getRegisterToken(ownerEmail, "password123", authURI);
        String productId = ProductTests.createProduct("Prod_For_Perm_Test_" + System.currentTimeMillis(), "d", 200, RestAssured.baseURI, ownerToken).jsonPath().getString("id");
        String cityId = CityTests.createCity("City_For_Perm_Test_" + System.currentTimeMillis(), 1.0, 1.0, 200, RestAssured.baseURI).jsonPath().getString("id");
        String campaignName = "Owned_Campaign_" + System.currentTimeMillis();
        CampaignTests.createCampaign(campaignName, "d", productId, "[]", "1", "1", cityId, "1", 200, RestAssured.baseURI, ownerToken);

        String futureAdminEmail = "future_admin_" + System.currentTimeMillis() + "@test.com";
        String futureAdminToken = AuthTests.getRegisterToken(futureAdminEmail, "password123", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + futureAdminToken)
                .when()
                .get("/campaign/all")
                .then()
                .statusCode(200)
                .body("size()", is(0));

        String rootAdminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);

        Response usersResponse = RestAssured.given()
                .header("Authorization", "Bearer " + rootAdminToken)
                .baseUri(authURI)
                .get("/all")
                .then().statusCode(200).extract().response();
        String futureAdminId = usersResponse.jsonPath().getString("find { it.email == '" + futureAdminEmail + "' }.id");
        Assertions.assertNotNull(futureAdminId, "User to be promoted was not found.");

        String editPayload = String.format("{\"email\": \"%s\", \"password\": \"password123\", \"role\": \"ADMIN\"}", futureAdminEmail);
        RestAssured.given()
                .header("Authorization", "Bearer " + rootAdminToken)
                .baseUri(authURI)
                .contentType("application/json")
                .body(editPayload)
                .when()
                .post("/edit/" + futureAdminId)
                .then()
                .statusCode(200);

        String promotedAdminToken = AuthTests.getLoginToken(futureAdminEmail, "password123", authURI);

        RestAssured.given()
                .header("Authorization", "Bearer " + promotedAdminToken)
                .when()
                .get("/campaign/all")
                .then()
                .statusCode(200)
                .body("find { it.name == '" + campaignName + "' }", notNullValue());
    }

    @Test
    public void adminCanDemoteAnotherAdmin() {
        String rootAdminToken = AuthTests.getLoginToken("admin@example.com", "password", authURI);
        String tempAdminEmail = "temp_admin_" + System.currentTimeMillis() + "@test.com";
        AuthTests.getRegisterToken(tempAdminEmail, "password123", authURI);

        Response usersResponse = RestAssured.given()
                .header("Authorization", "Bearer " + rootAdminToken)
                .baseUri(authURI)
                .get("/all")
                .then().statusCode(200).extract().response();
        String tempAdminId = usersResponse.jsonPath().getString("find { it.email == '" + tempAdminEmail + "' }.id");

        String promotePayload = String.format("{\"email\": \"%s\", \"password\": \"password123\", \"role\": \"ADMIN\"}", tempAdminEmail);
        RestAssured.given()
                .header("Authorization", "Bearer " + rootAdminToken)
                .baseUri(authURI)
                .contentType("application/json")
                .body(promotePayload)
                .when()
                .post("/edit/" + tempAdminId)
                .then().statusCode(200);

        String tempAdminToken = AuthTests.getLoginToken(tempAdminEmail, "password123", authURI);
        RestAssured.given()
                .header("Authorization", "Bearer " + tempAdminToken)
                .baseUri(authURI)
                .get("/all")
                .then().statusCode(200);

        String demotePayload = String.format("{\"email\": \"%s\", \"password\": \"password123\", \"role\": \"USER\"}", tempAdminEmail);
        RestAssured.given()
                .header("Authorization", "Bearer " + rootAdminToken)
                .baseUri(authURI)
                .contentType("application/json")
                .body(demotePayload)
                .when()
                .post("/edit/" + tempAdminId)
                .then().statusCode(200);

        String demotedUserToken = AuthTests.getLoginToken(tempAdminEmail, "password123", authURI);
        RestAssured.given()
                .header("Authorization", "Bearer " + demotedUserToken)
                .baseUri(authURI)
                .get("/all")
                .then()
                .statusCode(400)
                .body("message", equalTo("User not authorized"));
    }

}
