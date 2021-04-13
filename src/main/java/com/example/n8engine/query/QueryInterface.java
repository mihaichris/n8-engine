package com.example.n8engine.query;

import org.apache.jena.query.Query;

public interface QueryInterface {
    Query search(String searchQuery, String languageCode);
    Query findByEntityUri(String URI);
}
