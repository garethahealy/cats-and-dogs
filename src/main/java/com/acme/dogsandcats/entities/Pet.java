package com.acme.dogsandcats.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet extends PanacheEntity {

    public enum StatusEnum {
        AVAILABLE("available"),
        PENDING("pending"),
        SOLD("sold");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public String name;

    @OneToOne
    public Category category;

    @ManyToMany
    public List<PhotoUrl> photoUrls = new ArrayList<>();

    @ManyToMany
    public List<Tag> tags = new ArrayList<>();

    public StatusEnum status;

    public static Pet findById(Long id) {
        return find("id", id).firstResult();
    }
}

