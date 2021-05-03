package com.example.n8engine.controller;


import com.example.n8engine.dto.EntityRequest;
import com.example.n8engine.dto.EntityResponse;
import com.example.n8engine.mapper.JsonLdMapper;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    public EntityController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
    }

    @PostMapping()
    public EntityResponse findByEntity(@RequestBody EntityRequest entityRequest) {
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
}
