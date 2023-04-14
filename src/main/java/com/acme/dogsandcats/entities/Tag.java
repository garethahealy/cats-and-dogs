package com.acme.dogsandcats.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Tag extends PanacheEntity {

    public String name;

    public static Tag findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Tag findByName(String name) {
        return find("name", name).firstResult();
    }
}

