package com.example.n8engine.controller;

import com.example.n8engine.dto.SearchResponse;
import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/byOntology/{searchType}/{searchQuery}")
    public SearchResponse search(@PathVariable SearchType searchType, @PathVariable String searchQuery) {
        try {
            Set<Entity> entities = this.searcher.getEntitiesBySearchQuery(searchType, searchQuery);
            return new SearchResponse(entities);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
