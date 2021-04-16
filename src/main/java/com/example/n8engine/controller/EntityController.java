package com.example.n8engine.controller;


import com.example.n8engine.dto.EntityResponse;
import com.example.n8engine.mapper.JsonLdMapper;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/api/entity")
final public class EntityController {
    private final JsonLdMapper jsonLdMapper;
    private final Searcher searcher;

    public EntityController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
    }

    @GetMapping("/{Id}")
    public EntityResponse findByEntity(@PathVariable String Id) {
        EntityResponse entityResponse = new EntityResponse();
        Set<Object> ontologyPropertyJsonLd = new HashSet<>();
        try {
            Entity entity = this.searcher.findEntityByURI(Id);
            entityResponse.setEntity(this.jsonLdMapper.mapFromEntity(entity));
            Set<Entity> ontologyProperties = this.searcher.findOntologyPropertiesByURI(Id);
            for (Entity ontologyProperty: ontologyProperties) {
                ontologyPropertyJsonLd.add(this.jsonLdMapper.mapFromEntity(ontologyProperty));
            }
            entityResponse.setOntologyProperties(ontologyPropertyJsonLd);
            return entityResponse;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
