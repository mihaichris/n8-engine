package com.example.n8engine.controller;

import com.example.n8engine.reponse.SuggestResponse;
import com.example.n8engine.service.SuggesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);

    private final SuggesterService suggesterService;

    public SuggestController(SuggesterService suggesterService) {
        this.suggesterService = suggesterService;
    }

    @GetMapping("/{query}")
    public SuggestResponse suggest(@PathVariable String query) {
        SuggestResponse suggestResponse = new SuggestResponse();
        try {
            String queryCorrection = this.suggesterService.getSearchQueryCorrection(query);
            ArrayList<String> suggestions = this.suggesterService.getSuggestionsBySearchQuery(query);
            suggestResponse.setCorrection(queryCorrection);
            suggestResponse.setSimilarSearches(suggestions);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return suggestResponse;
    }
}
