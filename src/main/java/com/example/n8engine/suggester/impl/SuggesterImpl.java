package com.example.n8engine.suggester.impl;

import com.example.n8engine.suggester.Suggester;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SuggesterImpl implements Suggester {

    public ArrayList<String> getSuggestionsBySearchQuery(String query) {
        //TODO Implementare preluare sugestii
        return new ArrayList<String>();
    }

    public String getSearchQueryCorrection(String query) {
        return "";
    }
}
