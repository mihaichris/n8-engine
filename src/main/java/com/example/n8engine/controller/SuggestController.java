package com.example.n8engine.controller;

import com.example.n8engine.reponse.SuggestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @GetMapping
    public SuggestResponse suggest(String keyword) {
        SuggestResponse suggestResponse = new SuggestResponse();
        return suggestResponse;
    }
}
