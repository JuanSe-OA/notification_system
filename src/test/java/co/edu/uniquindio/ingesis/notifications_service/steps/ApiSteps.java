package co.edu.uniquindio.ingesis.notifications_service.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    private final World world;

    public ApiSteps(World world) {
        this.world = world;
    }

    @Given("la API base de notificaciones está disponible en {string}")
    public void laAPIBaseDeNotificacionesEstaDisponibleEn(String baseUrl) {
        world.setBaseUrl(baseUrl);
        RestAssured.baseURI = baseUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Given("poseo un token JWT válido con subject {string}")
    public void poseoUnTokenJWTVálidoConSubject(String subject) {
        // Usamos el TOKEN que llega por -DTOKEN o por variables de entorno
        String token = System.getProperty("TOKEN", System.getenv().getOrDefault("TOKEN", ""));
        world.setBearer(token);
        world.setExpectedSub(subject);

        // (Opcional) Validamos que el payload contenga el sub esperado sin verificar firma
        try {
            String payload = token.split("\\.")[1];
            String json = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            if (!json.contains("\"sub\":\"" + subject + "\"")) {
                System.out.println("[WARN] El JWT no contiene el subject esperado: " + subject);
            }
        } catch (Exception ignored) { }
    }

    @When("envío POST a {string} con el JSON:")
    public void envioPOSTAConElJSON(String path, String body) {
        path = resolvePath(path);
        ValidatableResponse resp = given()
                .spec(new RequestSpecBuilder().setContentType("application/json").build())
                .header("Authorization", "Bearer " + world.getBearer())
                .body(body)
                .when().post(path)
                .then();

        world.setResponse(resp);

        // Intentamos capturar y guardar el id
        try {
            String id = resp.extract().path("id");
            if (id != null) world.setNotificationId(id);
        } catch (Exception ignored) {}
    }

    @Given("que ya creé una notificación y guardé su {string}")
    public void queYaCreeUnaNotificacionYGuardeSu(String field) {
        if (world.getNotificationId() != null) return;

        String seedBody = """
            {
              "recipient": "%s",
              "channel": "EMAIL",
              "title": "Seed",
              "message": "seed"
            }
            """.formatted(world.getExpectedSub() == null ? "user@test.com" : world.getExpectedSub());

        ValidatableResponse resp = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + world.getBearer())
                .body(seedBody)
                .post("/api/notifications")
                .then().statusCode(200);

        String id = resp.extract().path(field);
        world.setNotificationId(id);
    }

    @When("envío GET a {string}")
    public void envioGETA(String path) {
        path = resolvePath(path);
        ValidatableResponse resp = given()
                .header("Authorization", "Bearer " + world.getBearer())
                .get(path)
                .then();
        world.setResponse(resp);
    }

    @When("envío GET a {string} SIN encabezado Authorization")
    public void envioGETASinHeaderAuth(String path) {
        path = resolvePath(path);
        ValidatableResponse resp = given()
                .get(path)
                .then();
        world.setResponse(resp);
    }

    private String resolvePath(String path) {
        if (path.contains("{id}") && world.getNotificationId() != null) {
            path = path.replace("{id}", world.getNotificationId());
        }
        return path;
    }
}
