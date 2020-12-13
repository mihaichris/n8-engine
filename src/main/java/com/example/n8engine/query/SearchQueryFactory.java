package com.example.n8engine.query;

import com.example.n8engine.enumeration.SearchType;
import com.example.n8engine.exception.SearchTypeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SearchQueryFactory {

    static public QueryInterface create(SearchType searchType, String searchQuery) throws SearchTypeNotFoundException {
        switch (searchType) {
            case DOCUMENTS:
                return new DocumentQuery(searchQuery);
            case TERMS:
                return new TermQuery(searchQuery);
        }

        throw new SearchTypeNotFoundException("Search type not found.");
    }
}
