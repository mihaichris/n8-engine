package com.example.n8engine.repository;

import com.example.n8engine.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Query.Operator;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Collection;

public interface ProductRepository extends SolrCrudRepository<Product, String> {

    @Highlight(prefix = "<b>", postfix = "</b>")
    @Query(fields = {Product.ID_FIELD, Product.NAME_FIELD,
            Product.PRICE_FIELD, Product.FEATURES_FIELD,
            Product.AVAILABLE_FIELD}, defaultOperator = Operator.AND)
    HighlightPage<Product> findByNameIn(Collection<String> names, Pageable page);

    @Facet(fields = {Product.NAME_FIELD})
    FacetPage<Product> findByNameStartsWith(Collection<String> nameFragments, Pageable pageable);
}
