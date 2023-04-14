package com.acme.dogsandcats.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Category extends PanacheEntity {

    public String name;

    public static Category findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Category findByName(String name) {
        return find("name", name).firstResult();
    }
}

