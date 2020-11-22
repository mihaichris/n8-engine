package com.example.n8engine.searcher;

import com.example.n8engine.model.Entity;
import org.apache.jena.query.Dataset;

import java.util.ArrayList;

public interface Searcher {
    public Dataset getDataset();
    public Entity findEntityByUID(String UID);
    public ArrayList<Entity> getEntitiesBySearchQuery(String query);
}
