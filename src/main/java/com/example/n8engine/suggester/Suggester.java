package com.example.n8engine.suggester;

import java.util.ArrayList;

public interface Suggester {
    ArrayList<String> getSuggestionsBySearchQuery(String query);
    String getSearchQueryCorrection(String query);
}
