package com.example.n8engine.reponse;

import com.example.n8engine.model.Entity;

import java.util.ArrayList;

public class SearchResponse {
    private ArrayList<Entity> entities;

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }
}
