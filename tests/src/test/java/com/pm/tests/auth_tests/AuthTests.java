package com.pm.tests.auth_tests;

import com.pm.tests.campaign_service_tests.CityTests;
import com.pm.tests.campaign_service_tests.ProductTests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthTests {
    private static final Log log = LogFactory.getLog(AuthTests.class);

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:10000/auth";
    }

    public static String getLoginToken(String email, String password, String baseUri) {
        String payload = String.format("""
        {
          "email": "%s",
          "password": "%s"
        }
        """, email, password);

        return RestAssured.given()
                .baseUri(baseUri)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    public static void validation(String loginToken, int exceptedStatus, String baseUri) {
        RestAssured.given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get("/validate")
                .then()
                .log().all()
                .statusCode(exceptedStatus);
    }

    public static String getRegisterToken(String email, String password, String baseUri) {
        String payload = String.format("""
        {
          "email": "%s",
          "password": "%s"
        }
        """, email, password);

        return RestAssured.given()
                .baseUri(baseUri)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    public void addUser_shouldReturnOkRequest(){

        String uniqueEmail = "user_" + System.currentTimeMillis() + "@test.com";
        String password = "password";

        String registerToken = getRegisterToken(uniqueEmail, password, RestAssured.baseURI);

        Assertions.assertNotNull(registerToken);
        Assertions.assertFalse(registerToken.isBlank());



        validation(registerToken,200, RestAssured.baseURI);


        String loginToken = getLoginToken(uniqueEmail, password, RestAssured.baseURI);

        Assertions.assertNotNull(registerToken);
        Assertions.assertFalse(registerToken.isBlank());

        validation(loginToken,200, RestAssured.baseURI);
    }



}
