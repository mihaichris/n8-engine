package com.example.n8engine.controller;

import com.example.n8engine.dto.SearchRequest;
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

@RestController
@Slf4j
@RequestMapping("/api/search")
final public class SearchController {

    private final Searcher searcher;
    private final JsonLdMapper jsonLdMapper;

    public SearchController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
    }

    @PostMapping()
    public  Set<Object> search(@RequestBody SearchRequest searchRequest) {
        Set<Object> entitiesJsonLd = new HashSet<>();
        try {
            Set<Entity> entities = this.searcher.getEntitiesBySearchQuery(searchRequest);
            for (Entity entity: entities) {
                Object jsonObject = this.jsonLdMapper.mapFromEntity(entity);
                entitiesJsonLd.add(jsonObject);
            }
            return entitiesJsonLd;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
