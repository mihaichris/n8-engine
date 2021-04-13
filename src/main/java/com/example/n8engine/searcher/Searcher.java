package com.example.n8engine.searcher;

import com.example.n8engine.dto.SearchRequest;
import com.example.n8engine.model.Entity;
import org.apache.jena.query.Dataset;

import java.util.Set;

public interface Searcher {
    Dataset getDataset();
    Entity findEntityByURI(String URI);
    Set<Entity> getEntitiesBySearchQuery(SearchRequest searchRequest);
}
