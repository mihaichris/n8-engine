package com.example.n8engine.query;

import lombok.AllArgsConstructor;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocumentQuery implements QueryInterface{

    /**
     * Search only by ontologies
     */
    @Override
    public Query search(String searchQuery, String languageCode) {
        String prefix = StrUtils.strjoinNL(
                "PREFIX n8: <http://n8.org/#>"
                , "PREFIX text: <http://jena.apache.org/text#>"
                , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                , "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
                , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT ?entity ?attribute ?literal ?score ?graph "
                , " WHERE {"
                , " ?entity rdf:type owl:Ontology. "
                ,  "(?entity ?score ?literal ?graph ?attribute) text:query ( n8:text " + "\"" + searchQuery + "\" 60 'lang:" + languageCode + "')."
                ,"}"
        );
        Query query = QueryFactory.create(prefix + "\n" + queryString);

        return query;
    }

    @Override
    public Query findByEntityUri(String URI) {
        return null;
    }

    @Override
    public Query findOntologyProperties(String URI) {
        return null;
    }
}
