package com.example.n8engine.semantic.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;

public class SemanticAnalyzer extends Analyzer {

    final CharArraySet englishStopWords = new CharArraySet(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "but", "by",
            "for", "if", "in", "into", "is", "it",
            "no", "not", "of", "on", "or", "such",
            "that", "the", "their", "then", "there", "these",
            "they", "this", "to", "was", "will", "with"
    ), false);

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(src);
        result = new StopFilter(result, englishStopWords);
        result = new PorterStemFilter(result);
        result = new CapitalizationFilter(result);
        return new TokenStreamComponents(src, result);
    }
}
