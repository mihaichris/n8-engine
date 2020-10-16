package com.example.n8engine.service.impl;

import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.service.JenaService;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class JenaServiceImpl implements JenaService {
    private final Environment environment;

    private final Dataset dataset;

    public JenaServiceImpl(Environment environment) {
        this.environment = environment;
        String filename = this.environment.getProperty("jena.resource.filename");
        String resourceURI = this.environment.getProperty("jena.resource-uri");
        Objects.requireNonNull(filename, "file name can not be null") ;
        Objects.requireNonNull(resourceURI, "resourceURI can not be null") ;
        this.dataset = DatasetFactory.assemble(filename, resourceURI);
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
        return new Entity(new ArrayList<Value>(), "");
    }
}
