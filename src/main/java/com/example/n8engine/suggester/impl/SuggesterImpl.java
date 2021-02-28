package com.example.n8engine.suggester.impl;

import com.example.n8engine.suggester.Suggester;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggesterImpl implements Suggester {

    public SuggesterImpl(Environment environment) {
        Directory luceneDir = null;
        try
        {
//            luceneDir = new SimpleFSDirectory( new File("E:\\Java Projects\\n8-engine\\src\\main\\resources\\index\\lucene").toPath() );
//            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            luceneDir = FSDirectory.open(Paths.get("E:\\Java Projects\\n8-engine\\src\\main\\resources\\index\\lucene"));
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("E:\\Java Projects\\n8-engine\\src\\main\\resources\\index\\lucene")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Document doc = searcher.doc(0);
            IndexableField field = doc.getFields().get(1);
            System.out.println(field.name());
            TopDocs hits = searcher.search(new MatchAllDocsQuery(), 1);
            System.out.println(hits);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Analyzer autocompleteAnalyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                Tokenizer source = new StandardTokenizer();
                TokenFilter filter = new LowerCaseFilter(source);
                return new TokenStreamComponents(source, filter);
            }
        };
        try {
            DirectoryReader sourceReader = DirectoryReader.open(luceneDir);
            LuceneDictionary dict = new LuceneDictionary(sourceReader, "description");
            AnalyzingSuggester  analyzingSuggester = new AnalyzingSuggester(luceneDir, "autocomplete_temp",
                    autocompleteAnalyzer);
            analyzingSuggester.build(dict);
            System.out.println(analyzingSuggester.getCount());
            List<Lookup.LookupResult> lookup = analyzingSuggester.lookup("l", false, 5);
            List<String> suggestions = lookup.stream().map(a -> a.key.toString()).collect(Collectors.toList());
            System.out.println(suggestions);
        } catch (Exception e) {

        }

    }

    public ArrayList<String> getSuggestionsBySearchQuery(String query) {

        //TODO Implementare preluare sugestii
        return new ArrayList<String>();
    }

    public String getSearchQueryCorrection(String query) {
        return "";
    }
}
