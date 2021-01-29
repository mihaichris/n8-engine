package com.example.n8engine.controller;

import com.example.n8engine.dto.SearchRequest;
import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.utils.JsonUtils;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/search")
final public class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final Searcher searcher;

    public SearchController(@Qualifier("searcher") Searcher searcher) {
        this.searcher = searcher;
    }

    @PostMapping()
    public  Set<Entity> search(@RequestBody SearchRequest searchRequest) {
        try {
            SearchType searchType = searchRequest.getSearchType();
            String searchQuery = searchRequest.getSearchQuery();
            Set<Entity> entities = this.searcher.getEntitiesBySearchQuery(searchType, searchQuery);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JsonldModule());
            for (Entity entity: entities) {
                String personJsonLd = objectMapper.writeValueAsString(entity);
                Object jsonObject = JsonUtils.fromString(personJsonLd);
                System.out.println(jsonObject);
            }
            return entities;
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
