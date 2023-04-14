package com.acme.dogsandcats.main;

import com.acme.dogsandcats.generated.model.Pet;
import com.acme.dogsandcats.services.PetService;
import io.quarkus.arc.Priority;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.vertx.core.json.JsonObject;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
//@TestProfile(PetRoutesAdviceWithTest.class)
public class PetRoutesAdviceWithTest {//extends CamelQuarkusTestSupport {

    public PetRoutesAdviceWithTest() {
        super();
    }

    @EndpointInject(value = "direct:getPetById")
    private ProducerTemplate endpoint;

   // @Override
   // public boolean isUseAdviceWith() {
   //     return true; // turn on advice with
   // }

    /**
     * @BeforeEach public void setup() throws Exception {
     * doPreSetup();
     * <p>
     * AdviceWith.adviceWith(context, "directGetPetById", a ->
     * a.weaveById("beanGet")
     * .replace()
     * .log("replaced...")
     * );
     * <p>
     * context.start();
     * startRouteDefinitions();
     * }
     **/

    @Test
    void testGet() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
        String jsonStr = IOUtils.toString(new FileReader(file));
        JsonObject jsonObject = new JsonObject(jsonStr);

        InputStream response = (InputStream)endpoint.requestBodyAndHeader(IOUtils.toInputStream(jsonObject.toString(), Charset.defaultCharset()), "petId", 100);

        assertThat(response, notNullValue());

        JsonObject jsonObj = new JsonObject(IOUtils.toString(response, Charset.defaultCharset()));
        Pet pet = jsonObj.mapTo(Pet.class);

        assertThat(pet, notNullValue());
        assertThat(pet.getId(), notNullValue());
    }
}

