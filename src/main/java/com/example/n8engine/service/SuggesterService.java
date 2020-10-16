package com.example.n8engine.service;

import java.util.ArrayList;

public interface SuggesterService {
    public ArrayList<String> getSuggestionsBySearchQuery(String query);
    public String getSearchQueryCorrection(String query);
}
