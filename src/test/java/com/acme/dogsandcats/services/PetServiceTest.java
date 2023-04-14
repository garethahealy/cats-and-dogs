package com.acme.dogsandcats.services;

import com.acme.dogsandcats.generated.model.Pet;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class PetServiceTest {
    @Inject
    DefaultPetService petService;

    @Test
    void update() throws IOException {
        Pet pet = addPet();
        Pet addAnswer = petService.add(pet);
        addAnswer.setStatus(Pet.StatusEnum.SOLD);

        Pet updateAnswer = petService.update(addAnswer);

        assertThat(updateAnswer, notNullValue());
        assertThat(updateAnswer.getId(), notNullValue());
        assertThat(pet.getId(), equalTo(updateAnswer.getId()));
        assertThat(addAnswer.getStatus(), equalTo(updateAnswer.getStatus()));
    }

    @Test
    void testAdd() throws IOException {
        Pet answer = petService.add(addPet());

        assertThat(answer, notNullValue());
        assertThat(answer.getId(), notNullValue());
    }

    @Test
    void testGet() throws IOException {
        Pet pet = addPet();
        Pet addAnswer = petService.add(pet);

        Pet answer = petService.get(addAnswer.getId());

        assertThat(answer, notNullValue());
        assertThat(answer.getId(), notNullValue());
        assertThat(pet.getId(), equalTo(answer.getId()));
    }

    private Pet addPet() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
        String jsonStr = IOUtils.toString(new FileReader(file));
        JsonObject jsonObj = new JsonObject(jsonStr);

        return jsonObj.mapTo(Pet.class);
    }
}