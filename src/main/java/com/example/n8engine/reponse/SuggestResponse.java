package com.example.n8engine.reponse;

import java.util.List;

public class SuggestResponse {
    public String suggest;
    public List<String> similarSearches;

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public List<String> getSimilarSearches() {
        return similarSearches;
    }

    public void setSimilarSearches(List<String> similarSearches) {
        this.similarSearches = similarSearches;
    }
}
