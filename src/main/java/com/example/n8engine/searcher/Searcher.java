package com.example.n8engine.searcher;

import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.model.Entity;
import org.apache.jena.query.Dataset;

import java.util.Set;

public interface Searcher {
    Dataset getDataset();
    Entity findEntityByURI(String UID);
    Set<Entity> getEntitiesBySearchQuery(SearchType searchType, String searchQuery);
}
