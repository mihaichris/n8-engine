package com.example.n8engine.query;

import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.exception.SearchTypeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SearchQueryFactory {

    private final DocumentQuery documentQuery;
    private final TermQuery termQuery;
    private final ResourceQuery resourceQuery;

     public QueryInterface create(SearchType searchType) throws SearchTypeNotFoundException {
        switch (searchType) {
            case DOCUMENT:
                return this.documentQuery;
            case RESOURCE:
                return this.resourceQuery;
            case TERMS:
                return this.termQuery;
        }

        throw new SearchTypeNotFoundException("Search type not found.");
    }
}
