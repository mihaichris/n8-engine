package com.example.n8engine.searcher.impl;

import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.exception.SearchTypeNotFoundException;
import com.example.n8engine.model.Attribute;
import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.query.QueryInterface;
import com.example.n8engine.query.SearchQueryFactory;
import com.example.n8engine.searcher.Searcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Service
@Slf4j
@Transactional
@Qualifier("searcher")
public class SearcherImpl implements Searcher {

    private final Dataset dataset;
    private final SearchQueryFactory searchQueryFactory;

    public SearcherImpl(Environment environment, SearchQueryFactory searchQueryFactory) {
        this.searchQueryFactory = searchQueryFactory;
        Path assemblerPath = Paths.get(Objects.requireNonNull(environment.getProperty("jena.resource.assembler-lucene")));
        String assemblerAbsolutPath = assemblerPath.toAbsolutePath().toString();
        String resourceURI = environment.getProperty("jena.resource.uri");
        Objects.requireNonNull(resourceURI, "resourceURI can not be null");
        this.dataset = DatasetFactory.assemble(assemblerAbsolutPath, resourceURI);
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Set<Entity> getEntitiesBySearchQuery(SearchType searchType, String searchQuery) {
        Set<Entity> entities = new HashSet<>();
        try {
            QueryInterface queryFactory = searchQueryFactory.create(searchType);
            Query query = queryFactory.search(searchQuery);
            try ( QueryExecution queryExecution = QueryExecutionFactory.create(query, this.getDataset())) {
                ResultSet results = queryExecution.execSelect();
                while (results.hasNext()) {
                    QuerySolution solution = results.nextSolution();
                    String entityName = solution.get("entity").toString();
                    String attributeName = solution.get("attribute").toString();
                    String valueName = solution.get("literal").toString();
                    String score = solution.get("score").toString();
                    String graph = solution.getResource("graph").toString();
                    if (containsName(entities, entityName)) {
                        Entity entity = findByNameInList(entities, entityName);
                        entities.remove(entity);
                        Attribute attribute = new Attribute(attributeName);
                        Value value = new Value(valueName, attribute);
                        entity.addValue(value);
                        entities.add(entity);
                        continue;
                    }
                    Entity entity = new Entity(entityName);
                    Attribute attribute = new Attribute(attributeName);
                    Value value = new Value(valueName, attribute);
                    entity.addValue(value);
                    entity.setGraph(graph);
                    entity.setScore(score);
                    entities.add(entity);
                }
            }
        } catch (SearchTypeNotFoundException e) {
            log.error("Search Type not found: " + e.getMessage());
        }
        return entities;
    }

    public Entity findEntityByURI(String UID) {
        // TODO Implementare offers
        return null;
    }

    private Boolean containsName(final Set<Entity> entities, final String name) {
        return entities.stream().map(Entity::getId).anyMatch(name::equals);
    }

    private Entity findByNameInList(final Set<Entity> entities, final String name) {
        return entities.stream().filter(entity -> name.equals(entity.getId())).findFirst().orElse(null);
    }

    private ObjectMapper registerObjectMapper(ObjectMapper objectMapper)  {
        objectMapper.registerModule(new JsonldModule());
        return objectMapper;
    }
}
