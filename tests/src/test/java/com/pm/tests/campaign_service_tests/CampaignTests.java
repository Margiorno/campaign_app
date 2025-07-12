package com.pm.tests.campaign_service_tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class CampaignTests {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:9000";
    }






}
