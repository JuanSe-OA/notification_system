package co.edu.uniquindio.ingesis.notifications_service.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class NotificationSteps {

    private final World world;
    private String bearer;

    public NotificationSteps(World world) {
        this.world = world;
        this.bearer = System.getProperty("TOKEN", "");
    }

    @Given("la API base está configurada")
    public void apiBaseConfigurada() { /* Hook lo hace */ }

    @When("creo una notificación {string} para el destinatario {string} con el mensaje {string}")
    public void creoNotificacion(String type, String to, String message) {
        ValidatableResponse resp = given()
                .spec(new RequestSpecBuilder()
                        .setContentType("application/json")
                        .build())
                .body("""
                  {
                    "type":"%s",
                    "recipient":"%s",
                    "message":"%s"
                  }
                  """.formatted(type, to, message))
                .when()
                .post("/api/notifications")
                .then();

        world.setResponse(resp);
        // Si fue 201, extraemos ID para usar luego
        try {
            String id = resp.extract().path("id");
            if (id != null) world.setNotificationId(id);
        } catch (Exception ignored) {}
    }

    @Given("tengo un id de notificación válido")
    public void tengoIdValido() {
        if (world.getNotificationId() == null) {
            // Fallback: crea una notificación rápida
            ValidatableResponse resp = given()
                    .contentType("application/json")
                    .body("""
                      {"type":"EMAIL","recipient":"user@test.com","message":"seed"}
                      """)
                    .post("/api/notifications").then().statusCode(201);
            String id = resp.extract().path("id");
            world.setNotificationId(id);
        }
    }

    @When("consulto la notificación por id")
    public void consultoNotificacionPorId() {
        ValidatableResponse resp = given()
                .get("/api/notifications/{id}", world.getNotificationId())
                .then();
        world.setResponse(resp);
    }

    @Given("envío el token de acceso")
    public void envioToken() {
        // No-op: solo marca que hay token. Se usa en el siguiente step si bearer no está vacío.
    }

    @When("consulto el historial de notificaciones paginado page={int} size={int}")
    public void consultoHistorialPaginado(int page, int size) {
        ValidatableResponse resp = given()
                .header("Authorization", bearer.isBlank() ? "" : "Bearer " + bearer)
                .get("/api/notifications/history?page={page}&size={size}", page, size)
                .then();
        world.setResponse(resp);
    }

    @When("consulto el historial de notificaciones paginado page={int} size={int} sin token")
    public void consultoHistorialSinToken(int page, int size) {
        ValidatableResponse resp = given()
                .get("/api/notifications/history?page={page}&size={size}", page, size)
                .then();
        world.setResponse(resp);
    }

    @When("guardo el {string} de la notificación")
    public void guardoCampo(String field) {
        String id = world.getResponse().extract().path(field);
        world.setNotificationId(id);
    }
}
