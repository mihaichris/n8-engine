package com.example.n8engine.dto;

import com.example.n8engine.model.Entity;

import java.util.Set;

final public class SearchResponse {
    private final Set<Entity> entities;

    public SearchResponse( Set<Entity>  entities) {
        this.entities = entities;
    }

    public  Set<Entity>  getEntities() {
        return entities;
    }
}
