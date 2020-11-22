package com.example.n8engine.dto;

import com.example.n8engine.model.Entity;

import java.util.ArrayList;

final public class SearchResponse {
    private final ArrayList<Entity> entities;

    public SearchResponse(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
