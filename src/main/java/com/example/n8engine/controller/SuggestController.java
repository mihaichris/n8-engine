package com.example.n8engine.controller;

import com.example.n8engine.dto.SuggestResponse;
import com.example.n8engine.suggester.Suggester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/suggest")
final public class SuggestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);

    private final Suggester suggester;

    public SuggestController(Suggester suggester) {
        this.suggester = suggester;
    }

    @GetMapping("/{query}")
    public SuggestResponse suggest(@PathVariable String query) {
        try {
            String queryCorrection = this.suggester.getSearchQueryCorrection(query);
            ArrayList<String> suggestions = this.suggester.getSuggestionsBySearchQuery(query);
            return new SuggestResponse(queryCorrection, suggestions);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
