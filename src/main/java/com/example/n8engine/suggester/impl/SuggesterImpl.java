package com.example.n8engine.suggester.impl;

import com.example.n8engine.semantic.analyzer.SemanticAutosuggestionAnalyzer;
import com.example.n8engine.suggester.Suggester;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("suggester")
public class SuggesterImpl implements Suggester {

    private AnalyzingSuggester analyzingSuggester;

    public SuggesterImpl(Environment environment) {
        String autosuggestionIndexPath = environment.getProperty("lucene.suggestion.index.path");
        try {
            Directory autosuggestionDirectory = FSDirectory.open(Path.of(autosuggestionIndexPath));
            buildAnalyzingSuggester(autosuggestionDirectory, new SemanticAutosuggestionAnalyzer());
        } catch (IOException exception) {
            log.error("Error reading index:" + exception.getMessage());
        }
    }

    @SneakyThrows()
    public List<String> getSuggestionsBySearchQuery(String term) {
        List<Lookup.LookupResult> lookup = this.analyzingSuggester.lookup(term, false, 5);
        List<String> suggestions = lookup.stream().map(a -> a.key.toString()).collect(Collectors.toList());

        return suggestions;
    }

    private void buildAnalyzingSuggester(Directory autocompleteDirectory, Analyzer autocompleteAnalyzer)
            throws IOException {
        DirectoryReader sourceReader = DirectoryReader.open(autocompleteDirectory);
        LuceneDictionary luceneDictionary = new LuceneDictionary(sourceReader, "description");
        this.analyzingSuggester = new AnalyzingSuggester(autocompleteDirectory, "autocomplete_temp",
                autocompleteAnalyzer);
        this.analyzingSuggester.build(luceneDictionary);
    }
}
