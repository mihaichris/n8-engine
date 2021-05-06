package com.example.n8engine.semantic.analyzer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.IOException;
import java.io.Reader;

public class SemanticAnalyzer extends StopwordAnalyzerBase {

    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

    public SemanticAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    public SemanticAnalyzer() {
        this(CharArraySet.EMPTY_SET);
    }

    public SemanticAnalyzer(Reader stopwords) throws IOException {
        this(loadStopwordSet(stopwords));
    }

    public void setMaxTokenLength(int length) {
        maxTokenLength = length;
    }

    public int getMaxTokenLength() {
        return maxTokenLength;
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        src.setMaxTokenLength(maxTokenLength);
        TokenStream tok = new LowerCaseFilter(src);
        tok = new StopFilter(tok, stopwords);
        return new TokenStreamComponents(r -> {
            src.setMaxTokenLength(SemanticAnalyzer.this.maxTokenLength);
            src.setReader(r);
        }, tok);
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }
//
//    final CharArraySet englishStopWords = new CharArraySet(Arrays.asList(
//            "a", "an", "and", "are", "as", "at", "be", "but", "by",
//            "for", "if", "in", "into", "is", "it",
//            "no", "not", "of", "on", "or", "such",
//            "that", "the", "their", "then", "there", "these",
//            "they", "this", "to", "was", "will", "with"
//    ), false);
//
//    @Override
//    protected TokenStreamComponents createComponents(String fieldName) {
//        final StandardTokenizer src = new StandardTokenizer();
//        TokenStream result = new LowerCaseFilter(src);
//        result = new StopFilter(result, englishStopWords);
//        result = new PorterStemFilter(result);
//        result = new CapitalizationFilter(result);
//        return new TokenStreamComponents(src, result);
//    }
}
