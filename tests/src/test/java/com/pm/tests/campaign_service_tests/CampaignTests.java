package com.pm.tests.campaign_service_tests;

import com.pm.tests.auth_tests.AuthTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class CampaignTests {

    private static final String authURI = "http://localhost:10000/auth";

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:10000/api";
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
            String baseUri,
            String token
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
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/campaign/new")
                .then()
                .assertThat()
                .log().all()
                .statusCode(expectedStatus)
                .extract()
                .response();
    }

    @Test
    public void addCampaign_shouldReturnOkRequest(){

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI, token);
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
                RestAssured.baseURI,
                token
        );
    }

    @Test
    public void addTwoCampaignWithSameName_shouldReturnBadRequest(){

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI, token);
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
                RestAssured.baseURI,
                token
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
                RestAssured.baseURI,
                token
        );
    }

    @Test
    public void addCampaignWithMissingName_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "test description", 200, RestAssured.baseURI, token);
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
                RestAssured.baseURI,
                token
        );

        response.then().log().all();
    }

    @Test
    public void addCampaignInvalidBidAmount_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response response = createCampaign(
                "ValidName",
                "desc",
                productId,
                keywords,
                "-1",
                "100.00",
                cityId,
                "10.0",
                400,
                RestAssured.baseURI,
                token
        );

        response.then().log().body();
    }

    @Test
    public void updateCampaign_shouldReturnUpdatedCampaign() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response createResponse = createCampaign(
                uniqueCampaignName,
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI,
                token
        );

        String campaignId = createResponse.jsonPath().getString("id");

        String updatedName = uniqueCampaignName + "_Updated";

        String updatePayload = String.format("""
            {
              "name": "%s",
              "description": "Updated description",
              "product": "%s",
              "keywords": ["updated", "keywords"],
              "bid_amount": "2.00",
              "campaign_amount": "200.00",
              "city": "%s",
              "radius": "15.0"
            }
            """, updatedName, productId, cityId);

        Response updateResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .patch("/campaign/update/" + campaignId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        updateResponse.then()
                .body("name", equalTo(updatedName))
                .body("description", equalTo("Updated description"))
                .body("bid_amount", equalTo("2.0"))
                .body("campaign_amount", equalTo("200.0"));
    }

    @Test
    public void startAndStopCampaign_shouldReturnOk() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";


        Response createResponse = createCampaign(
                uniqueCampaignName,
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI,
                token
        );

        String campaignId = createResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/campaign/" + campaignId + "/start")
                .then()
                .statusCode(200)
                .body("active", equalTo("true"));

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/campaign/" + campaignId + "/stop")
                .then()
                .statusCode(200)
                .body("active", equalTo("false"));
    }

    @Test
    public void addCampaignWithEmptyKeywords_shouldReturnOk() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);


        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[]";

        Response response = createCampaign(
                uniqueCampaignName,
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI,
                token
        );

        response.then()
                .statusCode(200)
                .body("keywords", hasSize(1))
                .body("keywords[0]", equalTo(uniqueCampaignName ));
    }

    @Test
    public void addCampaignWithNullKeywords_shouldReturnOk() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String payload = String.format("""
            {
              "name": "%s",
              "description": "desc",
              "product": "%s",
              "bid_amount": "1.50",
              "campaign_amount": "100.00",
              "city": "%s",
              "radius": "10.0"
            }
            """, uniqueCampaignName, productId, cityId);

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/campaign/new")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        response.then()
                .statusCode(200)
                .body("keywords", hasSize(1))
                .body("keywords[0]", equalTo(uniqueCampaignName ));
    }

    @Test
    public void addCampaignWithInvalidJson_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String invalidJson = "{ \"name\": \"Test\", \"product\": \"123\", ";

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(invalidJson)
                .when()
                .post("/campaign/new")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void updateCampaignWithNonExistingId_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);
        String id = "00000000-0000-0000-0000-000000000000";

        String updatePayload = """
            {
              "name": "NonExistent",
              "description": "desc",
              "product": "some-product-id",
              "keywords": ["test"],
              "bid_amount": "1.50",
              "campaign_amount": "100.00",
              "city": "some-city-id",
              "radius": "10.0"
            }
            """;

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .patch("/campaign/update/" + id)
                .then()
                .statusCode(400);
    }

    @Test
    public void deleteCampaign_shouldReturnNoContentAndThenNotFoundOnGet() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\"]";

        Response createResponse = createCampaign(
                uniqueCampaignName,
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI,
                token
        );

        String campaignId = createResponse.jsonPath().getString("id");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/campaign/delete/" + campaignId)
                .then()
                .statusCode(204);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/campaign/" + campaignId)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void updateCampaignWithInvalidRadius_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String uniqueCityName = "City_" + System.currentTimeMillis();
        String uniqueProductName = "Product_" + System.currentTimeMillis();
        String uniqueCampaignName = "Campaign_" + System.currentTimeMillis();

        Response cityResponse = CityTests.createCity(uniqueCityName, 52.2297, 21.0122, 200, RestAssured.baseURI);
        String cityId = cityResponse.jsonPath().getString("id");

        Response productResponse = ProductTests.createProduct(uniqueProductName, "desc", 200, RestAssured.baseURI, token);
        String productId = productResponse.jsonPath().getString("id");

        String keywords = "[\"java\", \"test\"]";

        Response createResponse = createCampaign(
                uniqueCampaignName,
                "desc",
                productId,
                keywords,
                "1.50",
                "100.00",
                cityId,
                "10.0",
                200,
                RestAssured.baseURI,
                token
        );

        String campaignId = createResponse.jsonPath().getString("id");

        String updatePayload = String.format("""
        {
          "name": "%s_Updated",
          "description": "Updated desc",
          "product": "%s",
          "keywords": ["updated", "keywords"],
          "bid_amount": "2.00",
          "campaign_amount": "200.00",
          "city": "%s",
          "radius": "-5.0"
        }
        """, uniqueCampaignName, productId, cityId);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .patch("/campaign/update/" + campaignId)
                .then()
                .statusCode(400)
                .log().body();
    }

    @Test
    public void addCampaignWithInvalidBidAmount_shouldReturnBadRequest() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String cityId = CityTests.createCity("City_" + System.currentTimeMillis(), 50.2, 20.1, 200, RestAssured.baseURI)
                .jsonPath().getString("id");

        String productId = ProductTests.createProduct("Product_" + System.currentTimeMillis(), "desc", 200, RestAssured.baseURI, token)
                .jsonPath().getString("id");

        String payload = String.format("""
        {
            "name": "InvalidBid",
            "description": "desc",
            "product": "%s",
            "keywords": ["test"],
            "bid_amount": "abc",
            "campaign_amount": "100.00",
            "city": "%s",
            "radius": "10.0"
        }
        """, productId, cityId);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/campaign/new")
                .then()
                .statusCode(400)
                .log().body();
    }

    @Test
    public void updateCampaignNameOnly_shouldReturnOk() {

        String token = AuthTests.getLoginToken("admin@example.com","password",authURI);
        AuthTests.validation(token,200,authURI);

        String cityId = CityTests.createCity("City_" + System.currentTimeMillis(), 50.1, 19.9, 200, RestAssured.baseURI)
                .jsonPath().getString("id");

        String productId = ProductTests.createProduct("Product_" + System.currentTimeMillis(), "desc", 200, RestAssured.baseURI, token)
                .jsonPath().getString("id");

        String name = "Campaign_" + System.currentTimeMillis();

        Response created = createCampaign(
                name,
                "desc",
                productId,
                "[\"keyword\"]",
                "1.0",
                "50.0",
                cityId,
                "5.0",
                200,
                RestAssured.baseURI,
                token
        );

        String campaignId = created.jsonPath().getString("id");

        String updatePayload = """
        {
            "name": "UpdatedCampaignName"
        }
        """;

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .patch("/campaign/update/" + campaignId)
                .then()
                .statusCode(200)
                .log().body();
    }
}
