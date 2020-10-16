package com.example.n8engine.controller;

import com.example.n8engine.model.Entity;
import com.example.n8engine.model.Value;
import com.example.n8engine.reponse.SearchResponse;
import com.example.n8engine.service.JenaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);

    private JenaService jenaService;

    public SearchController(JenaService jenaService) {
        this.jenaService = jenaService;
    }

    @GetMapping("/{query}")
    public SearchResponse search(@PathVariable String query) {
        SearchResponse searchResponse = new SearchResponse();
        try {
            ArrayList<Entity> entities = this.jenaService.getEntitiesBySearchQuery(query);
            searchResponse.setEntities(entities);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return searchResponse;
    }

    @GetMapping("/{uid}")
    public Entity find(@PathVariable String uid) {
        try {
            return this.jenaService.findEntityByUID(uid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new Entity(new ArrayList<Value>(), "");
    }
}
