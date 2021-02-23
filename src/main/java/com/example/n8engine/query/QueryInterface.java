package com.example.n8engine.query;

import org.apache.jena.query.Query;

public interface QueryInterface {
    Query search(String searchQuery);
    Query findByEntityUri(String URI);
}
