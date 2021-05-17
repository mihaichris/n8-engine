package com.example.n8engine.searcher.impl;

import com.example.n8engine.domain.Searches;
import com.example.n8engine.dto.SearchRequest;
import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.exception.SearchTypeNotFoundException;
import com.example.n8engine.model.Attribute;
import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.query.QueryInterface;
import com.example.n8engine.query.ResourceQuery;
import com.example.n8engine.query.SearchQueryFactory;
import com.example.n8engine.repository.SearchesRepository;
import com.example.n8engine.searcher.Searcher;
import com.example.n8engine.service.LanguageDetectorService;
import com.example.n8engine.service.PhraseService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.langdetect.Language;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
@Slf4j
@Qualifier("searcher")
public class SearcherImpl implements Searcher {

    private final Dataset dataset;
    private final SearchQueryFactory searchQueryFactory;
    private final ResourceQuery resourceQuery;
    private final SearchesRepository searchesRepository;
    private final LanguageDetectorService languageDetectorService;
    private final PhraseService phraseService;
    private Integer maxRetries = 0;


    public SearcherImpl(Environment environment, SearchQueryFactory searchQueryFactory, ResourceQuery resourceQuery, SearchesRepository searchesRepository, LanguageDetectorService languageDetectorService, PhraseService phraseService) {
        this.searchQueryFactory = searchQueryFactory;
        this.resourceQuery = resourceQuery;
        this.searchesRepository = searchesRepository;
        this.languageDetectorService = languageDetectorService;
        this.phraseService = phraseService;
        Path assemblerPath = Paths.get(Objects.requireNonNull(environment.getProperty("jena.resource.assembler-lucene")));
        String assemblerAbsolutPath = assemblerPath.toAbsolutePath().toString();
        String resourceURI = environment.getProperty("jena.resource.uri");
        Objects.requireNonNull(resourceURI, "resourceURI can not be null");
        this.dataset = DatasetFactory.assemble(assemblerAbsolutPath, resourceURI);
    }

    public Dataset getDataset() {
        return dataset;
    }

    @Cacheable("entities")
    public Set<Entity> getEntitiesBySearchQuery(SearchRequest searchRequest) {
        Set<Entity> entities = new HashSet<>();
        SearchType searchType = searchRequest.getSearchType();
        String searchQuery = searchRequest.getSearchQuery();
        String preparedPhraseQuery = phraseService.cleanPhrase(searchQuery);
        String language = buildLanguage(searchRequest);
        try {
            QueryInterface queryFactory = searchQueryFactory.create(searchType);
            Query query = queryFactory.search(preparedPhraseQuery, language);
            long startTime = System.currentTimeMillis();
            this.getDataset().begin(ReadWrite.READ);
            try (QueryExecution queryExecution = QueryExecutionFactory.create(query, this.getDataset())) {
                ResultSet results = queryExecution.execSelect();
                buildEntitiesFromResults(entities, results);
            } finally {
                this.getDataset().end();
            }
            long finishTime = System.currentTimeMillis();
            long queryRunningTime = finishTime - startTime;
            saveSearchIfNotExists(searchQuery, queryRunningTime);
        } catch (SearchTypeNotFoundException e) {
            log.error("Search Type not found: " + e.getMessage());
        }

        if (entities.isEmpty() && !this.maxRetries.equals(2)) {
            this.maxRetries += 1;
            String fallbackSearchQuery = "*" + searchQuery + "*";
            searchRequest.setSearchQuery(fallbackSearchQuery);
            return this.getEntitiesBySearchQuery(searchRequest);
        }
        this.maxRetries = 0;
        return entities;
    }

    private String buildLanguage(SearchRequest searchRequest) {
        String languageCode = searchRequest.getLanguage();
        if (!languageCode.isEmpty()) {
            return languageCode;
        }
        Language language = this.languageDetectorService.detectLanguage(searchRequest.getSearchQuery());
        String lang = language.getLang().toLowerCase(Locale.ROOT).substring(0, 2);
        log.info("Detected language: " + lang);
        return lang;
    }

    private void buildEntitiesFromResults(Set<Entity> entities, ResultSet results) {
        String score = "";
        String graph = "";
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String entityName = solution.get("entity").toString();
            String attributeName = solution.get("attribute").toString();
            String valueName = solution.get("value").toString();
            if (solution.get("score") != null) {
                score = solution.get("score").toString();
            }
            if (solution.getResource("graph") != null) {
                graph = solution.getResource("graph").toString();
            }
            if (containsName(entities, entityName)) {
                Entity entity = findByNameInList(entities, entityName);
                entities.remove(entity);
                Attribute attribute = new Attribute(attributeName);
                Value value = new Value(valueName, attribute);
                value.setScore(score);
                entity.addValue(value);
                entities.add(entity);
                continue;
            }
            Entity entity = new Entity(entityName);
            Attribute attribute = new Attribute(attributeName);
            Value value = new Value(valueName, attribute);
            value.setScore(score);
            entity.addValue(value);
            entity.setGraph(graph);
            entities.add(entity);
        }
    }

    @Async
    void saveSearchIfNotExists(String searchQuery, long queryRunningTime) {
        Optional<Searches> searches = searchesRepository.findBySearch(searchQuery);
        if (searches.isEmpty()) {
            searchesRepository.save(new Searches(searchQuery, queryRunningTime, "index"));
        } else {
            Searches searchesPresent = searches.get();
            searchesPresent.setQueryRunningTime(queryRunningTime);
            searchesRepository.save(searchesPresent);
        }
    }


    public Entity findEntityByURI(String URI) {
        Entity entity = new Entity(URI);
        try {
            Query query = resourceQuery.findByEntityUri(URI);
            this.getDataset().begin(ReadWrite.READ);
            try (QueryExecution queryExecution = QueryExecutionFactory.create(query, this.getDataset())) {
                ResultSet results = queryExecution.execSelect();
                Set<Value> values = new HashSet<>();
                while (results.hasNext()) {
                    QuerySolution solution = results.nextSolution();
                    String attributeName = solution.get("attribute").toString();
                    String valueName = solution.get("value").toString();
                    Attribute attribute = new Attribute(attributeName);
                    Value value = new Value(valueName, attribute);
                    values.add(value);
                }
                entity.setValues(values);
            } finally {
                this.getDataset().end();
            }
        } catch (EntityNotFoundException e) {
            log.error("URI not found: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public Set<Entity> findOntologyPropertiesByURI(String URI) {
        Set<Entity> entities = new HashSet<>();
        Query query = resourceQuery.findOntologyProperties(URI);
        this.getDataset().begin(ReadWrite.READ);
        try (QueryExecution queryExecution = QueryExecutionFactory.create(query, this.getDataset())) {
            ResultSet results = queryExecution.execSelect();
            buildEntitiesFromResults(entities, results);
        } finally {
            this.getDataset().end();
        }

        return entities;
    }

    private Boolean containsName(final Set<Entity> entities, final String name) {
        return entities.parallelStream().map(Entity::getId).anyMatch(name::equals);
    }

    private Entity findByNameInList(final Set<Entity> entities, final String name) {
        return entities.parallelStream().filter(entity -> name.equals(entity.getId())).findFirst().orElse(null);
    }
}
