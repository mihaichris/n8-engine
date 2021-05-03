package com.example.n8engine.query;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ResourceQuery implements QueryInterface {

    /*
     * Search by all triples
     */
    @Override
    public Query search(String searchQuery, String languageCode) {
        String field = "n8:text";
        List<String> splitSearchQuery = Arrays.asList(searchQuery.split(" "));
        if (QueryInterface.FIELDS.contains(splitSearchQuery.get(0))) {
            field = splitSearchQuery.get(0);
            searchQuery = splitSearchQuery.get(1);
        }

        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT ?entity ?attribute ?value ?score ?graph "
                , " WHERE {"
                ,  "(?entity ?score ?value ?graph ?attribute) text:query ( " + field + " \"" + searchQuery + "\" 60 'lang:" + languageCode + "' )."
                ,"}"
        );
        String query = QueryInterface.PREFIXES + "\n" + queryString;
        log.info(query);
        return QueryFactory.create(query);
    }

    @Override
    public Query findByEntityUri(String URI) {
        String prefix = StrUtils.strjoinNL(
                " "
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT *"
                , " WHERE {"
                ,  " <" + URI + ">" + " ?attribute ?value."
                ,  " ?entity ?attribute ?value. "
                ,"}"
        );
        log.info(queryString);
        return QueryFactory.create(prefix + "\n" + queryString);
    }

    @Override
    @SneakyThrows
    public Query findOntologyProperties(String URI) {
        String prefix = StrUtils.strjoinNL(
                "PREFIX ns: " + "<" + URI + ">"
        );
        String queryString = StrUtils.strjoinNL(
                "SELECT DISTINCT *"
                , " WHERE {"
                ,  " ?entity ?attribute ?value. "
                , " FILTER(isURI(?entity) && STRSTARTS(str(?entity), str(ns:))) "
                ,"}"
//                , " LIMIT 1 "
        );
        String query = prefix + "\n" + queryString;
        log.info(query);
        return QueryFactory.create(query);
    }
}
