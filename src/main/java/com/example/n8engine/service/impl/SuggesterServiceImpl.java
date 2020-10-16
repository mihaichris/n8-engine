package com.example.n8engine.service.impl;

import com.example.n8engine.service.SuggesterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SuggesterServiceImpl implements SuggesterService {

    public ArrayList<String> getSuggestionsBySearchQuery(String query) {
        //TODO Implementare preluare sugestii
        return new ArrayList<String>();
    }

    public String getSearchQueryCorrection(String query) {
        return "";
    }
}
