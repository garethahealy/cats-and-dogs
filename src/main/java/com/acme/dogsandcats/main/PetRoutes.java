package com.acme.dogsandcats.main;

import com.acme.dogsandcats.generated.model.Pet;
import com.acme.dogsandcats.services.PetService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PetRoutes extends RouteBuilder {

    @Inject
    PetService petService;

    @Override
    public void configure() throws Exception {

        from("direct:updatePet")
                .unmarshal().json(JsonLibrary.Jackson, Pet.class)
                .log("hit -> updatePet - ${body}")
                .bean(petService, "update")
                .marshal().json(JsonLibrary.Jackson);

        from("direct:addPet")
                .unmarshal().json(JsonLibrary.Jackson, Pet.class)
                .log("hit -> addPet - ${body}")
                .bean(petService, "add")
                .marshal().json(JsonLibrary.Jackson);

        from("direct:getPetById")
                .log("hit -> getPetById - ${headers.petId}")
                .bean(petService, "get")
                .marshal().json(JsonLibrary.Jackson);

        from("amqp:queue:incoming")
                .unmarshal().json(JsonLibrary.Jackson, Pet.class)
                .log("hit -> incoming - ${body}")
                .setHeader("petId", simple("${body.getId()}"))
                .bean(petService, "get")
                .marshal().json(JsonLibrary.Jackson)
                .to("amqp:queue:outgoing");

        from("amqp:queue:outgoing")
                .unmarshal().json(JsonLibrary.Jackson, Pet.class)
                .log("hit -> outgoing - ${body}");
    }
}
