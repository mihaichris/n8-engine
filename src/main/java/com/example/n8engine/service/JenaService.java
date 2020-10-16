package com.example.n8engine.service;

import com.example.n8engine.model.Entity;
import org.apache.jena.query.Dataset;

import java.util.ArrayList;

public interface JenaService {
    public Dataset getDataset();
    public Entity findEntityByUID(String UID);
    public ArrayList<Entity> getEntitiesBySearchQuery(String query);
}
