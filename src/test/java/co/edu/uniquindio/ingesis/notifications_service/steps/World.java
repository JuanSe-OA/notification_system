package co.edu.uniquindio.ingesis.notifications_service.steps;

import io.restassured.response.ValidatableResponse;

public class World {
    private String baseUrl;
    private String bearer;
    private String expectedSub;
    private String notificationId;
    private ValidatableResponse response;

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getBearer() { return bearer; }
    public void setBearer(String bearer) { this.bearer = bearer; }

    public String getExpectedSub() { return expectedSub; }
    public void setExpectedSub(String expectedSub) { this.expectedSub = expectedSub; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public ValidatableResponse getResponse() { return response; }
    public void setResponse(ValidatableResponse response) { this.response = response; }
}
