package com.example.n8engine.controller;

import com.example.n8engine.suggester.Suggester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/suggest")
final public class SuggestController {
    private final Suggester suggester;

    public SuggestController(@Qualifier("suggester") Suggester suggester) {
        this.suggester = suggester;
    }

    @GetMapping("/{query}")
    public List<String> suggest(@PathVariable String query) {
        try {
            List<String> suggestions = this.suggester.getSuggestionsBySearchQuery(query).stream().limit(3).collect(Collectors.toUnmodifiableList());
            return suggestions;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
