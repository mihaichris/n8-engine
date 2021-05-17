package com.example.n8engine.query;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;

import java.util.List;

public interface QueryInterface {
    String PREFIXES = StrUtils.strjoinNL(
            "PREFIX n8: <http://n8.org/#>"
            , "PREFIX text: <http://jena.apache.org/text#>"
            , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
            , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            , "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
            , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
            , "PREFIX skos:    <http://www.w3.org/2004/02/skos/core#>"
    );
    List<String> FIELDS = List.of("rdfs:comment","rdfs:label", "rdf:label", "dc:description", "dc:title", "skos:prefLabel", "skos:altLabel");
    Query search(String searchQuery, String languageCode);
    Query findByEntityUri(String URI);
    Query findOntologyProperties(String URI);
}
