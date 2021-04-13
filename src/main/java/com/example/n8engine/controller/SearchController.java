package com.example.n8engine.controller;

import com.example.n8engine.dto.SearchRequest;
import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.mapper.JsonLdMapper;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import com.example.n8engine.service.PhraseService;
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
    private final PhraseService phraseService;

    public SearchController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper, PhraseService phraseService) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
        this.phraseService = phraseService;
    }

    @PostMapping()
    public  Set<Object> search(@RequestBody SearchRequest searchRequest) {
        Set<Object> entitiesJsonLd = new HashSet<>();
        try {
            SearchType searchType = searchRequest.getSearchType();
            String searchQuery = searchRequest.getSearchQuery();
            String preparedPhraseQuery = phraseService.cleanPhrase(searchQuery);
            String languageCode = searchRequest.getLanguage();
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
