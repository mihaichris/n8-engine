package com.example.n8engine.semantic.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordTokenizer;

public class SemanticQueryAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new KeywordTokenizer());
    }
}
