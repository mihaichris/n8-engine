package com.example.n8engine.controller;

import com.example.n8engine.reponse.SuggestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @GetMapping("/{query}")
    public SuggestResponse suggest(@PathVariable String query) {
        SuggestResponse suggestResponse = new SuggestResponse();
        return suggestResponse;
    }
}
