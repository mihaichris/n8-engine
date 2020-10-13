package com.example.n8engine.controller;

import com.example.n8engine.reponse.SearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @GetMapping("/{query}")
    public SearchResponse search(@PathVariable String query) {
        SearchResponse searchResponse = new SearchResponse();
        return searchResponse;
    }
}
