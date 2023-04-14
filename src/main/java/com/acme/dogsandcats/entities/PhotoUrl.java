package com.acme.dogsandcats.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class PhotoUrl extends PanacheEntity {

    public String name;

    public static PhotoUrl findById(Long id){
        return find("id", id).firstResult();
    }

    public static PhotoUrl findByName(String name){
        return find("name", name).firstResult();
    }
}

