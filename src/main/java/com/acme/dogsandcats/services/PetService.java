package com.acme.dogsandcats.services;

import com.acme.dogsandcats.generated.model.Pet;
import org.apache.camel.Header;

public interface PetService {

    Pet update(Pet pet);

    Pet add(Pet pet);

    Pet get(@Header("petId") Long id);
}
