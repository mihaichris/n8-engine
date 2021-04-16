package com.example.n8engine.dto;

import com.example.n8engine.enumeration.SearchType;
import lombok.Data;

@Data
public class SearchRequest {
    private String searchQuery;
    private SearchType searchType;
    private String language = "en";
}
