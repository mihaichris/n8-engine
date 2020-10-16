package com.example.n8engine.reponse;

import java.util.List;

public class SuggestResponse {
    public String correction;
    public List<String> similarSearches;

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public List<String> getSimilarSearches() {
        return similarSearches;
    }

    public void setSimilarSearches(List<String> similarSearches) {
        this.similarSearches = similarSearches;
    }
}
