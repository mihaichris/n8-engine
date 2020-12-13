package com.example.n8engine.query;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

public class DocumentQuery implements QueryInterface{

    private final String searchQuery;

    public DocumentQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public Query getQuery() {
        String prefix = StrUtils.strjoinNL(
                "PREFIX : <http://n8.ro/#>"
                , "PREFIX text: <http://jena.apache.org/text#>"
                , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                , "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
                , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT ?entity, ?attribute, ?literal, ?score, ?graph "
                , "{"
                , " ?entity rdf:type owl:Ontology. "
                ,  "(?entity ?score ?literal ?graph ?attribute) text:query " + this.getSearchQuery() + ". "
                ,"}"
        );
        Query query = QueryFactory.create(prefix + "\n" + queryString);

        return query;
    }

    private String getSearchQuery() {
        return this.searchQuery;
    }
}
