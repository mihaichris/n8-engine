package com.example.n8engine.searcher.impl;

import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.searcher.Searcher;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Qualifier("searcher")
public class SearcherImpl implements Searcher {

    private final Dataset dataset;

    public SearcherImpl(Environment environment) throws IOException {
        Path assemblerPath = Paths.get(Objects.requireNonNull(environment.getProperty("jena.resource.assembler-lucene")));
        String assemblerAbsolutPath = assemblerPath.toAbsolutePath().toString();
        String resourceURI = environment.getProperty("jena.resource.offers-uri");
        Objects.requireNonNull(resourceURI, "resourceURI can not be null");

        this.dataset = DatasetFactory.assemble(assemblerAbsolutPath, resourceURI);
    }

    public Dataset getDataset() {
        return dataset;
    }

    public ArrayList<Entity> getEntitiesBySearchQuery(String query) {
        // TODO Implementare offers
        return new ArrayList<Entity>();
    }

    public Entity findEntityByUID(String UID) {
        // TODO Implementare offers
        return new Entity("", new ArrayList<Value>());
    }
}
