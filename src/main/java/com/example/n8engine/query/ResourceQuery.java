package com.example.n8engine.query;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ResourceQuery implements QueryInterface {

    /*
     * Search by all triples
     */
    @Override
    public Query search(String searchQuery) {
        String prefix = StrUtils.strjoinNL(
                "PREFIX : <http://n8.ro/#>"
                , "PREFIX text: <http://jena.apache.org/text#>"
                , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                , "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
                , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT ?entity ?attribute ?literal ?score ?graph "
                , " WHERE {"
                ,  "(?entity ?score ?literal ?graph ?attribute) text:query ( " + "\"" + searchQuery + "\" 'lang:" + "en" + "')."
                ,"}"
        );
        String query = prefix + "\n" + queryString;
        log.info(query);
        return QueryFactory.create(query);
    }

    @Override
    public Query findByEntityUri(String URI) {
        String prefix = StrUtils.strjoinNL(
                " "
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT ?attribute ?value"
                , " WHERE {"
                ,  URI + " ?attribute ?value."
                ,"}"
        );

        return QueryFactory.create(prefix + "\n" + queryString);
    }
}
