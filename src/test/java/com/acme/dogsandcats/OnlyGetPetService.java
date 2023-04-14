package com.acme.dogsandcats;

import com.acme.dogsandcats.generated.model.Pet;
import com.acme.dogsandcats.services.DefaultPetService;
import com.acme.dogsandcats.services.PetService;
import io.quarkus.arc.Priority;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Alternative
@Priority(1)
@ApplicationScoped
public class OnlyGetPetService implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(OnlyGetPetService.class);

    @Inject
    DefaultPetService defaultPetService;

    @Override
    public Pet update(Pet pet) {
        logger.info("update");

        return defaultPetService.update(pet);
    }

    @Override
    public Pet add(Pet pet) {
        logger.info("add");

        return defaultPetService.add(pet);
    }

    @Override
    public Pet get(Long id) {
        logger.info("get");

        if (id == 100) {
            File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
            String jsonStr;

            try {
                jsonStr = IOUtils.toString(new FileReader(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JsonObject jsonObject = new JsonObject(jsonStr);
            Pet pet = jsonObject.mapTo(Pet.class);
            pet.setId(100L);
            pet.setName("OnlyGetPetService");

            return pet;
        } else {
            return defaultPetService.get(id);
        }
    }
}
