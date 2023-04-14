package com.acme.dogsandcats.services;

import com.acme.dogsandcats.entities.PhotoUrl;
import com.acme.dogsandcats.entities.Tag;
import com.acme.dogsandcats.generated.model.Category;
import com.acme.dogsandcats.generated.model.Pet;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Transactional
    public Pet update(Pet pet) {
        com.acme.dogsandcats.entities.Pet petEntity = com.acme.dogsandcats.entities.Pet.findById(pet.getId());
        com.acme.dogsandcats.entities.Category categoryEntity = com.acme.dogsandcats.entities.Category.findByName(pet.getCategory().getName());
        if (categoryEntity == null) {
            categoryEntity = new com.acme.dogsandcats.entities.Category();
            categoryEntity.name = pet.getCategory().getName();
            categoryEntity.persist();
        }

        petEntity.name = pet.getName();
        petEntity.photoUrls = convertPhotoUrlsAndPersist(pet.getPhotoUrls());
        petEntity.category = categoryEntity;
        petEntity.status = com.acme.dogsandcats.entities.Pet.StatusEnum.fromValue(pet.getStatus().getValue());
        petEntity.tags = convertTagsAndPersist(pet.getTags());

        petEntity.persist();

        pet.getCategory().id(petEntity.category.id);
        pet.setTags(convertTagsBack(petEntity.tags));

        return pet;
    }

    @Transactional
    public Pet add(Pet pet) {
        com.acme.dogsandcats.entities.Pet petEntity = new com.acme.dogsandcats.entities.Pet();
        com.acme.dogsandcats.entities.Category categoryEntity = com.acme.dogsandcats.entities.Category.findByName(pet.getCategory().getName());
        if (categoryEntity == null) {
            categoryEntity = new com.acme.dogsandcats.entities.Category();
            categoryEntity.name = pet.getCategory().getName();
            categoryEntity.persist();
        }

        petEntity.name = pet.getName();
        petEntity.photoUrls = convertPhotoUrlsAndPersist(pet.getPhotoUrls());
        petEntity.category = categoryEntity;
        petEntity.status = com.acme.dogsandcats.entities.Pet.StatusEnum.fromValue(pet.getStatus().getValue());
        petEntity.tags = convertTagsAndPersist(pet.getTags());

        petEntity.persist();

        pet.setId(petEntity.id);
        pet.getCategory().id(petEntity.category.id);
        pet.setTags(convertTagsBack(petEntity.tags));

        return pet;
    }

    @Transactional
    public Pet get(@Header("petId") Long id) {
        com.acme.dogsandcats.entities.Pet entity = com.acme.dogsandcats.entities.Pet.findById(id);
        if (entity == null) {
            throw new NullPointerException(id + " not found for Pet.");
        }

        return new Pet().id(entity.id)
                .name(entity.name)
                .photoUrls(convertPhotoUrlsBack(entity.photoUrls))
                .category(new Category().id(entity.category.id).name(entity.category.name))
                .status(Pet.StatusEnum.fromValue(entity.status.getValue()))
                .tags(convertTagsBack(entity.tags));
    }

    private List<PhotoUrl> convertPhotoUrlsAndPersist(List<String> urls) {
        List<PhotoUrl> answer = new ArrayList<>();
        for (String url : urls) {
            PhotoUrl photoUrl = PhotoUrl.findByName(url);
            if (photoUrl == null) {
                photoUrl = new PhotoUrl();
            }

            photoUrl.name = url;
            photoUrl.persist();

            answer.add(photoUrl);
        }

        return answer;
    }

    private List<Tag> convertTagsAndPersist(List<com.acme.dogsandcats.generated.model.Tag> tags) {
        List<Tag> answer = new ArrayList<>();
        for (com.acme.dogsandcats.generated.model.Tag tag : tags) {
            Tag tagEntity = Tag.findById(tag.getId());
            if (tagEntity == null) {
                tagEntity = Tag.findByName(tag.getName());
                if (tagEntity == null) {
                    tagEntity = new Tag();
                }
            }

            if (!tag.getName().equalsIgnoreCase(tagEntity.name)) {
                tagEntity.name = tag.getName();
                tagEntity.persist();
            }

            answer.add(tagEntity);
        }

        return answer;
    }

    private List<com.acme.dogsandcats.generated.model.Tag> convertTagsBack(List<Tag> tags) {
        List<com.acme.dogsandcats.generated.model.Tag> answer = new ArrayList<>();
        for (Tag tag : tags) {
            com.acme.dogsandcats.generated.model.Tag converted = new com.acme.dogsandcats.generated.model.Tag();
            converted.id(tag.id);
            converted.name(tag.name);

            answer.add(converted);
        }

        return answer;
    }

    private List<String> convertPhotoUrlsBack(List<PhotoUrl> urls) {
        List<String> answer = new ArrayList<>();
        for (PhotoUrl url : urls) {
            answer.add(url.name);
        }

        return answer;
    }
}
