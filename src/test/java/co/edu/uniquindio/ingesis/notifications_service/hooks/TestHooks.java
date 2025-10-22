package co.edu.uniquindio.ingesis.notifications_service.hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class    TestHooks {
    @Before
    public void setup() {
        String baseUrl = System.getProperty("BASE_URL", "http://localhost:8081"); // ajusta puerto/host
        RestAssured.baseURI = baseUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }
}
