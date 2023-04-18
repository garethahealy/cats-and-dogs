package com.acme.dogsandcats.main;

import com.acme.dogsandcats.OnlyGetPetService;
import com.acme.dogsandcats.generated.model.Pet;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.vertx.core.json.JsonObject;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestProfile(PetRoutesAdviceWithTest.class)
public class PetRoutesAdviceWithTest extends CamelQuarkusTestSupport {
    @EndpointInject(value = "direct:getPetById")
    private ProducerTemplate endpoint;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    private void setupAdviceWith() throws Exception {
        AdviceWith.adviceWith(context, "directGetPetById", a ->
                a.weaveById("beanGet")
                        .replace()
                        .bean(new OnlyGetPetService(), "get")
        );
        
        context.start();
    }

    @Test
    void testGet() throws Exception {
        setupAdviceWith();

        InputStream response = (InputStream) endpoint.requestBodyAndHeader("", "petId", 100);

        assertThat(response, notNullValue());

        JsonObject jsonObj = new JsonObject(IOUtils.toString(response, Charset.defaultCharset()));
        Pet pet = jsonObj.mapTo(Pet.class);

        assertThat(pet, notNullValue());
        assertThat(pet.getId(), notNullValue());
    }
}

