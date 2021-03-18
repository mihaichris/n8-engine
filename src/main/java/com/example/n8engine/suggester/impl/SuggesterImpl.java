package com.example.n8engine.suggester.impl;

import com.example.n8engine.suggester.Suggester;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SuggesterImpl implements Suggester {

    private final Environment environment;

    public SuggesterImpl(Environment environment, Environment environment1) {
        this.environment = environment1;
    }

    public ArrayList<String> getSuggestionsBySearchQuery(String query) {

        //TODO Implementare preluare sugestii
        return new ArrayList<String>();
    }

    public String getSearchQueryCorrection(String query) {
        return "";
    }
}
