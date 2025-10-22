package co.edu.uniquindio.ingesis.notifications_service.steps;

import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AssertionsSteps {

    private final World world;

    public AssertionsSteps(World world) { this.world = world; }

    @Then("la respuesta debe tener código {int}")
    public void laRespuestaDebeTenerCodigo(int status) {
        world.getResponse().statusCode(status);
    }

    @Then("el cuerpo debe contener el campo {string}")
    public void elCuerpoDebeContenerElCampo(String field) {
        world.getResponse().body(field, notNullValue());
    }

    @Then("el cuerpo debe contener el campo {string} con el mismo valor")
    public void elCuerpoDebeContenerElCampoConElMismoValor(String field) {
        String value = world.getResponse().extract().path(field);
        assertThat("El campo no coincide con el id guardado",
                value, equalTo(world.getNotificationId()));
    }

    @Then("el campo {string} debe ser {string}")
    public void elCampoDebeSer(String field, String expected) {
        world.getResponse().body(field, equalTo(expected));
    }

    @Then("el campo {string} debe existir")
    public void elCampoDebeExistir(String field) {
        world.getResponse().body("$", hasKey(field));
    }

    @Then("el cuerpo debe contener una página de resultados")
    public void elCuerpoDebeContenerUnaPaginaDeResultados() {
        world.getResponse()
                .body(matchesJsonSchemaInClasspath("schemas/notification-page.json"));
    }

    @Then("todos los elementos deben tener {string} = {string}")
    public void todosLosElementosDebenTenerIgual(String field, String expected) {
        String body = world.getResponse().extract().asString();
        JsonPath jp = new JsonPath(body);

        // Ajusta si tu paginación devuelve "content" o "items"
        List<Map<String, Object>> content = jp.getList("content");
        assertThat("La respuesta no trae 'content'", content, is(notNullValue()));

        for (Map<String, Object> item : content) {
            Object value = item.get(field);
            assertThat("Elemento no cumple " + field + "=" + expected,
                    String.valueOf(value), equalTo(expected));
        }
    }
}
