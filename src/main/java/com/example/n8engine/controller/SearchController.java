package com.example.n8engine.controller;

import com.example.n8engine.reponse.SearchResponse;
import com.example.n8engine.request.SearchRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @GetMapping("/{searchRequest}")
    public SearchResponse search(@RequestBody SearchRequest searchRequest) {
        SearchResponse searchResponse = new SearchResponse();
        return searchResponse;
    }
}
