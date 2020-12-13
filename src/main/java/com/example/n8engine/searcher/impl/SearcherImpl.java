package com.example.n8engine.searcher.impl;

import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.exception.SearchTypeNotFoundException;
import com.example.n8engine.model.Attribute;
import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.query.QueryInterface;
import com.example.n8engine.query.SearchQueryFactory;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Service
@Slf4j
@Qualifier("searcher")
public class SearcherImpl implements Searcher {

    private final Dataset dataset;

    public SearcherImpl(Environment environment) throws IOException {
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
            QueryInterface queryFactory = SearchQueryFactory.create(searchType, searchQuery);
            Query query = queryFactory.getQuery();
            QueryExecution queryExecution = QueryExecutionFactory.create(query, this.getDataset());
            ResultSet results = queryExecution.execSelect();
            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String entityName = solution.get("entity").toString();
                String attributeName = solution.get("attribute").toString();
                String valueName = solution.get("value").toString();
                String score = solution.get("score").toString();
                RDFNode graph = solution.get("graph");
                if (containsName(entities, entityName)) {
                    Entity entity = findByNameInList(entities, entityName);
                    entities.remove(entity);
                    Attribute attribute = new Attribute(attributeName);
                    Value value = new Value(valueName, attribute);
                    entity.addValue(value);
                    entities.add(entity);
                    continue;
                }
                Entity entity = new Entity();
                Attribute attribute = new Attribute(attributeName);
                Value value = new Value(valueName, attribute);
                entity.addValue(value);
                entity.setGraph(graph);
                entity.setScore(score);
                entities.add(entity);
            }
            queryExecution.close();
        } catch (SearchTypeNotFoundException e) {
            log.info("Search Type not found: " + e.getMessage());
        }
        return entities;
    }

    public Entity findEntityByURI(String UID) {
        // TODO Implementare offers
        return null;
    }

    private Boolean containsName(final Set<Entity> entities, final String name) {
        return entities.stream().map(Entity::getName).anyMatch(name::equals);
    }

    private Entity findByNameInList(final Set<Entity> entities, final String name) {
        return entities.stream().filter(entity -> name.equals(entity.getName())).findFirst().orElse(null);
    }
}
