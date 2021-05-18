package com.example.n8engine.controller;


import com.example.n8engine.service.CachingManager;
import com.example.n8engine.dto.EntityRequest;
import com.example.n8engine.dto.EntityResponse;
import com.example.n8engine.mapper.JsonLdMapper;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/entity")
final public class EntityController {
    private final JsonLdMapper jsonLdMapper;
    private final Searcher searcher;
    private String uri;
    private final CachingManager cachingManager;

    public EntityController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper, CachingManager cachingManager) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
        this.cachingManager = cachingManager;
    }

    @GetMapping
    public void closeCache() {
        this.cachingManager.stopPersistentCacheManager();
    }

//    public void deleteEntity() {
//        this.searcher.getDataset().getDefaultModel().remove()
//    }

    @PostMapping()
    public EntityResponse findByEntity(@RequestBody EntityRequest entityRequest) {
        if (cachingManager.getEntityCache().containsKey(entityRequest.getUri())) {
            return cachingManager.getEntityCache().get(entityRequest.getUri());
        }
        EntityResponse entityResponse = new EntityResponse();

        String Id = entityRequest.getUri();
        try {
            Set<Entity> ontologyProperties = this.searcher.findOntologyPropertiesByURI(Id);
            Set<Object> entityDescription = this.filterEntityDescription(ontologyProperties, Id);
            Set<Object> entityClasses = this.filterEntityClasses(ontologyProperties, Id);
            Set<Object> entityProperties = this.filterEntityProperties(ontologyProperties, Id);

            entityResponse.setEntityDescription(entityDescription);
            entityResponse.setEntityClasses(entityClasses);
            entityResponse.setEntityProperties(entityProperties);
            cachingManager.getEntityCache().put(Id, entityResponse);
            return entityResponse;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }

    private Set<Object> filterEntityDescription(Set<Entity> ontologyProperties, String uri) {
        Set<Object> entityDescriptionJsonLd = new HashSet<>();
        Set<Entity> entityDescription = ontologyProperties.parallelStream().filter(entity -> entity.getId().equals(uri)).collect(Collectors.toSet());
        entityDescription.parallelStream().forEach(entityDescriptionJsonLd::add);
        return entityDescriptionJsonLd;
    }

    private Set<Object> filterEntityClasses(Set<Entity> ontologyProperties,  String uri) {
        Set<Object> entityClassesJsonLd = new HashSet<>();
        Set<Entity> entityClasses = ontologyProperties.parallelStream().filter(entity -> {
            if (entity.getId().equals(uri)) {
                return false;
            }
            String[] entityId = entity.getId().split("#");
            String lastSegment = entityId[entityId.length - 1];
            return Character.isUpperCase(lastSegment.charAt(0));
        }).collect(Collectors.toSet());
        entityClasses.parallelStream().forEach(entityClassesJsonLd::add);
        return entityClassesJsonLd;
    }

    private Set<Object> filterEntityProperties(Set<Entity> ontologyProperties,  String uri) {
        Set<Object> entityPropertiesJsonLd = new HashSet<>();
        Set<Entity> entityProperties = ontologyProperties.parallelStream().filter(entity -> {
            if (entity.getId().equals(uri)) {
                return false;
            }
            String[] entityId = entity.getId().split("#");
            String lastSegment = entityId[entityId.length - 1];
            return Character.isLowerCase(lastSegment.charAt(0));
        }).collect(Collectors.toSet());
        entityProperties.parallelStream().forEach(entityPropertiesJsonLd::add);
        return entityPropertiesJsonLd;
    }

    @PreDestroy
    public void destroy() {
        this.cachingManager.stopPersistentCacheManager();
    }
}
