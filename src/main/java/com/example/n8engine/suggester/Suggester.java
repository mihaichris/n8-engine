package com.example.n8engine.suggester;

import java.util.List;

public interface Suggester {
    List<String> getSuggestionsBySearchQuery(String query);
}
