package com.example.n8engine.dto;

import java.util.List;

final public class SuggestResponse {
    public String correction;
    public List<String> similarSearches;

    public SuggestResponse(String correction, List<String> similarSearches) {
        this.correction = correction;
        this.similarSearches = similarSearches;
    }

    public String getCorrection() {
        return correction;
    }

    public List<String> getSimilarSearches() {
        return similarSearches;
    }
}
