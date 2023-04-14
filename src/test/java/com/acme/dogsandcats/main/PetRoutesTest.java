package com.acme.dogsandcats.main;

import com.acme.dogsandcats.generated.model.Pet;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
//@TestProfile(PetRoutesTest.class)
public class PetRoutesTest {//extends CamelQuarkusTestSupport {
    public PetRoutesTest() {
        super();
    }

    @EndpointInject(value = "direct:addPet")
    private ProducerTemplate addPetEndpoint;
    @EndpointInject(value = "amqp:queue:incoming")
    private ProducerTemplate incomingEndpoint;
    @EndpointInject(value = "mock:outgoing")
    private MockEndpoint outgoingEndpoint;

    @Test
    void testAddViaRestAssured() throws IOException {
        JsonObject jsonObject = getDoggieJson();

        Response response = given().contentType(ContentType.JSON)
                .body(jsonObject.toString())
                .post("/v3/pet");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    void testAdd() throws IOException {
        JsonObject jsonObject = getDoggieJson();

        String response = addPetEndpoint.requestBody(IOUtils.toInputStream(jsonObject.toString(), Charset.defaultCharset()), String.class);

        assertThat(response, notNullValue());

        JsonObject jsonObj = new JsonObject(response);
        Pet pet = jsonObj.mapTo(Pet.class);

        assertThat(pet, notNullValue());
        assertThat(pet.getId(), notNullValue());
    }

    @Test
    void testIncoming() throws IOException, InterruptedException {
        JsonObject jsonObject = getDoggieJson();

        String addResponse = addPetEndpoint.requestBody(IOUtils.toInputStream(jsonObject.toString(), Charset.defaultCharset()), String.class);
        incomingEndpoint.sendBody(addResponse);

        outgoingEndpoint.expectedMessageCount(1);
        outgoingEndpoint.expectedBodiesReceived(addResponse);
        outgoingEndpoint.assertIsSatisfied(TimeUnit.SECONDS.toMillis(5));
    }

    private JsonObject getDoggieJson() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
        String jsonStr = IOUtils.toString(new FileReader(file));

        return new JsonObject(jsonStr);
    }
}