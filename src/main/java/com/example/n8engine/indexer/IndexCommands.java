package com.example.n8engine.indexer;

import com.example.n8engine.domain.Resource;
import com.example.n8engine.domain.Searches;
import com.example.n8engine.repository.ResourceRepository;
import com.example.n8engine.repository.SearchesRepository;
import com.example.n8engine.searcher.Searcher;
import com.example.n8engine.semantic.analyzer.SemanticAutosuggestionAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.util.QueryExecUtils;
import org.apache.jena.vocabulary.DC;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ShellComponent
public class IndexCommands {
    private final Environment environment;
    private int indexedNo = 0;
    private Searcher searcher;
    private ResourceRepository resourceRepository;
    private SearchesRepository searchesRepository;
    private IndexWriter autocompleteDirectoryWriter;
    private Dataset dataset;

    public IndexCommands(Environment environment, Searcher searcher, ResourceRepository resourceRepository) {
        this.environment = environment;
        this.resourceRepository = resourceRepository;
        this.searcher = searcher;
        this.dataset = this.searcher.getDataset();
        String autosuggestionIndexPath = environment.getProperty("lucene.suggestion.index.path");
        try {
            Directory autosuggestionDirectory = FSDirectory.open(Path.of(autosuggestionIndexPath));
            this.autocompleteDirectoryWriter = new IndexWriter(autosuggestionDirectory, new IndexWriterConfig(new SemanticAutosuggestionAnalyzer()));
        } catch (IOException exception) {
            log.info("Error reading index:" + exception.getMessage());
        }

    }

    @ShellMethod("Index content from a source.")
    public Integer index(@ShellOption() String source, @ShellOption() String target) {
        log.info("Indexing " + source + " content started.");
        try {
            Dataset ds = this.searcher.getDataset();
            if (target.equals("suggests")) {
                loadSuggests(source);
            } else {
                loadData(source);
            }
            ds.end();
            saveIFNotExists(source);
        } catch (Exception exception) {
            log.error("Operation failed: " + exception.getMessage());
            return 1;
        }

        return 0;
    }

    @ShellMethod("Index content from resource table.")
    public Integer indexFromDb(@ShellOption() String target) {
        log.info("Indexing all from resource table.");
        List<Resource> resourceList = resourceRepository.findAllOrderedById();
        resourceList.parallelStream().forEach(resource -> {
            try {
                indexedNo+=1;
                log.info("Indexing: " + resource.getUri());
                if (target.equals("suggests")) {
                    loadSuggests(resource.getUri());
                    resource.setIndexedSuggestion(true);
                } else {
                    loadData(resource.getUri());
                    resource.setIndexed(true);
                }
                try {
                    resourceRepository.save(resource);
                } catch (Exception e) {
                    resourceRepository.save(resource);
                }

                log.info("Number of resource indexed:" + indexedNo);
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + resource + ":" + exception.toString());
            }
        });
        this.indexedNo = 0;
        return 0;
    }

    @ShellMethod("Index content from resource files.")
    public Integer indexFromFiles() throws IOException {
        log.info("Indexing all from resource files.");
        List<Path> txtFiles = Files.walk(Paths.get("E:\\Java Projects\\n8-engine\\src\\main\\resources\\data"))
                //use to string here, otherwise checking for path segments
                .filter(p -> p.toString().endsWith(".nt"))
                .collect(Collectors.toList());
        Dataset ds = this.searcher.getDataset();
        for (Path path : txtFiles) {
            try {
                loadData(path.getFileName().toString());
                log.info("Indexed: " + path.getFileName().toString());
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + path.getFileName().toString() + ":" + exception.toString());
            }
        }
        return 0;
    }

    private void loadData(String file) {
        log.info("Start loading data");
        long startTime = System.nanoTime();
        dataset.begin(TxnType.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            RDFDataMgr.read(model, file);
//            dataset.commit();
            commit();
        } finally {
            dataset.end();
        }
        long finishTime = System.nanoTime();
        double time = (finishTime - startTime) / 1.0e6;
        log.info(String.format("Finish loading - %.2fms", time));
    }

    @Async
//    @Scheduled(cron = "0 * * * * *")
    void commit() {
        log.info("Started commit.....");
//        dataset.begin(TxnType.WRITE);
        try {
            dataset.commit();
        } finally {
//            dataset.end();
        }
        log.info("Ended commit.....");
    }

    private void saveIFNotExists(String source) {
        Optional<Resource> resource = resourceRepository.findByUri(source);
        if (resource.isEmpty()) {
            Resource newResource = new Resource();
            newResource.setUri(source);
            resourceRepository.save(newResource);
        }
    }

    private void loadSuggests(String source) {
        log.info("Start loading suggests");
        long startTime = System.nanoTime();
        try {
            Model model = ModelFactory.createDefaultModel();
            model.read(source);
            NodeIterator nodeIterator = model.listObjectsOfProperty(DC.description);
            while (nodeIterator.hasNext()) {
                RDFNode r = nodeIterator.nextNode();
                String description = r.asLiteral().getString();
                Document document = new Document();
                document.add(new TextField("description", description, Field.Store.YES));
                autocompleteDirectoryWriter.addDocument(document);
                autocompleteDirectoryWriter.commit();
            }
        } catch (IOException exception) {
            log.info("Error reading index:" + exception.getMessage());
        }
        long finishTime = System.nanoTime();
        double time = (finishTime - startTime) / 1.0e6;
        log.info(String.format("Finish loading suggests - %.2fms", time));
    }

    @ShellMethod("Test searching")
    public Integer testSearching() throws IOException {
        log.info("Searching...");
        List<Searches> searches = this.searchesRepository.findAll();
        long startTime = System.nanoTime() ;
        String prefix = StrUtils.strjoinNL(
                "PREFIX n8: <http://n8.org/#>"
                , "PREFIX text: <http://jena.apache.org/text#>"
                , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                , "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
                , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        );
        searches.parallelStream().forEach(search -> {
            String queryNoIndex = StrUtils.strjoinNL
                    ( "SELECT * "
                            , " { "
                            , "        ?s rdfs:label ?label . "
                            , " filter ( strstarts(?label, \""+ search.getSearch() +" \") ) ."
                            , " } LIMIT 1000") ;
//            String queryIndex = StrUtils.strjoinNL
//                    ( "SELECT * "
//                            , " { "
//                            , "  ?s text:query ( n8:label \""+ search.getSearch() +"\")."
//                            , "        ?s rdfs:label ?label . "
//                            , " }") ;
//            String queryUnion = StrUtils.strjoinNL
//                    ( "SELECT * "
//                            , " { "
//                            , "  ?s text:query ( n8:label \""+ search.getSearch() +"\")."
//                            , "        ?s rdfs:label ?label . "
//                            , " }") ;
            Dataset dataset = this.searcher.getDataset();
            dataset.begin(ReadWrite.READ) ;
            try {
                Query q = QueryFactory.create(prefix+"\n"+queryNoIndex) ;
                QueryExecution qexec = QueryExecutionFactory.create(q , dataset) ;
                QueryExecUtils.executeQuery(q, qexec) ;
            } finally { dataset.end() ; }
            long finishTime = System.nanoTime() ;
            long time = finishTime-startTime;
            search.setType("filter");
//            search.setType("index");
//            search.setType("union");
            search.setQueryRunningTime(time);
            this.searchesRepository.save(search);
        });

        return 0;
    }
}
